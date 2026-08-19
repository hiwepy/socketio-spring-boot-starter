package com.corundumstudio.socketio.store;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.UUID;

/**
 * Hazelcast-backed implementation of a per-session Socket.IO {@link Store}.
 *
 * <p>Each session is stored as a distributed {@link IMap} keyed by the session id,
 * allowing session data to be shared across all Socket.IO server nodes that join the
 * Hazelcast cluster.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class HazelcastExtStore  implements Store {

    private final IMap<String, Object> map;

    /**
     * Construct a Hazelcast-backed store for the given session.
     * @param sessionId the Socket.IO client session id
     * @param hazelcastInstance the Hazelcast instance used to back the store
     */
    public HazelcastExtStore(UUID sessionId, HazelcastInstance hazelcastInstance) {
        map = hazelcastInstance.getMap(CacheKey.SOCKET_IO_SESSION.getKey(sessionId));
    }

    /**
     * Set the value associated with the given key.
     * @param key the entry key
     * @param val the entry value
     */
    @Override
    /**
     * <p>Sets the set.</p>
     * @param key
     * @param val
     */
    public void set(String key, Object val) {
        map.put(key, val);
    }

    /**
     * Get the value associated with the given key.
     * @param key the entry key
     * @param <T> the value type
     * @return the value, or {@code null} if absent
     */
    @Override
    public <T> T get(String key) {
        return (T) map.get(key);
    }

    /**
     * Whether the given key is present in the store.
     * @param key the entry key
     * @return {@code true} if the key is present
     */
    @Override
    /**
     * <p>Has.</p>
     * @param key
     * @return the has
     */
    public boolean has(String key) {
        return map.containsKey(key);
    }

    /**
     * Delete the entry associated with the given key.
     * @param key the entry key
     */
    @Override
    /**
     * <p>Del.</p>
     * @param key
     */
    public void del(String key) {
        map.delete(key);
    }

}
