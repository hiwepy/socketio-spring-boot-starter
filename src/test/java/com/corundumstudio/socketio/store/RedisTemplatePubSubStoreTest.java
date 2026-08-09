package com.corundumstudio.socketio.store;

import com.corundumstudio.socketio.store.pubsub.PubSubMessage;
import com.corundumstudio.socketio.store.pubsub.PubSubType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link RedisTemplatePubSubStore}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class RedisTemplatePubSubStoreTest {

    private RedisTemplate<Object, Object> redisTemplate;
    private RedisMessageListenerContainer listenerContainer;
    private RedisTemplatePubSubStore pubSubStore;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        listenerContainer = mock(RedisMessageListenerContainer.class);
        pubSubStore = new RedisTemplatePubSubStore(redisTemplate, listenerContainer, 1L);
    }

    @Test
    void publishShouldSetNodeIdAndSend() {
        PubSubMessage msg = mock(PubSubMessage.class);
        pubSubStore.publish(PubSubType.CONNECT, msg);
        verify(msg).setNodeId(1L);
        verify(redisTemplate).convertAndSend(PubSubType.CONNECT.toString(), msg);
    }

    @Test
    void subscribeShouldAddMessageListener() {
        Topic topic = new ChannelTopic(PubSubType.CONNECT.toString());
        doNothing().when(listenerContainer).addMessageListener(any(), eq(topic));
        pubSubStore.subscribe(PubSubType.CONNECT, message -> {}, PubSubMessage.class);
        verify(listenerContainer).addMessageListener(any(), eq(topic));
    }

    @Test
    void unsubscribeShouldRemoveListeners() {
        // First subscribe to register a listener
        pubSubStore.subscribe(PubSubType.DISCONNECT, message -> {}, PubSubMessage.class);
        // Then unsubscribe
        pubSubStore.unsubscribe(PubSubType.DISCONNECT);
        verify(listenerContainer).removeMessageListener(any());
    }

    @Test
    void shutdownShouldNotThrow() {
        assertThatNoException().isThrownBy(pubSubStore::shutdown);
    }
}
