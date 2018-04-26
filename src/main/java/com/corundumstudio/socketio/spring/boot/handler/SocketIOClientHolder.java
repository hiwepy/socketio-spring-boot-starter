/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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
package com.corundumstudio.socketio.spring.boot.handler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.corundumstudio.socketio.SocketIOClient;

public abstract class SocketIOClientHolder {

	private static final ConcurrentHashMap<String, Map<UUID, SocketIOClient>> COMPLIED_CLIENTS = new ConcurrentHashMap<String, Map<UUID, SocketIOClient>>();

	public static Map<UUID, SocketIOClient> getClients(String group) {
		if (group != null) {
			Map<UUID, SocketIOClient> ret = COMPLIED_CLIENTS.get(group);
			if (ret != null) {
				return ret;
			}
			ret = new ConcurrentHashMap<UUID, SocketIOClient>();
			Map<UUID, SocketIOClient> existing = COMPLIED_CLIENTS.putIfAbsent(group, ret);
			if (existing != null) {
				ret = existing;
			}
			
			return ret;
		}
		return null;
	}
	
}
