package com.corundumstudio.socketio.store;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

import java.util.UUID;

public class RedissonExtStoreFactory extends RedissonStoreFactory {

    private final RedissonClient redisClient;
    public RedissonExtStoreFactory(Redisson redisClient, Redisson redisPub, Redisson redisSub) {
        super(redisClient, redisPub, redisSub);
        this.redisClient = redisClient;
    }

    @Override
    public Store createStore(UUID sessionId) {
        return new RedissonExtStore(sessionId, redisClient);
    }


}
