package com.corundumstudio.socketio.spring.boot;

import com.corundumstudio.socketio.store.StoreFactory;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.DnsAddressResolverGroupFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SocketIORedissonConfiguration}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class SocketIORedissonConfigurationTest {

    private final SocketIORedissonConfiguration configuration = new SocketIORedissonConfiguration();

    @Test
    void codecShouldReturnJsonJacksonCodec() {
        Codec codec = configuration.codec();
        assertThat(codec).isNotNull();
    }

    @Test
    void addressResolverGroupFactoryShouldReturnDnsFactory() {
        AddressResolverGroupFactory factory = configuration.addressResolverGroupFactory();
        assertThat(factory).isNotNull();
        assertThat(factory).isInstanceOf(DnsAddressResolverGroupFactory.class);
    }

    @Test
    void redissonConfigShouldBuildConfigForSingleMode() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setServer(SocketIORedissonProperties.RedisServerMode.SINGLE);
        AddressResolverGroupFactory resolverFactory = new DnsAddressResolverGroupFactory();
        Codec codec = configuration.codec();

        Config config = configuration.redissonConfig(props, resolverFactory, codec, null);
        assertThat(config).isNotNull();
        assertThat(config.useSingleServer()).isNotNull();
    }

    @Test
    void redissonConfigShouldBuildConfigForClusterMode() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setServer(SocketIORedissonProperties.RedisServerMode.CLUSTER);
        AddressResolverGroupFactory resolverFactory = new DnsAddressResolverGroupFactory();
        Codec codec = configuration.codec();

        Config config = configuration.redissonConfig(props, resolverFactory, codec, null);
        assertThat(config).isNotNull();
        assertThat(config.useClusterServers()).isNotNull();
    }

    @Test
    void redissonConfigShouldBuildConfigForMasterSlaveMode() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setServer(SocketIORedissonProperties.RedisServerMode.MASTERSLAVE);
        AddressResolverGroupFactory resolverFactory = new DnsAddressResolverGroupFactory();
        Codec codec = configuration.codec();

        Config config = configuration.redissonConfig(props, resolverFactory, codec, null);
        assertThat(config).isNotNull();
        assertThat(config.useMasterSlaveServers()).isNotNull();
    }

    @Test
    void redissonConfigShouldBuildConfigForReplicatedMode() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setServer(SocketIORedissonProperties.RedisServerMode.REPLICATED);
        AddressResolverGroupFactory resolverFactory = new DnsAddressResolverGroupFactory();
        Codec codec = configuration.codec();

        Config config = configuration.redissonConfig(props, resolverFactory, codec, null);
        assertThat(config).isNotNull();
        assertThat(config.useReplicatedServers()).isNotNull();
    }

    @Test
    void redissonConfigShouldBuildConfigForSentinelMode() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setServer(SocketIORedissonProperties.RedisServerMode.SENTINEL);
        AddressResolverGroupFactory resolverFactory = new DnsAddressResolverGroupFactory();
        Codec codec = configuration.codec();

        Config config = configuration.redissonConfig(props, resolverFactory, codec, null);
        assertThat(config).isNotNull();
        assertThat(config.useSentinelServers()).isNotNull();
    }

    @Test
    void prefixConstantShouldBeCorrect() {
        assertThat(SocketIORedissonProperties.PREFIX).isEqualTo("socket-io.cache.redisson");
    }
}
