/*
 * Copyright (c) 2010-2020, hiwepy (https://github.com/easy-4-java).
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
package com.corundumstudio.socketio.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the {@code RedisTemplate}-backed Socket.IO session store.
 *
 * <p>Binds properties under the {@code socket-io.cache.redis-template} prefix and
 * exposes the {@code enabled} flag used to activate the
 * {@link SocketIORedisTemplateConfiguration RedisTemplate} store.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(SocketIORedisTemplateProperties.PREFIX)
public class SocketIORedisTemplateProperties {

	/** Configuration property prefix shared by all RedisTemplate store properties. */
	public static final String PREFIX = "socket-io.cache.redis-template";

	/**
	 * Enable SocketIO Redis Store With RedisTemplate.
	 */
	private boolean enabled = false;

	/**
	 * Get whether the RedisTemplate-backed Socket.IO session store is enabled.
	 * @return {@code true} if the RedisTemplate store is enabled, otherwise {@code false}
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Set whether the RedisTemplate-backed Socket.IO session store is enabled.
	 * @param enabled whether to enable the RedisTemplate store
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
