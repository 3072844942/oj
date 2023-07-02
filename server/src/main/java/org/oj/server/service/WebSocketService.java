package org.oj.server.service;

import com.alibaba.fastjson.JSON;
import jakarta.websocket.*;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import org.oj.server.dao.ChatRecordRepository;
import org.oj.server.dto.RecallMessageDTO;
import org.oj.server.dto.VoiceDTO;
import org.oj.server.dto.WebsocketMessageDTO;
import org.oj.server.entity.ChatRecord;
import org.oj.server.enums.ChatTypeEnum;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.HTMLUtils;
import org.oj.server.util.IpUtils;
import org.oj.server.vo.ChatRecordVO;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author march
 * @since 2023/7/2 下午4:30
 */
@Component
@ServerEndpoint(value = "/websocket", configurator = WebSocketService.ChatConfigurator.class)
public class WebSocketService {
    /**
     * 用户session集合
     */
    private static final CopyOnWriteArraySet<WebSocketService> webSocketSet = new CopyOnWriteArraySet<>();
    private final ChatRecordRepository chatRecordRepository;
    private final UploadService uploadService;
    /**
     * 用户session
     */
    private Session session;

    public WebSocketService(ChatRecordRepository chatRecordRepository, UploadService uploadService) {
        this.chatRecordRepository = chatRecordRepository;
        this.uploadService = uploadService;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) throws IOException {
        // 加入连接
        this.session = session;
        webSocketSet.add(this);
        // 更新在线人数
        updateOnlineCount();
        // 加载历史聊天记录
        ChatRecordVO chatRecordVO = listChatRecords(endpointConfig);
        // 发送消息
        WebsocketMessageDTO messageDTO = WebsocketMessageDTO.builder()
                .type(ChatTypeEnum.HISTORY_RECORD.getType())
                .data(chatRecordVO)
                .build();
        synchronized (session) {
            session.getBasicRemote().sendText(JSON.toJSONString(messageDTO));
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        WebsocketMessageDTO messageDTO = JSON.parseObject(message, WebsocketMessageDTO.class);
        switch (Objects.requireNonNull(ChatTypeEnum.getChatType(messageDTO.getType()))) {
            case SEND_MESSAGE:
                // 发送消息
                ChatRecord chatRecord = JSON.parseObject(JSON.toJSONString(messageDTO.getData()), ChatRecord.class);
                // 过滤html标签
                chatRecord.setContent(HTMLUtils.deleteTag(chatRecord.getContent()));
                chatRecordRepository.insert(chatRecord);
                messageDTO.setData(chatRecord);
                // 广播消息
                broadcastMessage(messageDTO);
                break;
            case RECALL_MESSAGE:
                // 撤回消息
                RecallMessageDTO recallMessage = JSON.parseObject(JSON.toJSONString(messageDTO.getData()), RecallMessageDTO.class);
                // 删除记录
                chatRecordRepository.deleteById(recallMessage.getId());
                // 广播消息
                broadcastMessage(messageDTO);
                break;
            case HEART_BEAT:
                // 心跳消息
                messageDTO.setData("pong");
                session.getBasicRemote().sendText(JSON.toJSONString(JSON.toJSONString(messageDTO)));
            default:
                break;
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() throws IOException {
        // 更新在线人数
        webSocketSet.remove(this);
        updateOnlineCount();
    }

    /**
     * 加载历史聊天记录
     *
     * @param endpointConfig 配置
     * @return 加载历史聊天记录
     */
    private ChatRecordVO listChatRecords(EndpointConfig endpointConfig) {
        // 获取聊天历史记录
        List<ChatRecord> chatRecordList = chatRecordRepository.findAll(Sort.by(Sort.Order.desc("updateTime")));
        // 获取当前用户ip
        String ipAddress = endpointConfig.getUserProperties().get(ChatConfigurator.HEADER_NAME).toString();
        return ChatRecordVO.builder()
                .chatRecordList(chatRecordList)
                .ipAddress(ipAddress)
                .ipSource(IpUtils.getIpSource(ipAddress))
                .build();
    }

    /**
     * 更新在线人数
     *
     * @throws IOException io异常
     */
    @Async
    public void updateOnlineCount() throws IOException {
        // 获取当前在线人数
        WebsocketMessageDTO messageDTO = WebsocketMessageDTO.builder()
                .type(ChatTypeEnum.ONLINE_COUNT.getType())
                .data(webSocketSet.size())
                .build();
        // 广播消息
        broadcastMessage(messageDTO);
    }

    /**
     * 发送语音
     *
     * @param voiceVO 语音路径
     */
    public void sendVoice(VoiceDTO voiceVO) {
        // 上传语音文件
        String content = uploadService.uploadVideo(voiceVO.getFile());
        voiceVO.setContent(content);
        // 保存记录
        ChatRecord chatRecord = BeanCopyUtils.copyObject(voiceVO, ChatRecord.class);
        chatRecordRepository.insert(chatRecord);
        // 发送消息
        WebsocketMessageDTO messageDTO = WebsocketMessageDTO.builder()
                .type(ChatTypeEnum.VOICE_MESSAGE.getType())
                .data(chatRecord)
                .build();
        // 广播消息
        try {
            broadcastMessage(messageDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 广播消息
     *
     * @param messageDTO 消息dto
     * @throws IOException io异常
     */
    private void broadcastMessage(WebsocketMessageDTO messageDTO) throws IOException {
        for (WebSocketService webSocketService : webSocketSet) {
            synchronized (webSocketService.session) {
                webSocketService.session.getBasicRemote().sendText(JSON.toJSONString(messageDTO));
            }
        }
    }

    /**
     * 获取客户端真实ip
     */
    public static class ChatConfigurator extends ServerEndpointConfig.Configurator {

        public static String HEADER_NAME = "X-Real-IP";

        @Override
        public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
            try {
                String firstFoundHeader = request.getHeaders().get(HEADER_NAME.toLowerCase()).get(0);
                sec.getUserProperties().put(HEADER_NAME, firstFoundHeader);
            } catch (Exception e) {
                sec.getUserProperties().put(HEADER_NAME, "未知ip");
            }
        }
    }
}
