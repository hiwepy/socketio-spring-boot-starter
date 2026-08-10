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

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.UUID;

/**
 * Redis-backed implementation of a per-session Socket.IO {@link Store} that uses
 * Spring's {@link RedisTemplate} to access the underlying hash.
 *
 * <p>Each session is stored as a Redis hash keyed by the session id, so that session
 * data is shared across all Socket.IO server nodes connected to the same Redis.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class RedisTemplateStore implements Store {

    private final BoundHashOperations<Object, Object, Object> hashOperations;

    /**
     * Construct a Redis-backed store for the given session.
     * @param sessionId the Socket.IO client session id
     * @param redisTemplate the Redis template used to access the session hash
     */
    public RedisTemplateStore(UUID sessionId, RedisTemplate<Object, Object> redisTemplate) {
    	this.hashOperations = redisTemplate.boundHashOps(CacheKey.SOCKET_IO_SESSION.getKey(sessionId));
    }

    /**
     * Set the value associated with the given key.
     * @param key the entry key
     * @param value the entry value
     */
    @Override
    public void set(String key, Object value) {
    	hashOperations.put(key, value);
    }

	/**
	 * Get the value associated with the given key.
	 * @param key the entry key
	 * @param <T> the value type
	 * @return the value, or {@code null} if absent
	 */
	@Override
    public <T> T get(String key) {
		Map<Object, Object> map = hashOperations.entries();
		if(map == null) {
			return null;
		}
        return (T) map.get(key);
    }

    /**
     * Whether the given key is present in the store.
     * @param key the entry key
     * @return {@code true} if the key is present
     */
    @Override
    public boolean has(String key) {
        return hashOperations.hasKey(key);
    }

    /**
     * Delete the entry associated with the given key.
     * @param key the entry key
     */
    @Override
    public void del(String key) {
    	hashOperations.delete(key);
    }

}
