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

@Configuration
@AutoConfigureBefore({ SocketioServerAutoConfiguration.class})
@ConditionalOnClass({HazelcastClient.class})
@ConditionalOnProperty(prefix = SocketioHazelcastProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ SocketioHazelcastProperties.class })
public class SocketioHazelcastConfiguration {

	@Autowired
	private SocketioHazelcastProperties config;

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	public HazelcastInstance hazelcastClient() {
		return HazelcastClient.newHazelcastClient(config);
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	public HazelcastInstance hazelcastPub() {
		return HazelcastClient.newHazelcastClient(config);
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	public HazelcastInstance hazelcastSub() {
		return HazelcastClient.newHazelcastClient(config);
	}

	@Bean
	public StoreFactory clientStoreFactory(HazelcastInstance hazelcastClient, HazelcastInstance hazelcastPub, HazelcastInstance hazelcastSub) {
		return new HazelcastExtStoreFactory( hazelcastClient,  hazelcastPub, hazelcastSub);
	}

}
