package org.oj.server.config;

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
    @Autowired
    private MongoMessageListener mongoMessageListener;
    @Autowired
    private MessageListenerContainer messageListenerContainer;

    @Override
    public void afterPropertiesSet() {
        ChangeStreamRequest<Object> request = ChangeStreamRequest.builder(mongoMessageListener).database(database).build();
        messageListenerContainer.register(request, Object.class);
    }
}
