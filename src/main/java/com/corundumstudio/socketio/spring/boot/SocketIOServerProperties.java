/*
 * Copyright (c) 2010-2020, hiwepy (https://github.com/hiwepy).
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

@ConfigurationProperties(SocketIOServerProperties.PREFIX)
public class SocketIOServerProperties extends Configuration {

	public static final String PREFIX = "socket-io.server";

	/**
	 * If set to true, then useLinuxNativeEpoll property is passed to SocketIO server as is.
	 * If set to false and useLinuxNativeEpoll set to true,
	 * then additional check is performed if epoll library is available on classpath.
	 */
	private boolean failIfNativeEpollLibNotPresent = false;

	public boolean isFailIfNativeEpollLibNotPresent() {
		return failIfNativeEpollLibNotPresent;
	}

	public void setFailIfNativeEpollLibNotPresent(boolean failIfNativeEpollLibNotPresent) {
		this.failIfNativeEpollLibNotPresent = failIfNativeEpollLibNotPresent;
	}
}
