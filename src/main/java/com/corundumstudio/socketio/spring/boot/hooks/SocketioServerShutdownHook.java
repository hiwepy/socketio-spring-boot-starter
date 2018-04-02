package com.corundumstudio.socketio.spring.boot.hooks;

import com.corundumstudio.socketio.SocketIOServer;

public class SocketioServerShutdownHook extends Thread {
	
	private SocketIOServer server;
	
	public SocketioServerShutdownHook(SocketIOServer server) {
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
