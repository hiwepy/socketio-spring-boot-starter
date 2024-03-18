package com.corundumstudio.socketio.store;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.UUID;

public class HazelcastExtStore  implements Store {

    private final IMap<String, Object> map;

    public HazelcastExtStore(UUID sessionId, HazelcastInstance hazelcastInstance) {
        map = hazelcastInstance.getMap(CacheKey.SOCKET_IO_SESSION.getKey(sessionId));
    }

    @Override
    public void set(String key, Object val) {
        map.put(key, val);
    }

    @Override
    public <T> T get(String key) {
        return (T) map.get(key);
    }

    @Override
    public boolean has(String key) {
        return map.containsKey(key);
    }

    @Override
    public void del(String key) {
        map.delete(key);
    }
