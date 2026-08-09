package com.corundumstudio.socketio.store;

import com.hazelcast.core.HazelcastInstance;

import java.util.UUID;

/**
 * Factory for {@link HazelcastExtStore} instances, used to back the Socket.IO session
 * store with a Hazelcast cluster.
 *
 * <p>Extends {@link HazelcastStoreFactory} so that the pub/sub side is handled by the
 * Hazelcast topics while session data is held in {@link HazelcastExtStore}.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public class HazelcastExtStoreFactory extends HazelcastStoreFactory {

    private final HazelcastInstance hazelcastClient;

    /**
     * Construct a factory backed by the given Hazelcast clients.
     * @param hazelcastClient the Hazelcast instance used for session storage
     * @param hazelcastPub the Hazelcast instance used for publishing
     * @param hazelcastSub the Hazelcast instance used for subscribing
     */
    public HazelcastExtStoreFactory(HazelcastInstance hazelcastClient, HazelcastInstance hazelcastPub, HazelcastInstance hazelcastSub) {
        super(hazelcastClient, hazelcastPub, hazelcastSub);
        this.hazelcastClient = hazelcastClient;
    }

    /**
     * Create a new {@link HazelcastExtStore} for the given session id.
     * @param sessionId the Socket.IO client session id
     * @return a new Hazelcast-backed store
     */
    @Override
    public Store createStore(UUID sessionId) {
        return new HazelcastExtStore(sessionId, hazelcastClient);
    }

}
