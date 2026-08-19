package com.corundumstudio.socketio.store;

import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.UUID;

/**
 * Redisson-backed implementation of a per-session Socket.IO {@link Store}.
 *
 * <p>Each session is stored as a Redisson distributed {@link java.util.Map} keyed by
 * the session id, so that session data is shared across all Socket.IO server nodes
 * connected to the same Redis.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class RedissonExtStore implements Store{

    private final Map<String, Object> map;

    /**
     * Construct a Redisson-backed store for the given session.
     * @param sessionId the Socket.IO client session id
     * @param redisson the Redisson client used to obtain the backing map
     */
    public RedissonExtStore(UUID sessionId, RedissonClient redisson) {
        this.map = redisson.getMap(CacheKey.SOCKET_IO_SESSION.getKey(sessionId));
    }

    /**
     * Set the value associated with the given key.
     * @param key the entry key
     * @param value the entry value
     */
    @Override
    /**
     * <p>Sets the set.</p>
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        map.put(key, value);
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
        map.remove(key);
    }


}
