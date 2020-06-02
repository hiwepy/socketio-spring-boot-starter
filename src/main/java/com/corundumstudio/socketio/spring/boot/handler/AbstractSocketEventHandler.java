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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;

public abstract class AbstractSocketEventHandler {

	private static Logger LOG = LoggerFactory.getLogger(AbstractSocketEventHandler.class);
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
		LOG.debug("Connect OK.");
		LOG.debug("Session ID  : %s", client.getSessionId());
		LOG.debug("HttpHeaders : %s", client.getHandshakeData().getHttpHeaders());
		LOG.debug("UrlParams   : %s", client.getHandshakeData().getUrlParams());
		
		client.sendEvent("welcome", "ok");
	}
	
	// 添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
	@OnDisconnect
	public void onDisconnect(SocketIOClient client) {
		LOG.debug("Disconnect OK.");
		LOG.debug("Session ID  : %s", client.getSessionId());
	}

	public Collection<SocketIOClient> getClients(String group) {
		return getSocketIOServer().getNamespace(group).getAllClients();
	}

	public SocketIOServer getSocketIOServer() {
		return socketIOServer;
	}

	public void setSocketIOServer(SocketIOServer socketIOServer) {
		this.socketIOServer = socketIOServer;
	}
	
}
