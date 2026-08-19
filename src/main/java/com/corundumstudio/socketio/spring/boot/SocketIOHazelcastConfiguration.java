package com.corundumstudio.socketio.spring.boot;

import com.corundumstudio.socketio.store.HazelcastExtStoreFactory;
import com.corundumstudio.socketio.store.StoreFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration that backs the Socket.IO server with a Hazelcast cluster.
 *
 * <p>Activated when the Hazelcast client is on the classpath and
 * {@code socket-io.cache.hazelcast.enabled=true}. It provisions three Hazelcast
 * client instances (one for general storage, one for publishing and one for
 * subscribing) and exposes them through a {@link HazelcastExtStoreFactory}.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@AutoConfigureBefore({ SocketIOServerAutoConfiguration.class})
@ConditionalOnClass({HazelcastClient.class})
@ConditionalOnProperty(prefix = SocketIOHazelcastProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ SocketIOHazelcastProperties.class })
public class SocketIOHazelcastConfiguration {

	@Autowired
	private SocketIOHazelcastProperties config;

	/**
	 * Create the default Hazelcast client used for general session storage.
	 * @return a new Hazelcast client
	 */
	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
    /**
     * <p>Hazelcast client.</p>
     * @return the hazelcast client
     */
	public HazelcastInstance hazelcastClient() {
		return HazelcastClient.newHazelcastClient(config);
	}

	/**
	 * Create the Hazelcast client used for publishing pub/sub messages.
	 * @return a new Hazelcast client dedicated to publishing
	 */
	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
    /**
     * <p>Hazelcast pub.</p>
     * @return the hazelcast pub
     */
	public HazelcastInstance hazelcastPub() {
		return HazelcastClient.newHazelcastClient(config);
	}

	/**
	 * Create the Hazelcast client used for subscribing to pub/sub messages.
	 * @return a new Hazelcast client dedicated to subscribing
	 */
	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
    /**
     * <p>Hazelcast sub.</p>
     * @return the hazelcast sub
     */
	public HazelcastInstance hazelcastSub() {
		return HazelcastClient.newHazelcastClient(config);
	}

	/**
	 * Create the {@link StoreFactory} backed by the three Hazelcast clients.
	 * @param hazelcastClient the general-purpose Hazelcast client
	 * @param hazelcastPub the publishing Hazelcast client
	 * @param hazelcastSub the subscribing Hazelcast client
	 * @return a Hazelcast-backed store factory
	 */
	@Bean
	@ConditionalOnMissingBean
    /**
     * <p>Client store factory.</p>
     * @param hazelcastClient
     * @param hazelcastPub
     * @param hazelcastSub
     * @return the client store factory
     */
	public StoreFactory clientStoreFactory(HazelcastInstance hazelcastClient, HazelcastInstance hazelcastPub, HazelcastInstance hazelcastSub) {
		return new HazelcastExtStoreFactory( hazelcastClient,  hazelcastPub, hazelcastSub);
	}

}
