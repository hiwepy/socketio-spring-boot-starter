package com.corundumstudio.socketio.spring.boot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SocketIOHazelcastConfiguration}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class SocketIOHazelcastConfigurationTest {

    @Test
    void prefixConstantShouldBeCorrect() {
        assertThat(SocketIOHazelcastProperties.PREFIX).isEqualTo("socket-io.cache.hazelcast");
    }
}
