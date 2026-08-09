package org.redisson.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RedissonConfig}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class RedissonConfigTest {

    @Test
    void defaultConstructorShouldCreateEmptyConfig() {
        RedissonConfig config = new RedissonConfig();
        assertThat(config).isNotNull();
    }

    @Test
    void copyConstructorShouldCopyExistingConfig() {
        RedissonConfig original = new RedissonConfig();
        original.setThreads(4);
        RedissonConfig copy = new RedissonConfig(original);
        assertThat(copy).isNotNull();
    }

    @Test
    void clusterConstructorShouldConfigureClusterMode() {
        ClusterServersConfig clusterConfig = new ClusterServersConfig();
        RedissonConfig config = new RedissonConfig(clusterConfig);
        assertThat(config).isNotNull();
        assertThat(config.useClusterServers()).isNotNull();
    }

    @Test
    void masterSlaveConstructorShouldConfigureMasterSlaveMode() {
        MasterSlaveServersConfig masterSlaveConfig = new MasterSlaveServersConfig();
        RedissonConfig config = new RedissonConfig(masterSlaveConfig);
        assertThat(config).isNotNull();
        assertThat(config.useMasterSlaveServers()).isNotNull();
    }

    @Test
    void replicatedConstructorShouldConfigureReplicatedMode() {
        ReplicatedServersConfig replicatedConfig = new ReplicatedServersConfig();
        RedissonConfig config = new RedissonConfig(replicatedConfig);
        assertThat(config).isNotNull();
        assertThat(config.useReplicatedServers()).isNotNull();
    }

    @Test
    void sentinelConstructorShouldConfigureSentinelMode() {
        SentinelServersConfig sentinelConfig = new SentinelServersConfig();
        RedissonConfig config = new RedissonConfig(sentinelConfig);
        assertThat(config).isNotNull();
        assertThat(config.useSentinelServers()).isNotNull();
    }

    @Test
    void singleConstructorShouldConfigureSingleMode() {
        SingleServerConfig singleConfig = new SingleServerConfig();
        RedissonConfig config = new RedissonConfig(singleConfig);
        assertThat(config).isNotNull();
        assertThat(config.useSingleServer()).isNotNull();
    }

    @Test
    void allArgsConstructorShouldSetAllConfigs() {
        ClusterServersConfig clusterConfig = new ClusterServersConfig();
        MasterSlaveServersConfig masterSlaveConfig = new MasterSlaveServersConfig();
        ReplicatedServersConfig replicatedConfig = new ReplicatedServersConfig();
        SentinelServersConfig sentinelConfig = new SentinelServersConfig();
        SingleServerConfig singleConfig = new SingleServerConfig();

        RedissonConfig config = new RedissonConfig(
                clusterConfig, masterSlaveConfig, replicatedConfig,
                sentinelConfig, singleConfig);

        assertThat(config).isNotNull();
        assertThat(config.getClusterServersConfig()).isEqualTo(clusterConfig);
        assertThat(config.getMasterSlaveServersConfig()).isEqualTo(masterSlaveConfig);
        assertThat(config.getReplicatedServersConfig()).isEqualTo(replicatedConfig);
        assertThat(config.getSentinelServersConfig()).isEqualTo(sentinelConfig);
        assertThat(config.getSingleServerConfig()).isEqualTo(singleConfig);
    }
}
