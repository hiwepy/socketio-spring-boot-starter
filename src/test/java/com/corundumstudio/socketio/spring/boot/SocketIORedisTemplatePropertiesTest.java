package com.corundumstudio.socketio.spring.boot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SocketIORedisTemplateProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class SocketIORedisTemplatePropertiesTest {

    @Test
    void defaultEnabledShouldBeFalse() {
        SocketIORedisTemplateProperties props = new SocketIORedisTemplateProperties();
        assertThat(props.isEnabled()).isFalse();
    }

    @Test
    void setEnabledShouldStoreValue() {
        SocketIORedisTemplateProperties props = new SocketIORedisTemplateProperties();
        props.setEnabled(true);
        assertThat(props.isEnabled()).isTrue();
    }

    @Test
    void prefixConstantShouldBeCorrect() {
        assertThat(SocketIORedisTemplateProperties.PREFIX).isEqualTo("socket-io.cache.redis-template");
    }
}
