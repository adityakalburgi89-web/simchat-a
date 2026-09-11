package com.SIMCHAT_A.SIMCHAT_A.confiq;

import com.SIMCHAT_A.SIMCHAT_A.service.SseEmitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Slf4j
@Configuration
public class RedisPubSubConfig {

    // => redis pub/sub message listener container
    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                         SseEmitterService sseEmitterService) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // => pub/sub listener -> broadcast to sse stream
        MessageListener listener = (Message message, byte[] pattern) -> {
            String channel = new String(message.getChannel());
            String body = new String(message.getBody());
            log.info("Pub/Sub Received on channel {}: {}", channel, body);

            // => extract roomId from chatroom:{roomId}:channel
            if (channel.startsWith("chatroom:") && channel.endsWith(":channel")) {
                String roomId = channel.substring("chatroom:".length(), channel.length() - ":channel".length());
                sseEmitterService.broadcast(roomId, body);
            }
        };

        // => subscribe to pattern chatroom:*:channel
        container.addMessageListener(listener, new PatternTopic("chatroom:*:channel"));
        return container;
    }
}
