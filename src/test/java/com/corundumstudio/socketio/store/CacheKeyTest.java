package com.corundumstudio.socketio.store;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * Tests for {@link CacheKey}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class CacheKeyTest {

    @Test
    void socketIoSessionsKeyShouldNotRequireArg() {
        String key = CacheKey.SOCKET_IO_SESSIONS.getKey();
        assertThat(key).isEqualTo("rds:socket_io:sessions");
    }

    @Test
    void socketIoSessionKeyShouldIncludeSessionId() {
        String sessionId = "test-session-123";
        String key = CacheKey.SOCKET_IO_SESSION.getKey(sessionId);
        assertThat(key).isEqualTo("rds:socket_io:session:test-session-123");
    }

    @Test
    void socketIoIpRegionKeyShouldIncludeIp() {
        String ip = "192.168.1.1";
        String key = CacheKey.SOCKET_IO_IP_REGION.getKey(ip);
        assertThat(key).isEqualTo("rds:socket_io:ip:region:192.168.1.1");
    }

    @Test
    void socketIoIpLocationKeyShouldIncludeIp() {
        String ip = "10.0.0.1";
        String key = CacheKey.SOCKET_IO_IP_LOCATION.getKey(ip);
        assertThat(key).isEqualTo("rds:socket_io:ip:location:10.0.0.1");
    }

    @Test
    void getKeyStrShouldJoinWithDelimiter() {
        String key = CacheKey.getKeyStr("part1", "part2", "part3");
        assertThat(key).isEqualTo("rds:part1:part2:part3");
    }

    @Test
    void getKeyStrShouldSkipNullParts() {
        String key = CacheKey.getKeyStr("part1", null, "part3");
        assertThat(key).isEqualTo("rds:part1:part3");
    }

    @Test
    void getKeyStrShouldSkipBlankParts() {
        String key = CacheKey.getKeyStr("part1", "", "part3");
        assertThat(key).isEqualTo("rds:part1:part3");
    }

    @Test
    void getKeyStrShouldSkipWhitespaceParts() {
        String key = CacheKey.getKeyStr("part1", "   ", "part3");
        assertThat(key).isEqualTo("rds:part1:part3");
    }

    @Test
    void getKeyStrWithSingleArgShouldWork() {
        String key = CacheKey.getKeyStr("only");
        assertThat(key).isEqualTo("rds:only");
    }

    @Test
    void getThreadKeyStrShouldIncludeThreadId() {
        String key = CacheKey.getThreadKeyStr("prefix", "arg1");
        long threadId = Thread.currentThread().getId();
        assertThat(key).isEqualTo("prefix:" + threadId + ":arg1");
    }

    @Test
    void getThreadKeyStrShouldSkipNullArgs() {
        String key = CacheKey.getThreadKeyStr("prefix", null, "arg2");
        long threadId = Thread.currentThread().getId();
        assertThat(key).isEqualTo("prefix:" + threadId + ":arg2");
    }

    @Test
    void redisPrefixShouldBeRds() {
        assertThat(CacheKey.REDIS_PREFIX).isEqualTo("rds");
    }

    @Test
    void delimiterShouldBeColon() {
        assertThat(CacheKey.DELIMITER).isEqualTo(":");
    }

    @Test
    void getDescShouldReturnDescription() {
        assertThat(CacheKey.SOCKET_IO_SESSIONS.getDesc()).isNotEmpty();
        assertThat(CacheKey.SOCKET_IO_SESSION.getDesc()).isNotEmpty();
        assertThat(CacheKey.SOCKET_IO_IP_REGION.getDesc()).isNotEmpty();
        assertThat(CacheKey.SOCKET_IO_IP_LOCATION.getDesc()).isNotEmpty();
    }

    @Test
    void allEnumValuesShouldBePresent() {
        CacheKey[] values = CacheKey.values();
        assertThat(values).hasSize(4);
    }

    @Test
    void mainMethodShouldNotThrow() {
        assertThatNoException().isThrownBy(() -> CacheKey.main(new String[]{}));
    }

    @Test
    void valueOfShouldReturnCorrectEnum() {
        assertThat(CacheKey.valueOf("SOCKET_IO_SESSIONS")).isEqualTo(CacheKey.SOCKET_IO_SESSIONS);
        assertThat(CacheKey.valueOf("SOCKET_IO_SESSION")).isEqualTo(CacheKey.SOCKET_IO_SESSION);
        assertThat(CacheKey.valueOf("SOCKET_IO_IP_REGION")).isEqualTo(CacheKey.SOCKET_IO_IP_REGION);
        assertThat(CacheKey.valueOf("SOCKET_IO_IP_LOCATION")).isEqualTo(CacheKey.SOCKET_IO_IP_LOCATION);
    }
}
