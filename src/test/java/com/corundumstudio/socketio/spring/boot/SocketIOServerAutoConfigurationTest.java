package com.corundumstudio.socketio.spring.boot;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.listener.DefaultExceptionListener;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.corundumstudio.socketio.store.MemoryStoreFactory;
import com.corundumstudio.socketio.store.StoreFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.DisposableBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * Tests for {@link SocketIOServerAutoConfiguration}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class SocketIOServerAutoConfigurationTest {

    private SocketIOServerAutoConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = new SocketIOServerAutoConfiguration();
    }

    @Test
    void shouldImplementDisposableBean() {
        assertThat(configuration).isInstanceOf(DisposableBean.class);
    }

    @Test
    void shouldImplementCommandLineRunner() {
        assertThat(configuration).isInstanceOf(CommandLineRunner.class);
    }

    @Test
    void socketAuthzListenerShouldReturnAuthorizationListener() {
        AuthorizationListener listener = configuration.socketAuthzListener();
        assertThat(listener).isNotNull();
        assertThat(listener).isInstanceOf(AuthorizationListener.class);
    }

    @Test
    void exceptionListenerShouldReturnDefaultExceptionListener() {
        ExceptionListener listener = configuration.exceptionListener();
        assertThat(listener).isNotNull();
        assertThat(listener).isInstanceOf(DefaultExceptionListener.class);
    }

    @Test
    void clientStoreFactoryShouldReturnMemoryStoreFactory() {
        StoreFactory factory = configuration.clientStoreFactory();
        assertThat(factory).isNotNull();
        assertThat(factory).isInstanceOf(MemoryStoreFactory.class);
    }

    @Test
    void destroyShouldNotThrowWhenSocketIOServerIsNull() {
        assertThatNoException().isThrownBy(() -> configuration.destroy());
    }

    @Test
    void runShouldNotThrowWhenSocketIOServerIsNull() {
        assertThatNoException().isThrownBy(() -> configuration.run());
    }

    @Test
    void prefixConstantShouldBeCorrect() {
        assertThat(SocketIOServerProperties.PREFIX).isEqualTo("socket-io.server");
    }
}
