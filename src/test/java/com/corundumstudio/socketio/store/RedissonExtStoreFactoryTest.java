package com.corundumstudio.socketio.store;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link RedissonExtStoreFactory}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class RedissonExtStoreFactoryTest {

    @Test
    void createStoreShouldReturnRedissonExtStore() {
        Redisson redisClient = mock(Redisson.class);
        Redisson redisPub = mock(Redisson.class);
        Redisson redisSub = mock(Redisson.class);

        @SuppressWarnings("unchecked")
        RMap<Object, Object> mockMap = mock(RMap.class);
        doReturn(mockMap).when((RedissonClient) redisClient).getMap(anyString());

        RedissonExtStoreFactory factory = new RedissonExtStoreFactory(redisClient, redisPub, redisSub);
        UUID sessionId = UUID.randomUUID();
        Store store = factory.createStore(sessionId);
        assertThat(store).isInstanceOf(RedissonExtStore.class);
    }
}
