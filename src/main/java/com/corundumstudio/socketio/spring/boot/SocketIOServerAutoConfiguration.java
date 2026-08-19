package com.corundumstudio.socketio.spring.boot;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.handler.SuccessAuthorizationListener;
import com.corundumstudio.socketio.listener.DefaultExceptionListener;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.corundumstudio.socketio.spring.boot.hooks.SocketIOServerShutdownHook;
import com.corundumstudio.socketio.store.MemoryStoreFactory;
import com.corundumstudio.socketio.store.StoreFactory;
import com.corundumstudio.socketio.store.pubsub.PubSubStore;
import io.netty.channel.epoll.Epoll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for the {@link SocketIOServer}.
 *
 * <p>Activated when {@code socket-io.server.enabled=true}. It wires the
 * authorization listener, exception listener and store factory, registers the
 * Spring annotation scanner used to discover {@code @OnConnect}/{@code @OnDisconnect}
 * handlers and starts/stops the server as part of the application lifecycle.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = SocketIOServerProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ SocketIOServerProperties.class })
@Slf4j
public class SocketIOServerAutoConfiguration implements DisposableBean, CommandLineRunner {

	/**
	 * <p>Creates the default {@link AuthorizationListener} that allows every handshake.</p>
	 *
	 * @return a successful authorization listener
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthorizationListener socketAuthzListener() {
		return new SuccessAuthorizationListener();
	}

	/**
	 * <p>Creates the default {@link ExceptionListener}.</p>
	 *
	 * @return a default exception listener
	 */
	@Bean
	@ConditionalOnMissingBean
	public ExceptionListener exceptionListener() {
		return new DefaultExceptionListener();
	}

	/**
	 * <p>Creates the default in-memory {@link StoreFactory} used to persist session data.</p>
	 *
	 * @return a memory-based store factory
	 */
	@Bean
	@ConditionalOnMissingBean
	public StoreFactory clientStoreFactory() {
		return new MemoryStoreFactory();
	}

	/**
	 * <p>Creates the {@link SocketIOServer} bean from the resolved configuration, listener
	 * and store factory. Native epoll is gracefully downgraded if the library is not
	 * available and {@code failIfNativeEpollLibNotPresent} is {@code false}.</p>
	 *
	 * @param config                 the Socket.IO server configuration
	 * @param authorizationListener  the authorization listener
	 * @param exceptionListener      the exception listener
	 * @param clientStoreFactory     the session store factory
	 * @return the configured, but not yet started, Socket.IO server
	 */
	@Bean(destroyMethod = "stop")
	public SocketIOServer socketIOServer(
			SocketIOServerProperties config,
			AuthorizationListener authorizationListener,
			ExceptionListener exceptionListener,
			StoreFactory clientStoreFactory) {

		config.setAuthorizationListener(authorizationListener);
		config.setExceptionListener(exceptionListener);
		config.setStoreFactory(clientStoreFactory);

		if (config.isUseLinuxNativeEpoll()
				&& !config.isFailIfNativeEpollLibNotPresent()
				&& !Epoll.isAvailable()) {
			log.warn("Epoll library not available, disabling native epoll");
			config.setUseLinuxNativeEpoll(false);
		}

		final SocketIOServer server = new SocketIOServer(config);

		return server;
	}

	/**
	 * <p>Creates the {@link SpringAnnotationScanner} used to register Socket.IO event
	 * handlers annotated with {@code @OnConnect}/{@code @OnDisconnect}/{@code @OnEvent}.</p>
	 *
	 * @param socketServer the Socket.IO server
	 * @return the Spring annotation scanner
	 */
	@Bean
	public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
		return new SpringAnnotationScanner(socketServer);
	}

	/**
	 * <p>Exposes the {@link PubSubStore} used to broadcast events across server nodes.</p>
	 *
	 * @param socketServer the Socket.IO server
	 * @return the pub/sub store of the active store factory
	 */
	@Bean
	public PubSubStore pubSubStore(SocketIOServer socketServer) {
		return socketServer.getConfiguration().getStoreFactory().pubSubStore();
	}

	@Autowired
	protected SocketIOServer socketIOServer;

	/**
	 * <p>Stops the Socket.IO server when the Spring container is destroyed.</p>
	 *
	 * @throws Exception if stopping the server fails
	 */
	@Override
	public void destroy() throws Exception {
		if (socketIOServer != null) {
			socketIOServer.stop();
		}
	}

	/**
	 * <p>Starts the Socket.IO server after the application context is ready and registers
	 * a JVM shutdown hook that releases resources on exit.</p>
	 *
	 * @param args the incoming application arguments
	 * @throws Exception if starting the server fails
	 */
	@Override
	public void run(String... args) throws Exception {
		if (socketIOServer != null) {
			Runtime.getRuntime().addShutdownHook(new SocketIOServerShutdownHook(socketIOServer));
			socketIOServer.start();
		}
	}
}
