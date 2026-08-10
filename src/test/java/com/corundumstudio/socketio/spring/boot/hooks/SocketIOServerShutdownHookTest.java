package com.corundumstudio.socketio.spring.boot.hooks;

import com.corundumstudio.socketio.SocketIOServer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link SocketIOServerShutdownHook}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class SocketIOServerShutdownHookTest {

    @Test
    void runShouldStopServer() {
        SocketIOServer server = mock(SocketIOServer.class);
        SocketIOServerShutdownHook hook = new SocketIOServerShutdownHook(server);

        hook.run();

        verify(server).stop();
    }

    @Test
    void runShouldSwallowException() {
        SocketIOServer server = mock(SocketIOServer.class);
        doThrow(new RuntimeException("test")).when(server).stop();
        SocketIOServerShutdownHook hook = new SocketIOServerShutdownHook(server);

        assertThatNoException().isThrownBy(hook::run);
    }

    @Test
    void constructorShouldStoreServer() {
        SocketIOServer server = mock(SocketIOServer.class);
        SocketIOServerShutdownHook hook = new SocketIOServerShutdownHook(server);
        assertThatNoException().isThrownBy(hook::run);
    }
}
