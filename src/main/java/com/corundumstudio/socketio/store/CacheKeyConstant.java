package com.corundumstudio.socketio.store;

/**
 * String constants for the canonical Socket.IO cache keys.
 *
 * <p>Held separately from {@link CacheKey} so that they can be referenced directly
 * when building keys without going through the enum.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public abstract class CacheKeyConstant {

	/**
	 * user
	 */
	public final static String SOCKET_IO_SESSIONS_KEY = "socket_io:sessions";
	/**
	 * userinformation
	 */
	public final static String SOCKET_IO_SESSION_KEY = "socket_io:session";
	/**
	 * IP
	 */
	public final static String SOCKET_IO_IP_REGION_KEY = "socket_io:ip:region";
	/**
	 * IP
	 */
	public final static String SOCKET_IO_IP_LOCATION_KEY = "socket_io:ip:location";

}
