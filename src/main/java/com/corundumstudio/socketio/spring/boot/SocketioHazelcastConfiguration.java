package com.corundumstudio.socketio.spring.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.store.HazelcastStoreFactory;
import com.corundumstudio.socketio.store.StoreFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

@Configuration
@AutoConfigureBefore( name = {
	"com.corundumstudio.socketio.spring.boot.SocketioServerAutoConfiguration"
})
@ConditionalOnClass(name = {"com.hazelcast.client.HazelcastClient"})
@ConditionalOnProperty(prefix = SocketioHazelcastProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ SocketioHazelcastProperties.class })
public class SocketioHazelcastConfiguration {

	protected static Logger LOG = LoggerFactory.getLogger(SocketioHazelcastConfiguration.class);
	@Autowired
	private SocketioHazelcastProperties config;
	
	@Bean
	public HazelcastInstance hazelcastClient() {
		return HazelcastClient.newHazelcastClient(config);
	}
	
	@Bean
	public HazelcastInstance hazelcastPub() {
		return HazelcastClient.newHazelcastClient(config);
	}
	
	@Bean
	public HazelcastInstance hazelcastSub() {
		return HazelcastClient.newHazelcastClient(config);
	}
	
	@Bean
	public StoreFactory clientStoreFactory(HazelcastInstance hazelcastClient, HazelcastInstance hazelcastPub, HazelcastInstance hazelcastSub) {
		return new HazelcastStoreFactory( hazelcastClient,  hazelcastPub, hazelcastSub);
	}

}
