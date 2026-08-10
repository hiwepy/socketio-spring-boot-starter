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
 * Tests for {@link HazelcastExtStoreFactory}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class HazelcastExtStoreFactoryTest {

    private HazelcastInstance hazelcastClient;
    private HazelcastInstance hazelcastPub;
    private HazelcastInstance hazelcastSub;
    private HazelcastExtStoreFactory factory;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        hazelcastClient = mock(HazelcastInstance.class);
        hazelcastPub = mock(HazelcastInstance.class);
        hazelcastSub = mock(HazelcastInstance.class);
        IMap<String, Object> mockMap = mock(IMap.class);
        doReturn(mockMap).when(hazelcastClient).getMap(anyString());
        factory = new HazelcastExtStoreFactory(hazelcastClient, hazelcastPub, hazelcastSub);
    }

    @Test
    void createStoreShouldReturnHazelcastExtStore() {
        UUID sessionId = UUID.randomUUID();
        Store store = factory.createStore(sessionId);
        assertThat(store).isInstanceOf(HazelcastExtStore.class);
    }
}
