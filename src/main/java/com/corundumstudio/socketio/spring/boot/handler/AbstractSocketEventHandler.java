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
package com.corundumstudio.socketio.spring.boot.handler;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.UUID;

@Slf4j
public abstract class AbstractSocketEventHandler {

	private SocketIOServer socketIOServer;

	public AbstractSocketEventHandler() {
	}

	public AbstractSocketEventHandler(SocketIOServer socketIOServer) {
		this.socketIOServer = socketIOServer;
	}

	// 添加connect事件，当客户端发起连接时调用，本文中将clientid与sessionid存入数据库
	// 方便后面发送消息时查找到对应的目标client,
	@OnConnect
	public void onConnect(SocketIOClient client) {
		log.debug("Connect OK.");
		log.debug("Session ID  : %s", client.getSessionId());
		log.debug("HttpHeaders : %s", client.getHandshakeData().getHttpHeaders());
		log.debug("UrlParams   : %s", client.getHandshakeData().getUrlParams());

		client.sendEvent("welcome", "ok");
	}

	// 添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
	@OnDisconnect
	public void onDisconnect(SocketIOClient client) {
		log.debug("Disconnect OK.");
		log.debug("Session ID  : %s", client.getSessionId());
	}

	public Collection<SocketIOClient> getClients(String namespace) {
		return getSocketIOServer().getNamespace(namespace).getAllClients();
	}

	public SocketIOClient getClient(String namespace, UUID sessionId) {
		return getSocketIOServer().getNamespace(namespace).getClient(sessionId);
	}

	public BroadcastOperations getBroadcastOperations(String namespace) {
		return getSocketIOServer().getNamespace(namespace).getBroadcastOperations();
	}

	public BroadcastOperations getBroadcastOperations(String namespace, String room) {
		return getSocketIOServer().getNamespace(namespace).getRoomOperations(room);
	}

	public SocketIOServer getSocketIOServer() {
		return socketIOServer;
	}

	public void setSocketIOServer(SocketIOServer socketIOServer) {
		this.socketIOServer = socketIOServer;
	}

}
