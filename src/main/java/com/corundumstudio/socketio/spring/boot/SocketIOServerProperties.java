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

import com.corundumstudio.socketio.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Netty Socket.IO server.
 *
 * <p>Binds properties under the {@code socket-io.server} prefix and extends the
 * underlying {@link Configuration} with a flag controlling how strictly the native
 * epoll library availability is enforced on Linux.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(SocketIOServerProperties.PREFIX)
public class SocketIOServerProperties extends Configuration {

	/** Configuration property prefix shared by all Socket.IO server properties. */
	public static final String PREFIX = "socket-io.server";

	/**
	 * If set to true, then useLinuxNativeEpoll property is passed to SocketIO server as is.
	 * If set to false and useLinuxNativeEpoll set to true,
	 * then additional check is performed if epoll library is available on classpath.
	 */
	private boolean failIfNativeEpollLibNotPresent = false;

	/**
	 * Get whether the absence of the native epoll library should fail startup.
	 * @return {@code true} to fail if epoll is not present, otherwise {@code false}
	 */
	public boolean isFailIfNativeEpollLibNotPresent() {
		return failIfNativeEpollLibNotPresent;
	}

	/**
	 * Set whether the absence of the native epoll library should fail startup.
	 * @param failIfNativeEpollLibNotPresent {@code true} to fail if epoll is not present, otherwise {@code false}
	 */
	public void setFailIfNativeEpollLibNotPresent(boolean failIfNativeEpollLibNotPresent) {
		this.failIfNativeEpollLibNotPresent = failIfNativeEpollLibNotPresent;
	}
}
