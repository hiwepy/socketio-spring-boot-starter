package com.corundumstudio.socketio.spring.boot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SocketIOServerProperties}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class SocketIOServerPropertiesTest {

    @Test
    void defaultFailIfNativeEpollLibNotPresentShouldBeFalse() {
        SocketIOServerProperties props = new SocketIOServerProperties();
        assertThat(props.isFailIfNativeEpollLibNotPresent()).isFalse();
    }

    @Test
    void setFailIfNativeEpollLibNotPresentShouldStoreValue() {
        SocketIOServerProperties props = new SocketIOServerProperties();
        props.setFailIfNativeEpollLibNotPresent(true);
        assertThat(props.isFailIfNativeEpollLibNotPresent()).isTrue();
    }

    @Test
    void prefixConstantShouldBeCorrect() {
        assertThat(SocketIOServerProperties.PREFIX).isEqualTo("socket-io.server");
    }
}
