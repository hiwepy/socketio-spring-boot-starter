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
package com.corundumstudio.socketio.spring.boot.handler;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.UUID;

/**
 * Base class for Socket.IO event handlers, providing convenient access to the
 * {@link SocketIOServer} and default {@link OnConnect}/{@link OnDisconnect} hooks.
 *
 * <p>Subclasses typically extend this class, override the connect/disconnect callbacks
 * and add their own {@code @OnEvent} methods to handle domain-specific events.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractSocketEventHandler {

	private SocketIOServer socketIOServer;

	public AbstractSocketEventHandler() {
	}

	/**
	 * Construct a handler bound to the given {@link SocketIOServer}.
	 * @param socketIOServer the Socket.IO server instance
	 */
	public AbstractSocketEventHandler(SocketIOServer socketIOServer) {
		this.socketIOServer = socketIOServer;
	}

	/**
	 * Invoked when a client connects to the server.
	 *
	 * <p>The default implementation logs the session id, HTTP headers and URL
	 * parameters of the handshake and sends a {@code "welcome"} event to the client.</p>
	 *
	 * @param client the newly connected client
	 */
	@OnConnect
	public void onConnect(SocketIOClient client) {
		log.debug("Connect OK.");
		log.debug("Session ID  : {}", client.getSessionId());
		log.debug("HttpHeaders : {}", client.getHandshakeData().getHttpHeaders());
		log.debug("UrlParams   : {}", client.getHandshakeData().getUrlParams());

		client.sendEvent("welcome", "ok");
	}

	/**
	 * Invoked when a client disconnects from the server.
	 *
	 * <p>The default implementation logs the disconnect and the session id.</p>
	 *
	 * @param client the disconnecting client
	 */
	@OnDisconnect
	public void onDisconnect(SocketIOClient client) {
		log.debug("Disconnect OK.");
		log.debug("Session ID  : {}", client.getSessionId());
	}

	/**
	 * Get all clients currently connected to the given namespace.
	 * @param namespace the namespace to query
	 * @return the collection of connected clients
	 */
	public Collection<SocketIOClient> getClients(String namespace) {
		return getSocketIOServer().getNamespace(namespace).getAllClients();
	}

	/**
	 * Get the client for the given namespace and session id, if any.
	 * @param namespace the namespace to query
	 * @param sessionId the session id of the client
	 * @return the matching client, or {@code null} if not found
	 */
	public SocketIOClient getClient(String namespace, UUID sessionId) {
		return getSocketIOServer().getNamespace(namespace).getClient(sessionId);
	}

	/**
	 * Get the broadcast operations for the given namespace (i.e. all clients in the namespace).
	 * @param namespace the namespace to broadcast to
	 * @return the broadcast operations
	 */
	public BroadcastOperations getBroadcastOperations(String namespace) {
		return getSocketIOServer().getNamespace(namespace).getBroadcastOperations();
	}

	/**
	 * Get the broadcast operations for the given room within the namespace.
	 * @param namespace the namespace to broadcast to
	 * @param room the room name within the namespace
	 * @return the broadcast operations for the room
	 */
	public BroadcastOperations getBroadcastOperations(String namespace, String room) {
		return getSocketIOServer().getNamespace(namespace).getRoomOperations(room);
	}

	/**
	 * Get the underlying {@link SocketIOServer}.
	 * @return the Socket.IO server
	 */
	public SocketIOServer getSocketIOServer() {
		return socketIOServer;
	}

	/**
	 * Set the underlying {@link SocketIOServer}.
	 * @param socketIOServer the Socket.IO server
	 */
	public void setSocketIOServer(SocketIOServer socketIOServer) {
		this.socketIOServer = socketIOServer;
	}

}
