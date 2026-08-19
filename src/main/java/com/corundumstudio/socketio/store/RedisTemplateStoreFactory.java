/*
 * Copyright (c) 2018, hiwepy (https://github.com/easy-4-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.corundumstudio.socketio.store;

import com.corundumstudio.socketio.store.pubsub.BaseStoreFactory;
import com.corundumstudio.socketio.store.pubsub.PubSubStore;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Map;
import java.util.UUID;

/**
 * Factory for {@link RedisTemplateStore} instances that backs the Socket.IO session
 * store with Redis through Spring's {@link RedisTemplate}.
 *
 * <p>Also provides the {@link RedisTemplatePubSubStore} used for cross-node pub/sub
 * messaging and {@link RedisTemplateMap} instances for room/scoped data.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class RedisTemplateStoreFactory extends BaseStoreFactory {

	private final RedisTemplate<Object, Object> redisTemplate;

    private final PubSubStore pubSubStore;

    /**
     * Construct a factory backed by the given Redis template and listener container.
     * @param redisTemplate the Redis template used for session storage
     * @param listenerContainer the listener container used for pub/sub messaging
     */
    public RedisTemplateStoreFactory(RedisTemplate<Object, Object> redisTemplate, RedisMessageListenerContainer listenerContainer) {
        this.redisTemplate = redisTemplate;
        this.pubSubStore = new RedisTemplatePubSubStore(redisTemplate, listenerContainer, getNodeId());
    }

    /**
     * Create a new {@link RedisTemplateStore} for the given session id.
     * @param sessionId the Socket.IO client session id
     * @return a new Redis-backed store
    @Override
    public Store createStore(UUID sessionId) {
        return new RedisTemplateStore(sessionId, redisTemplate);
    }

    /**
     * Get the {@link PubSubStore} used to broadcast events across server nodes.
     * @return the Redis pub/sub store
    @Override
    public PubSubStore pubSubStore() {
        return pubSubStore;
    }

    /**
     * Release any resources held by this factory; the RedisTemplate-based
     * implementation has nothing to release so this is a no-op.
    @Override
    public void shutdown() {

    }

    /**
     * Create a new {@link RedisTemplateMap} bound to the given Redis hash key.
     * @param name the Redis hash key backing the map
     * @param <K> the key type
     * @param <V> the value type
     * @return a new Redis-backed map
     */
    @Override
    public <K, V> Map<K, V> createMap(String name) {
        return new RedisTemplateMap<K, V>(redisTemplate, name);
    }

}
