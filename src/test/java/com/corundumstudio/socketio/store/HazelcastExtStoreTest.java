package com.corundumstudio.socketio.store;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link HazelcastExtStore}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class HazelcastExtStoreTest {

    private HazelcastInstance hazelcastInstance;
    private IMap<String, Object> map;
    private HazelcastExtStore store;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        hazelcastInstance = mock(HazelcastInstance.class);
        map = mock(IMap.class);
        UUID sessionId = UUID.randomUUID();
        doReturn(map).when(hazelcastInstance).getMap(anyString());
        store = new HazelcastExtStore(sessionId, hazelcastInstance);
    }

    @Test
    void setShouldPutIntoMap() {
        store.set("key1", "value1");
        verify(map).put("key1", "value1");
    }

    @Test
    void getShouldReturnFromMap() {
        when(map.get("key1")).thenReturn("value1");
        Object result = store.get("key1");
        assertThat(result).isEqualTo("value1");
    }

    @Test
    void getShouldReturnNullWhenAbsent() {
        when(map.get("missing")).thenReturn(null);
        Object result = store.get("missing");
        assertThat(result).isNull();
    }

    @Test
    void hasShouldReturnTrueWhenPresent() {
        when(map.containsKey("key1")).thenReturn(true);
        boolean result = store.has("key1");
        assertThat(result).isTrue();
    }

    @Test
    void hasShouldReturnFalseWhenAbsent() {
        when(map.containsKey("missing")).thenReturn(false);
        boolean result = store.has("missing");
        assertThat(result).isFalse();
    }

    @Test
    void delShouldDeleteFromMap() {
        store.del("key1");
        verify(map).delete("key1");
    }
}
