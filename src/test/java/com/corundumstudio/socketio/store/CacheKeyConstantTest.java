package com.corundumstudio.socketio.store;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CacheKeyConstant}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class CacheKeyConstantTest {

    @Test
    void sessionsKeyShouldBeCorrect() {
        assertThat(CacheKeyConstant.SOCKET_IO_SESSIONS_KEY).isEqualTo("socket_io:sessions");
    }

    @Test
    void sessionKeyShouldBeCorrect() {
        assertThat(CacheKeyConstant.SOCKET_IO_SESSION_KEY).isEqualTo("socket_io:session");
    }

    @Test
    void ipRegionKeyShouldBeCorrect() {
        assertThat(CacheKeyConstant.SOCKET_IO_IP_REGION_KEY).isEqualTo("socket_io:ip:region");
    }

    @Test
    void ipLocationKeyShouldBeCorrect() {
        assertThat(CacheKeyConstant.SOCKET_IO_IP_LOCATION_KEY).isEqualTo("socket_io:ip:location");
    }
}
