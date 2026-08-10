package com.corundumstudio.socketio.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link RedisTemplateStore}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class RedisTemplateStoreTest {

    private RedisTemplate<Object, Object> redisTemplate;
    private BoundHashOperations<Object, Object, Object> hashOps;
    private RedisTemplateStore store;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        hashOps = mock(BoundHashOperations.class);
        UUID sessionId = UUID.randomUUID();
        when(redisTemplate.boundHashOps(anyString())).thenReturn(hashOps);
        store = new RedisTemplateStore(sessionId, redisTemplate);
    }

    @Test
    void setShouldDelegateToHashOps() {
        store.set("key1", "value1");
        verify(hashOps).put("key1", "value1");
    }

    @Test
    void getShouldReturnNullWhenMapIsNull() {
        when(hashOps.entries()).thenReturn(null);
        Object result = store.get("key1");
        assertThat(result).isNull();
    }

    @Test
    void getShouldReturnValueFromMap() {
        Map<Object, Object> entries = new HashMap<>();
        entries.put("key1", "value1");
        when(hashOps.entries()).thenReturn(entries);
        Object result = store.get("key1");
        assertThat(result).isEqualTo("value1");
    }

    @Test
    void getShouldReturnNullWhenKeyAbsent() {
        Map<Object, Object> entries = new HashMap<>();
        when(hashOps.entries()).thenReturn(entries);
        Object result = store.get("missing");
        assertThat(result).isNull();
    }

    @Test
    void hasShouldDelegateToHashOps() {
        when(hashOps.hasKey("key1")).thenReturn(true);
        boolean result = store.has("key1");
        assertThat(result).isTrue();
    }

    @Test
    void hasShouldReturnFalseWhenAbsent() {
        when(hashOps.hasKey("missing")).thenReturn(false);
        boolean result = store.has("missing");
        assertThat(result).isFalse();
    }

    @Test
    void delShouldDelegateToHashOps() {
        store.del("key1");
        verify(hashOps).delete("key1");
    }
}
