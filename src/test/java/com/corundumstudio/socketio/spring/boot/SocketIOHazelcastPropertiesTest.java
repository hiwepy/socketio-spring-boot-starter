package com.corundumstudio.socketio.spring.boot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SocketIOHazelcastProperties}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class SocketIOHazelcastPropertiesTest {

    @Test
    void defaultEnabledShouldBeFalse() {
        SocketIOHazelcastProperties props = new SocketIOHazelcastProperties();
        assertThat(props.isEnabled()).isFalse();
    }

    @Test
    void setEnabledShouldStoreValue() {
        SocketIOHazelcastProperties props = new SocketIOHazelcastProperties();
        props.setEnabled(true);
        assertThat(props.isEnabled()).isTrue();
    }

    @Test
    void prefixConstantShouldBeCorrect() {
        assertThat(SocketIOHazelcastProperties.PREFIX).isEqualTo("socket-io.cache.hazelcast");
    }
}
