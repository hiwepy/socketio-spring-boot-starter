package com.corundumstudio.socketio.store;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

import java.util.UUID;

/**
 * Factory for {@link RedissonExtStore} instances used to back the Socket.IO session
 * store with Redis through Redisson.
 *
 * <p>Extends {@link RedissonStoreFactory} so that pub/sub is handled by Redisson's
 * topics while session data is held in {@link RedissonExtStore}.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class RedissonExtStoreFactory extends RedissonStoreFactory {

    private final RedissonClient redisClient;

    /**
     * Construct a factory backed by the given Redisson clients.
     * @param redisClient the Redisson client used for session storage
     * @param redisPub the Redisson client used for publishing
     * @param redisSub the Redisson client used for subscribing
     */
    public RedissonExtStoreFactory(Redisson redisClient, Redisson redisPub, Redisson redisSub) {
        super(redisClient, redisPub, redisSub);
        this.redisClient = redisClient;
    }

    /**
     * Create a new {@link RedissonExtStore} for the given session id.
     * @param sessionId the Socket.IO client session id
     * @return a new Redisson-backed store
     */
    @Override
    public Store createStore(UUID sessionId) {
        return new RedissonExtStore(sessionId, redisClient);
    }


}
