package com.corundumstudio.socketio.spring.boot;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.RedisConfig;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.DnsAddressResolverGroupFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.store.RedissonStoreFactory;
import com.corundumstudio.socketio.store.StoreFactory;

import io.netty.channel.EventLoopGroup;

@Configuration
@AutoConfigureBefore( name = {
	"com.corundumstudio.socketio.spring.boot.SocketioServerAutoConfiguration"
})
@ConditionalOnClass(name = {"org.redisson.api.RedissonClient"})
@ConditionalOnProperty(prefix = SocketioRedisProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ SocketioRedisProperties.class })
public class SocketioRedisConfiguration {

	protected static Logger LOG = LoggerFactory.getLogger(SocketioRedisConfiguration.class);
	
	@Bean
	@ConditionalOnMissingBean
	public Codec codec() {
		return new JsonJacksonCodec();
	}
	
	/**
     * AddressResolverGroupFactory switch between default and round robin
     */
	@Bean
	@ConditionalOnMissingBean
	public AddressResolverGroupFactory addressResolverGroupFactory() {
		return new DnsAddressResolverGroupFactory();
	}

	
	@Bean
	public Config redisConfig(SocketioRedisProperties config,
			AddressResolverGroupFactory addressResolverGroupFactory,
			Codec codec, 
			@Autowired(required = false) EventLoopGroup eventLoopGroup) {
		
		RedisConfig redisConfig = new RedisConfig(config.getCluster(),
				config.getMasterSlave(),
				config.getReplicated(),
				config.getSentinel(),
				config.getSingle());
		
		redisConfig.setAddressResolverGroupFactory(addressResolverGroupFactory);
		redisConfig.setCodec(codec);
		redisConfig.setEventLoopGroup(eventLoopGroup);
		redisConfig.setKeepPubSubOrder(config.isKeepPubSubOrder());
		redisConfig.setLockWatchdogTimeout(config.getLockWatchdogTimeout());
		redisConfig.setMaxCleanUpDelay(config.getMaxCleanUpDelay());
		redisConfig.setMinCleanUpDelay(config.getMinCleanUpDelay());
		redisConfig.setNettyThreads(config.getNettyThreads());
		redisConfig.setReferenceEnabled(config.isReferenceEnabled());
		redisConfig.setThreads(config.getThreads());
		redisConfig.setTransportMode(config.getTransportMode());
		redisConfig.setUseScriptCache(config.isUseScriptCache());
		// 根据服务模式检查配置
		switch (config.getServer()) {
			case CLUSTER: {
				redisConfig.useClusterServers();
			};break;
			case MASTERSLAVE: {
				redisConfig.useMasterSlaveServers();
			};break;
			case REPLICATED: {
				redisConfig.useReplicatedServers();
			};break;
			case SENTINEL: {
				redisConfig.useSentinelServers();
			};break;
			default: {
				redisConfig.useSentinelServers();
			};break;
		}
		
		return redisConfig;
	}
	
	
	@Bean
	@ConditionalOnMissingBean
	public RedissonClient redisClient(Config redisConfig) {
		return Redisson.create(redisConfig);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public RedissonClient redisPub(Config redisConfig) {
		return Redisson.create(redisConfig);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public RedissonClient redisSub(Config redisConfig) {
		return Redisson.create(redisConfig);
	}
	
	@Bean
	public StoreFactory clientStoreFactory(Redisson redisClient, Redisson redisPub, Redisson redisSub) {
		return new RedissonStoreFactory(redisClient, redisPub, redisSub);
	}

}
