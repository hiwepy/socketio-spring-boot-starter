package com.corundumstudio.socketio.spring.boot;

import org.junit.jupiter.api.Test;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.ReplicatedServersConfig;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SocketIORedissonProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class SocketIORedissonPropertiesTest {

    @Test
    void defaultsShouldBeCorrect() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        assertThat(props.isEnabled()).isFalse();
        assertThat(props.getThreads()).isEqualTo(16);
        assertThat(props.getNettyThreads()).isEqualTo(32);
        assertThat(props.isReferenceEnabled()).isTrue();
        assertThat(props.getTransportMode()).isEqualTo(TransportMode.NIO);
        assertThat(props.getServer()).isEqualTo(SocketIORedissonProperties.RedisServerMode.SINGLE);
        assertThat(props.getLockWatchdogTimeout()).isEqualTo(30000L);
        assertThat(props.isKeepPubSubOrder()).isTrue();
        assertThat(props.isDecodeInExecutor()).isFalse();
        assertThat(props.isUseScriptCache()).isFalse();
        assertThat(props.getMinCleanUpDelay()).isEqualTo(5);
        assertThat(props.getMaxCleanUpDelay()).isEqualTo(1800);
    }

    @Test
    void setEnabledShouldStoreValue() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setEnabled(true);
        assertThat(props.isEnabled()).isTrue();
    }

    @Test
    void setThreadsShouldStoreValue() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setThreads(8);
        assertThat(props.getThreads()).isEqualTo(8);
    }

    @Test
    void setNettyThreadsShouldStoreValue() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setNettyThreads(16);
        assertThat(props.getNettyThreads()).isEqualTo(16);
    }

    @Test
    void setReferenceEnabledShouldStoreValue() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setReferenceEnabled(false);
        assertThat(props.isReferenceEnabled()).isFalse();
    }

    @Test
    void setTransportModeShouldStoreValue() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setTransportMode(TransportMode.EPOLL);
        assertThat(props.getTransportMode()).isEqualTo(TransportMode.EPOLL);
    }

    @Test
    void setServerShouldStoreValue() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setServer(SocketIORedissonProperties.RedisServerMode.CLUSTER);
        assertThat(props.getServer()).isEqualTo(SocketIORedissonProperties.RedisServerMode.CLUSTER);
    }

    @Test
    void setLockWatchdogTimeoutShouldStoreValue() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setLockWatchdogTimeout(60000L);
        assertThat(props.getLockWatchdogTimeout()).isEqualTo(60000L);
    }

    @Test
    void setKeepPubSubOrderShouldStoreValue() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setKeepPubSubOrder(false);
        assertThat(props.isKeepPubSubOrder()).isFalse();
    }

    @Test
    void setDecodeInExecutorShouldStoreValue() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setDecodeInExecutor(true);
        assertThat(props.isDecodeInExecutor()).isTrue();
    }

    @Test
    void setUseScriptCacheShouldStoreValue() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setUseScriptCache(true);
        assertThat(props.isUseScriptCache()).isTrue();
    }

    @Test
    void setMinCleanUpDelayShouldStoreValue() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setMinCleanUpDelay(10);
        assertThat(props.getMinCleanUpDelay()).isEqualTo(10);
    }

    @Test
    void setMaxCleanUpDelayShouldStoreValue() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        props.setMaxCleanUpDelay(3600);
        assertThat(props.getMaxCleanUpDelay()).isEqualTo(3600);
    }

    @Test
    void sentinelGetterSetterShouldWork() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        SentinelServersConfig config = new SentinelServersConfig();
        props.setSentinel(config);
        assertThat(props.getSentinel()).isSameAs(config);
    }

    @Test
    void masterSlaveGetterSetterShouldWork() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        MasterSlaveServersConfig config = new MasterSlaveServersConfig();
        props.setMasterSlave(config);
        assertThat(props.getMasterSlave()).isSameAs(config);
    }

    @Test
    void singleGetterSetterShouldWork() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        assertThat(props.getSingle()).isNull();
    }

    @Test
    void clusterGetterSetterShouldWork() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        ClusterServersConfig config = new ClusterServersConfig();
        props.setCluster(config);
        assertThat(props.getCluster()).isSameAs(config);
    }

    @Test
    void replicatedGetterSetterShouldWork() {
        SocketIORedissonProperties props = new SocketIORedissonProperties();
        ReplicatedServersConfig config = new ReplicatedServersConfig();
        props.setReplicated(config);
        assertThat(props.getReplicated()).isSameAs(config);
    }

    @Test
    void redisServerModeEnumShouldHaveAllValues() {
        SocketIORedissonProperties.RedisServerMode[] modes = SocketIORedissonProperties.RedisServerMode.values();
        assertThat(modes).hasSize(5);
        assertThat(modes).contains(
                SocketIORedissonProperties.RedisServerMode.CLUSTER,
                SocketIORedissonProperties.RedisServerMode.MASTERSLAVE,
                SocketIORedissonProperties.RedisServerMode.REPLICATED,
                SocketIORedissonProperties.RedisServerMode.SENTINEL,
                SocketIORedissonProperties.RedisServerMode.SINGLE
        );
    }

    @Test
    void prefixConstantShouldBeCorrect() {
        assertThat(SocketIORedissonProperties.PREFIX).isEqualTo("socket-io.cache.redisson");
    }
}
