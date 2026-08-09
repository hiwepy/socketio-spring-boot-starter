package com.corundumstudio.socketio.spring.boot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SocketIOHazelcastConfiguration}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class SocketIOHazelcastConfigurationTest {

    @Test
    void prefixConstantShouldBeCorrect() {
        assertThat(SocketIOHazelcastProperties.PREFIX).isEqualTo("socket-io.cache.hazelcast");
    }
}
