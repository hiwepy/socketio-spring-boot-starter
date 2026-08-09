package com.corundumstudio.socketio.store;

import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * Enumeration of canonical cache keys used by the Socket.IO session store.
 *
 * <p>Each entry pairs a human-readable description with a function that builds the
 * final Redis key for a given argument, so that session, region and location keys
 * can be constructed in a consistent way.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public enum CacheKey {

    /**
     * Socket
     */
    SOCKET_IO_SESSIONS("Socket会话列表", (p1) -> {
        return CacheKey.getKeyStr(CacheKeyConstant.SOCKET_IO_SESSIONS_KEY);
    }),
    /**
     * Socketinformation
     */
    SOCKET_IO_SESSION("Socket会话信息", (sessionId) -> {
        return CacheKey.getKeyStr(CacheKeyConstant.SOCKET_IO_SESSION_KEY, sessionId);
    }),

    /**
     * IP
     */
    SOCKET_IO_IP_REGION("用户坐标对应的地区编码缓存", (ip)->{
        return getKeyStr(CacheKeyConstant.SOCKET_IO_IP_REGION_KEY, ip);
    }),
    /**
     * IP
     */
    SOCKET_IO_IP_LOCATION("用户坐标对应的地理位置缓存", (ip)->{
        return getKeyStr(CacheKeyConstant.SOCKET_IO_IP_LOCATION_KEY, ip);
    })
	;

	private String desc;
    private Function<Object, String> function;

    CacheKey(String desc, Function<Object, String> function) {
        this.desc = desc;
        this.function = function;
    }

    /**
     * Get the human-readable description of this cache key.
     * @return the description of the key
     */
    public String getDesc() {
		return desc;
	}

    /**
     * 1、retrievekey
     * @return rediskey
     */
    public String getKey() {
        return this.function.apply(null);
    }

    /**
     * 1、retrievekey
     * @param key key
     * @return key rediskey
     */
    public String getKey(Object key) {
        return this.function.apply(key);
    }

    /** Prefix prepended to every Socket.IO Redis key. */
    public static String REDIS_PREFIX = "rds";
    /** Delimiter used to join the parts of a cache key. */
    public final static String DELIMITER = ":";

    /**
     * Build a cache key by joining the Redis prefix and the supplied parts, skipping
     * any {@code null} or blank parts.
     * @param args the parts of the key
     * @return the fully qualified cache key
     */
    public static String getKeyStr(Object... args) {
        StringJoiner tempKey = new StringJoiner(DELIMITER);
        tempKey.add(REDIS_PREFIX);
        for (Object s : args) {
            if (Objects.isNull(s) || !StringUtils.hasText(s.toString())) {
                continue;
            }
            tempKey.add(s.toString());
        }
        return tempKey.toString();
    }

    /**
     * Build a thread-scoped cache key by prefixing the result with the current thread
     * id, useful for per-thread scratch storage.
     * @param prefix the prefix to use (instead of the default Redis prefix)
     * @param args the parts of the key
     * @return the thread-scoped cache key
     */
    public static String getThreadKeyStr(String prefix, Object... args) {

        StringJoiner tempKey = new StringJoiner(DELIMITER);
        tempKey.add(prefix);
        tempKey.add(String.valueOf(Thread.currentThread().getId()));
        for (Object s : args) {
            if (Objects.isNull(s) || !StringUtils.hasText(s.toString())) {
                continue;
            }
            tempKey.add(s.toString());
        }
        return tempKey.toString();
    }

    /** Application entry point.
     * @param args the args
     */
    public static void main(String[] args) {
        System.out.println(getKeyStr(233,""));
    }


}
