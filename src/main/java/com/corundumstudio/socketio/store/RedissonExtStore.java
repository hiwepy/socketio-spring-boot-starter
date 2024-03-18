package com.corundumstudio.socketio.store;

import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.UUID;

public class RedissonExtStore implements Store{

    private final Map<String, Object> map;

    public RedissonExtStore(UUID sessionId, RedissonClient redisson) {
        this.map = redisson.getMap(CacheKey.SOCKET_IO_SESSION.getKey(sessionId));
    }

    @Override
    public void set(String key, Object value) {
        map.put(key, value);
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
        map.remove(key);
    }


}
