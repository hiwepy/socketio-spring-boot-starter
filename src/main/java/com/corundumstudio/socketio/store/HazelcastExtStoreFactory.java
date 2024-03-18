package com.corundumstudio.socketio.store;

import com.hazelcast.core.HazelcastInstance;

import java.util.UUID;

public class HazelcastExtStoreFactory extends HazelcastStoreFactory {

    private final HazelcastInstance hazelcastClient;

    public HazelcastExtStoreFactory(HazelcastInstance hazelcastClient, HazelcastInstance hazelcastPub, HazelcastInstance hazelcastSub) {
        super(hazelcastClient, hazelcastPub, hazelcastSub);
        this.hazelcastClient = hazelcastClient;
    }

    @Override
    public Store createStore(UUID sessionId) {
        return new HazelcastExtStore(sessionId, hazelcastClient);
    }

}
