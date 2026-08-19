package com.corundumstudio.socketio.spring.boot.hooks;

import com.corundumstudio.socketio.SocketIOServer;

/**
 * JVM shutdown hook that stops the {@link SocketIOServer} when the runtime exits.
 *
 * <p>Registered so that the Socket.IO server releases its network resources and
 * cleanly disconnects all clients on application shutdown.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class SocketIOServerShutdownHook extends Thread {

	private SocketIOServer server;

	/**
	 * Construct a shutdown hook for the given Socket.IO server.
	 * @param server the Socket.IO server to stop on JVM exit
	 */
	public SocketIOServerShutdownHook(SocketIOServer server) {
		this.server = server;
	}

	/**
	 * Stop the Socket.IO server, swallowing any errors so that JVM shutdown is not aborted.
	@Override
	public void run() {
		try {
			server.stop();
		} catch (Exception e) {
		}
	}

}
