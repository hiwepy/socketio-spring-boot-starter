package com.corundumstudio.socketio.store;

public abstract class CacheKeyConstant {

	/**
	 * 用户会话列表
	 */
	public final static String SOCKET_IO_SESSIONS_KEY = "socket_io:sessions";
	/**
	 * 用户会话信息
	 */
	public final static String SOCKET_IO_SESSION_KEY = "socket_io:session";
	/**
	 * IP坐标缓存
	 */
	public final static String SOCKET_IO_IP_REGION_KEY = "socket_io:ip:region";
	/**
	 * IP坐标缓存
	 */
	public final static String SOCKET_IO_IP_LOCATION_KEY = "socket_io:ip:location";

}
