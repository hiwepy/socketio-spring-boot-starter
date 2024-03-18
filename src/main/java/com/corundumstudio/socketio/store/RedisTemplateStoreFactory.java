/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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

public class RedisTemplateStoreFactory extends BaseStoreFactory {

	private final RedisTemplate<Object, Object> redisTemplate;

    private final PubSubStore pubSubStore;

    public RedisTemplateStoreFactory(RedisTemplate<Object, Object> redisTemplate, RedisMessageListenerContainer listenerContainer) {
        this.redisTemplate = redisTemplate;
        this.pubSubStore = new RedisTemplatePubSubStore(redisTemplate, listenerContainer, getNodeId());
    }

    @Override
    public Store createStore(UUID sessionId) {
        return new RedisTemplateStore(sessionId, redisTemplate);
    }

    @Override
    public PubSubStore pubSubStore() {
        return pubSubStore;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public <K, V> Map<K, V> createMap(String name) {
        return new RedisTemplateMap<K, V>(redisTemplate, name);
    }

}
