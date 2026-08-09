package com.corundumstudio.socketio.store;

import com.corundumstudio.socketio.store.pubsub.PubSubStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link RedisTemplateStoreFactory}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class RedisTemplateStoreFactoryTest {

    private RedisTemplate<Object, Object> redisTemplate;
    private RedisMessageListenerContainer listenerContainer;
    private RedisTemplateStoreFactory factory;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        listenerContainer = mock(RedisMessageListenerContainer.class);
        factory = new RedisTemplateStoreFactory(redisTemplate, listenerContainer);
    }

    @Test
    void createStoreShouldReturnRedisTemplateStore() {
        UUID sessionId = UUID.randomUUID();
        Store store = factory.createStore(sessionId);
        assertThat(store).isInstanceOf(RedisTemplateStore.class);
    }

    @Test
    void pubSubStoreShouldReturnRedisTemplatePubSubStore() {
        PubSubStore pubSubStore = factory.pubSubStore();
        assertThat(pubSubStore).isInstanceOf(RedisTemplatePubSubStore.class);
    }

    @Test
    void shutdownShouldNotThrow() {
        factory.shutdown();
    }

    @Test
    void createMapShouldReturnRedisTemplateMap() {
        Map<String, String> map = factory.createMap("test-map");
        assertThat(map).isInstanceOf(RedisTemplateMap.class);
    }
}
