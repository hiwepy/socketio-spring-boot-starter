package com.corundumstudio.socketio.spring.boot;

import com.corundumstudio.socketio.store.RedissonExtStoreFactory;
import com.corundumstudio.socketio.store.StoreFactory;
import io.netty.channel.EventLoopGroup;
import org.redisson.Redisson;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.RedissonConfig;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.DnsAddressResolverGroupFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore({ SocketIOServerAutoConfiguration.class})
@ConditionalOnClass({ Redisson.class })
@ConditionalOnProperty(prefix = SocketIORedissonProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ SocketIORedissonProperties.class })
public class SocketIORedissonConfiguration {

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
	public Config redissonConfig(SocketIORedissonProperties properties,
								 AddressResolverGroupFactory addressResolverGroupFactory,
								 Codec codec,
								 @Autowired(required = false) EventLoopGroup eventLoopGroup) {

		RedissonConfig config = new RedissonConfig(properties.getCluster(),
				properties.getMasterSlave(),
				properties.getReplicated(),
				properties.getSentinel(),
				properties.getSingle());

		config.setAddressResolverGroupFactory(addressResolverGroupFactory);
		config.setCodec(codec);
		config.setEventLoopGroup(eventLoopGroup);
		config.setKeepPubSubOrder(properties.isKeepPubSubOrder());
		config.setLockWatchdogTimeout(properties.getLockWatchdogTimeout());
		config.setMaxCleanUpDelay(properties.getMaxCleanUpDelay());
		config.setMinCleanUpDelay(properties.getMinCleanUpDelay());
		config.setNettyThreads(properties.getNettyThreads());
		config.setReferenceEnabled(properties.isReferenceEnabled());
		config.setThreads(properties.getThreads());
		config.setTransportMode(properties.getTransportMode());
		config.setUseScriptCache(properties.isUseScriptCache());
		// 根据服务模式检查配置
		switch (properties.getServer()) {
			case CLUSTER: {
				config.useClusterServers();
			};break;
			case MASTERSLAVE: {
				config.useMasterSlaveServers();
			};break;
			case REPLICATED: {
				config.useReplicatedServers();
			};break;
			case SENTINEL: {
				config.useSentinelServers();
			};break;
			default: {
				config.useSingleServer();
			};break;
		}

		return config;
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	public Redisson redissonClient(Config redissonConfig) {
		return (Redisson) Redisson.create(redissonConfig);
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	public Redisson redissonPub(Config redissonConfig) {
		return (Redisson) Redisson.create(redissonConfig);
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	public Redisson redissonSub(Config redissonConfig) {
		return (Redisson) Redisson.create(redissonConfig);
	}

	@Bean
	public StoreFactory clientStoreFactory(Redisson redisClient, Redisson redisPub, Redisson redisSub) {
		return new RedissonExtStoreFactory(redisClient, redisPub, redisSub);
	}

}
