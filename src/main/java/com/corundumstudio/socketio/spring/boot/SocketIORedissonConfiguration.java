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

/**
 * Auto-configuration that backs the Socket.IO server with Redis through Redisson.
 *
 * <p>Activated when Redisson is on the classpath and
 * {@code socket-io.cache.redisson.enabled=true}. It provisions the Redisson codec,
 * the address resolver group factory, three Redisson clients (one for general
 * storage, one for publishing and one for subscribing) and exposes them through a
 * {@link RedissonExtStoreFactory}.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@AutoConfigureBefore({ SocketIOServerAutoConfiguration.class})
@ConditionalOnClass({ Redisson.class })
@ConditionalOnProperty(prefix = SocketIORedissonProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ SocketIORedissonProperties.class })
public class SocketIORedissonConfiguration {

	/**
	 * Create the default {@link Codec} used to serialize Redisson values (JSON via Jackson).
	 * @return a JSON Jackson codec
	 */
	@Bean
	@ConditionalOnMissingBean
    /**
     * <p>Codec.</p>
     * @return the codec
     */
	public Codec codec() {
		return new JsonJacksonCodec();
	}

	/**
     * AddressResolverGroupFactory switch between default and round robin
     */
	@Bean
	@ConditionalOnMissingBean
    /**
     * <p>Address resolver group factory.</p>
     * @return the address resolver group factory
     */
	public AddressResolverGroupFactory addressResolverGroupFactory() {
		return new DnsAddressResolverGroupFactory();
	}

	/**
	 * Build the Redisson {@link Config} from the bound properties, selecting the
	 * appropriate server configuration (single, sentinel, cluster, master/slave or
	 * replicated) based on the configured {@code server} mode.
	 * @param properties the Redisson properties
	 * @param addressResolverGroupFactory the address resolver group factory
	 * @param codec the codec used to serialize values
	 * @param eventLoopGroup the optional shared Netty event loop group
	 * @return the resolved Redisson configuration
	 */
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

	/**
	 * Create the default Redisson client used for general session storage.
	 * @param redissonConfig the Redisson configuration
	 * @return a new Redisson client
	 */
	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
    /**
     * <p>Redisson client.</p>
     * @param redissonConfig
     * @return the redisson client
     */
	public Redisson redissonClient(Config redissonConfig) {
		return (Redisson) Redisson.create(redissonConfig);
	}

	/**
	 * Create the Redisson client dedicated to publishing pub/sub messages.
	 * @param redissonConfig the Redisson configuration
	 * @return a new Redisson client dedicated to publishing
	 */
	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
    /**
     * <p>Redisson pub.</p>
     * @param redissonConfig
     * @return the redisson pub
     */
	public Redisson redissonPub(Config redissonConfig) {
		return (Redisson) Redisson.create(redissonConfig);
	}

	/**
	 * Create the Redisson client dedicated to subscribing to pub/sub messages.
	 * @param redissonConfig the Redisson configuration
	 * @return a new Redisson client dedicated to subscribing
	 */
	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
    /**
     * <p>Redisson sub.</p>
     * @param redissonConfig
     * @return the redisson sub
     */
	public Redisson redissonSub(Config redissonConfig) {
		return (Redisson) Redisson.create(redissonConfig);
	}

	/**
	 * Create the {@link StoreFactory} backed by the three Redisson clients.
	 * @param redisClient the general-purpose Redisson client
	 * @param redisPub the publishing Redisson client
	 * @param redisSub the subscribing Redisson client
	 * @return a Redisson-backed store factory
	 */
	@Bean
	@ConditionalOnMissingBean
    /**
     * <p>Client store factory.</p>
     * @param redisClient
     * @param redisPub
     * @param redisSub
     * @return the client store factory
     */
	public StoreFactory clientStoreFactory(Redisson redisClient, Redisson redisPub, Redisson redisSub) {
		return new RedissonExtStoreFactory(redisClient, redisPub, redisSub);
	}

}
