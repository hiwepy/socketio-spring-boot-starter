package com.corundumstudio.socketio.spring.boot.handler;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link AbstractSocketEventHandler}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class AbstractSocketEventHandlerTest {

    private SocketIOServer server;
    private SocketIONamespace namespace;
    private TestableSocketEventHandler handler;

    @BeforeEach
    void setUp() {
        server = mock(SocketIOServer.class);
        namespace = mock(SocketIONamespace.class);
        when(server.getNamespace(anyString())).thenReturn(namespace);
        handler = new TestableSocketEventHandler(server);
    }

    @Test
    void constructorWithServerShouldStoreServer() {
        assertThat(handler.getSocketIOServer()).isSameAs(server);
    }

    @Test
    void defaultConstructorShouldHaveNullServer() {
        TestableSocketEventHandler h = new TestableSocketEventHandler();
        assertThat(h.getSocketIOServer()).isNull();
    }

    @Test
    void setSocketIOServerShouldStoreServer() {
        TestableSocketEventHandler h = new TestableSocketEventHandler();
        h.setSocketIOServer(server);
        assertThat(h.getSocketIOServer()).isSameAs(server);
    }

    @Test
    void onConnectShouldLogAndSendWelcome() {
        SocketIOClient client = mock(SocketIOClient.class);
        UUID sessionId = UUID.randomUUID();
        when(client.getSessionId()).thenReturn(sessionId);
        HandshakeData handshakeData = mock(HandshakeData.class);
        when(handshakeData.getHttpHeaders()).thenReturn(new io.netty.handler.codec.http.DefaultHttpHeaders());
        when(handshakeData.getUrlParams()).thenReturn(java.util.Collections.emptyMap());
        when(client.getHandshakeData()).thenReturn(handshakeData);

        handler.onConnect(client);

        verify(client).sendEvent("welcome", "ok");
    }

    @Test
    void onDisconnectShouldLog() {
        SocketIOClient client = mock(SocketIOClient.class);
        UUID sessionId = UUID.randomUUID();
        when(client.getSessionId()).thenReturn(sessionId);

        handler.onDisconnect(client);

        verify(client).getSessionId();
    }

    @Test
    void getClientsShouldDelegateToNamespace() {
        Collection<SocketIOClient> clients = List.of(mock(SocketIOClient.class));
        when(namespace.getAllClients()).thenReturn(clients);

        Collection<SocketIOClient> result = handler.getClients("/test");

        assertThat(result).isEqualTo(clients);
        verify(server).getNamespace("/test");
    }

    @Test
    void getClientShouldDelegateToNamespace() {
        UUID sessionId = UUID.randomUUID();
        SocketIOClient client = mock(SocketIOClient.class);
        when(namespace.getClient(sessionId)).thenReturn(client);

        SocketIOClient result = handler.getClient("/test", sessionId);

        assertThat(result).isSameAs(client);
    }

    @Test
    void getBroadcastOperationsShouldDelegateToNamespace() {
        BroadcastOperations ops = mock(BroadcastOperations.class);
        when(namespace.getBroadcastOperations()).thenReturn(ops);

        BroadcastOperations result = handler.getBroadcastOperations("/test");

        assertThat(result).isSameAs(ops);
    }

    @Test
    void getBroadcastOperationsWithRoomShouldDelegateToNamespace() {
        BroadcastOperations ops = mock(BroadcastOperations.class);
        when(namespace.getRoomOperations("room1")).thenReturn(ops);

        BroadcastOperations result = handler.getBroadcastOperations("/test", "room1");

        assertThat(result).isSameAs(ops);
    }

    /**
     * Concrete subclass to make the abstract class testable.
     */
    private static class TestableSocketEventHandler extends AbstractSocketEventHandler {
        TestableSocketEventHandler() {
            super();
        }

        TestableSocketEventHandler(SocketIOServer server) {
            super(server);
        }
    }
}
