package com.corundumstudio.socketio.spring.boot;

import com.corundumstudio.socketio.store.StoreFactory;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SocketIORedisTemplateConfiguration}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class SocketIORedisTemplateConfigurationTest {

    private final SocketIORedisTemplateConfiguration configuration = new SocketIORedisTemplateConfiguration();

    @Test
    void socketIoRedisTemplateShouldReturnConfiguredTemplate() throws Exception {
        RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
        RedisTemplate<Object, Object> template = configuration.socketIoRedisTemplate(factory);
        assertThat(template).isNotNull();
        assertThat(template.getKeySerializer()).isNotNull();
        assertThat(template.getValueSerializer()).isNotNull();
        assertThat(template.getHashKeySerializer()).isNotNull();
        assertThat(template.getHashValueSerializer()).isNotNull();
    }

    @Test
    void socketIoRedisMessageListenerContainerShouldReturnContainer() {
        RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
        RedisMessageListenerContainer container = configuration.socketIoRedisMessageListenerContainer(factory);
        assertThat(container).isNotNull();
    }

    @Test
    void clientStoreFactoryShouldReturnStoreFactory() throws IOException {
        RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
        StoreFactory storeFactory = configuration.clientStoreFactory(factory);
        assertThat(storeFactory).isNotNull();
    }

    @Test
    void prefixConstantShouldBeCorrect() {
        assertThat(SocketIORedisTemplateProperties.PREFIX).isEqualTo("socket-io.cache.redis-template");
    }
}
