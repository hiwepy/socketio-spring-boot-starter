package com.corundumstudio.socketio.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link RedissonExtStore}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class RedissonExtStoreTest {

    private RedissonClient redissonClient;
    private RMap<String, Object> backingMap;
    private RedissonExtStore store;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        redissonClient = mock(RedissonClient.class);
        backingMap = mock(RMap.class);
        UUID sessionId = UUID.randomUUID();
        doReturn(backingMap).when(redissonClient).getMap(anyString());
        store = new RedissonExtStore(sessionId, redissonClient);
    }

    @Test
    void setShouldPutIntoMap() {
        store.set("key1", "value1");
        verify(backingMap).put("key1", "value1");
    }

    @Test
    void getShouldReturnFromMap() {
        when(backingMap.get("key1")).thenReturn("value1");
        Object result = store.get("key1");
        assertThat(result).isEqualTo("value1");
    }

    @Test
    void getShouldReturnNullWhenAbsent() {
        when(backingMap.get("missing")).thenReturn(null);
        Object result = store.get("missing");
        assertThat(result).isNull();
    }

    @Test
    void hasShouldReturnTrueWhenPresent() {
        when(backingMap.containsKey("key1")).thenReturn(true);
        boolean result = store.has("key1");
        assertThat(result).isTrue();
    }

    @Test
    void hasShouldReturnFalseWhenAbsent() {
        when(backingMap.containsKey("missing")).thenReturn(false);
        boolean result = store.has("missing");
        assertThat(result).isFalse();
    }

    @Test
    void delShouldRemoveFromMap() {
        store.del("key1");
        verify(backingMap).remove("key1");
    }
}
