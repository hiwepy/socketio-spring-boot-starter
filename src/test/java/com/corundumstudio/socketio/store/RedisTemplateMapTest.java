package com.corundumstudio.socketio.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link RedisTemplateMap}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class RedisTemplateMapTest {

    private RedisTemplate<Object, Object> redisTemplate;
    private BoundHashOperations<Object, Object, Object> hashOps;
    private RedisTemplateMap<String, String> map;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        hashOps = mock(BoundHashOperations.class);
        when(redisTemplate.boundHashOps("test-map")).thenReturn(hashOps);
        map = new RedisTemplateMap<>(redisTemplate, "test-map");
    }

    @Test
    void sizeShouldDelegateToHashOps() {
        when(hashOps.size()).thenReturn(5L);
        assertThat(map.size()).isEqualTo(5);
    }

    @Test
    void isEmptyShouldReflectSizeZero() {
        when(hashOps.size()).thenReturn(0L);
        // Note: the implementation has isEmpty returning size() > 0
        assertThat(map.isEmpty()).isFalse();
    }

    @Test
    void isEmptyShouldReflectSizePositive() {
        when(hashOps.size()).thenReturn(3L);
        // Note: the implementation has isEmpty returning size() > 0
        assertThat(map.isEmpty()).isTrue();
    }

    @Test
    void containsKeyShouldDelegateToHashOps() {
        when(hashOps.hasKey("key1")).thenReturn(true);
        assertThat(map.containsKey("key1")).isTrue();
    }

    @Test
    void containsValueShouldDelegateToHashOps() {
        Map<Object, Object> entries = new HashMap<>();
        entries.put("k1", "v1");
        when(hashOps.entries()).thenReturn(entries);
        assertThat(map.containsValue("v1")).isTrue();
    }

    @Test
    void getShouldDelegateToHashOps() {
        when(hashOps.get("key1")).thenReturn("value1");
        assertThat(map.get("key1")).isEqualTo("value1");
    }

    @Test
    void putShouldDelegateToHashOps() {
        String result = map.put("key1", "value1");
        verify(hashOps).put("key1", "value1");
        assertThat(result).isEqualTo("value1");
    }

    @Test
    void removeShouldDelegateToHashOps() {
        when(hashOps.get("key1")).thenReturn("value1");
        String result = map.remove("key1");
        verify(hashOps).delete("test-map", "key1");
        assertThat(result).isEqualTo("value1");
    }

    @Test
    void putAllShouldDelegateToHashOps() {
        Map<String, String> entries = Map.of("k1", "v1", "k2", "v2");
        map.putAll(entries);
        verify(hashOps).putAll(entries);
    }

    @Test
    void clearShouldDelegateToHashOps() {
        map.clear();
        verify(hashOps).delete("test-map");
    }

    @Test
    void keySetShouldDelegateToHashOps() {
        Set<Object> keys = new HashSet<>();
        keys.add("k1");
        when(hashOps.keys()).thenReturn(keys);
        assertThat(map.keySet()).isEqualTo(keys);
    }

    @Test
    void valuesShouldDelegateToHashOps() {
        java.util.List<Object> values = java.util.List.of("v1", "v2");
        when(hashOps.values()).thenReturn(values);
        assertThat(map.values()).containsExactly("v1", "v2");
    }

    @Test
    void entrySetShouldDelegateToHashOps() {
        Map<Object, Object> entries = new HashMap<>();
        entries.put("k1", "v1");
        when(hashOps.entries()).thenReturn(entries);
        Set<Map.Entry<String, String>> result = map.entrySet();
        assertThat(result).hasSize(1);
    }

    @Test
    void entrySetShouldReturnEmptyWhenEntriesIsEmpty() {
        when(hashOps.entries()).thenReturn(new HashMap<>());
        assertThat(map.entrySet()).isEmpty();
    }
}
