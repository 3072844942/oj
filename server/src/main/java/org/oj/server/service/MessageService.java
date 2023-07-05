package org.oj.server.service;

import org.oj.server.dao.MessageRepository;
import org.oj.server.dto.MessageDTO;
import org.oj.server.entity.Message;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.vo.MessageVO;
import org.oj.server.vo.PageVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author march
 * @since 2023/5/31 下午3:09
 */
@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public PageVO<MessageVO> findAll() {
        List<MessageVO> list = messageRepository.findAll().stream().map(MessageVO::of).toList();
        return new PageVO<>(list, (long) list.size());
    }

    public void deleteOne(String messageId) {
        Optional<Message> byId = messageRepository.findById(messageId);
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }
        Message message = byId.get();
        if (PermissionUtil.enableWrite(message.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        messageRepository.deleteById(messageId);
    }

    public MessageVO insertOne(MessageDTO messageDTO) {
        Message of = Message.of(messageDTO);
        of.setState(EntityStateEnum.PUBLIC);
        messageRepository.save(of);
        return MessageVO.of(of);
    }
}
