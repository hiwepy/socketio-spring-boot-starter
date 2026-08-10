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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link Map} view backed by a Redis hash accessed through Spring's
 * {@link RedisTemplate}.
 *
 * <p>Each instance is bound to a single hash key (the {@code name}) and delegates
 * read/write operations to {@link BoundHashOperations}, allowing Socket.IO room and
 * session data to be persisted in Redis.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class RedisTemplateMap<K, V> implements Map<K, V> {

	private final String name;
	private final BoundHashOperations<Object, Object, Object> hashOperations;

	/**
	 * Construct a map view bound to the given Redis hash key.
	 * @param redisTemplate the Redis template used to access the hash
	 * @param name the Redis hash key backing this map
	 */
	public RedisTemplateMap(RedisTemplate<Object, Object> redisTemplate, String name) {
		this.name = name;
		this.hashOperations = redisTemplate.boundHashOps(name);
	}

	@Override
	public int size() {
		return hashOperations.size().intValue();
	}

	@Override
	/** Returns whether the empty is enabled.
	 * @return the result
	 */
	public boolean isEmpty() {
		return size() > 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return hashOperations.hasKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return hashOperations.entries().containsValue(value);
	}

	@Override
	public V get(Object key) {
		return (V) hashOperations.get(key);
	}

	@Override
	public V put(Object key, Object value) {
		hashOperations.put(key, value);
		return (V) value;
	}

	@Override
	public V remove(Object key) {
		Object value = hashOperations.get(key);
		hashOperations.delete(name, key);
		return (V) value;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		hashOperations.putAll(m);
	}

	@Override
	public void clear() {
		hashOperations.delete(name);
	}

	@Override
	public Set<K> keySet() {
		return (Set<K>) hashOperations.keys();
	}

	@Override
	public Collection<V> values() {
		return (Collection<V>) hashOperations.values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		Set<Entry<Object, Object>> sets = hashOperations.entries().entrySet();
		if(sets != null) {
			return sets.stream().map((m) -> {
				return (Entry<K, V>) m;
			}).collect(Collectors.toSet());
		}
		return null;
	}

}
