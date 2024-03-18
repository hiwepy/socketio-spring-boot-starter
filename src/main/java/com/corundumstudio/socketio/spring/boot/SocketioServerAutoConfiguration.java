package com.corundumstudio.socketio.spring.boot;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.handler.SuccessAuthorizationListener;
import com.corundumstudio.socketio.listener.DefaultExceptionListener;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.corundumstudio.socketio.spring.boot.hooks.SocketioServerShutdownHook;
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

@Configuration
@ConditionalOnProperty(prefix = SocketioServerProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ SocketioServerProperties.class })
@Slf4j
public class SocketioServerAutoConfiguration implements DisposableBean, CommandLineRunner {

	@Bean
	@ConditionalOnMissingBean
	public AuthorizationListener socketAuthzListener() {
		return new SuccessAuthorizationListener();
	}

	@Bean
	@ConditionalOnMissingBean
	public ExceptionListener exceptionListener() {
		return  new DefaultExceptionListener();
	}

	@Bean
	@ConditionalOnMissingBean
	public StoreFactory clientStoreFactory() {
		return new MemoryStoreFactory();
	}

	@Bean(destroyMethod = "stop")
	public SocketIOServer socketIOServer(
			SocketioServerProperties config,
			AuthorizationListener authorizationListener,
			ExceptionListener exceptionListener,
			StoreFactory clientStoreFactory) {

		// 身份验证
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

	@Bean
	public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
		return new SpringAnnotationScanner(socketServer);
	}

	@Bean
	public PubSubStore pubSubStore(SocketIOServer socketServer) {
		return socketServer.getConfiguration().getStoreFactory().pubSubStore();
	}

	@Autowired
	protected SocketIOServer socketIOServer;

	@Override
	public void destroy() throws Exception {
		if (socketIOServer != null) {
			socketIOServer.stop();
		}
	}

	@Override
	public void run(String... args) throws Exception {
		if (socketIOServer != null) {

			/**
			 * 应用退出时，要调用shutdown来清理资源，关闭网络连接，注销自己
			 * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
			 */
			Runtime.getRuntime().addShutdownHook(new SocketioServerShutdownHook(socketIOServer));

			socketIOServer.start();
		}
	}
}
