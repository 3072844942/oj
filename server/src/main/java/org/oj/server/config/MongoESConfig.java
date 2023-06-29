package org.oj.server.config;

import com.mongodb.client.model.changestream.FullDocument;
import org.self.MongoMessageListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest;
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer;

/**
 * @author march
 * @since 2023/6/26 下午5:24
 */
@Configuration
@ComponentScan("org.self")
public class MongoESConfig implements InitializingBean {
    @Value("${spring.data.mongodb.authentication-database}")
    private String database;
    private final MongoMessageListener mongoMessageListener;
    private final MessageListenerContainer messageListenerContainer;

    public MongoESConfig(MongoMessageListener mongoMessageListener, MessageListenerContainer messageListenerContainer) {
        this.mongoMessageListener = mongoMessageListener;
        this.messageListenerContainer = messageListenerContainer;
    }

    @Override
    public void afterPropertiesSet() {
        // 监听整个数据库
        ChangeStreamRequest<Object> request = ChangeStreamRequest
                .builder(mongoMessageListener)
                .database(database)
                .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)
                .build();
        messageListenerContainer.register(request, Object.class);
    }
}
