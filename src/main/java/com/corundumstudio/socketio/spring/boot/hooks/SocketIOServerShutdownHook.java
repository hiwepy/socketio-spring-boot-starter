package com.corundumstudio.socketio.spring.boot.hooks;

import com.corundumstudio.socketio.SocketIOServer;

public class SocketIOServerShutdownHook extends Thread {

	private SocketIOServer server;

	public SocketIOServerShutdownHook(SocketIOServer server) {
		this.server = server;
	}

	@Override
	public void run() {
		try {
			server.stop();
		} catch (Exception e) {
		}
	}

}
