package com.corundumstudio.socketio.store;

import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

public enum CacheKey {

    /**
     * Socket会话列表
     */
    SOCKET_IO_SESSIONS("Socket会话列表", (p1) -> {
        return CacheKey.getKeyStr(CacheKeyConstant.SOCKET_IO_SESSIONS_KEY);
    }),
    /**
     * Socket会话信息
     */
    SOCKET_IO_SESSION("Socket会话信息", (sessionId) -> {
        return CacheKey.getKeyStr(CacheKeyConstant.SOCKET_IO_SESSION_KEY, sessionId);
    }),

    /**
     * IP地区编码缓存
     */
    SOCKET_IO_IP_REGION("用户坐标对应的地区编码缓存", (ip)->{
        return getKeyStr(CacheKeyConstant.SOCKET_IO_IP_REGION_KEY, ip);
    }),
    /**
     * IP坐标缓存
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

    public String getDesc() {
		return desc;
	}

    /**
     * 1、获取全名称key
     * @return 无参数组合后的redis缓存key
     */
    public String getKey() {
        return this.function.apply(null);
    }

    /**
     * 1、获取全名称key
     * @param key 缓存key的部分值
     * @return key参数组合后的redis缓存key
     */
    public String getKey(Object key) {
        return this.function.apply(key);
    }

    public static String REDIS_PREFIX = "rds";
    public final static String DELIMITER = ":";

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

    public static void main(String[] args) {
        System.out.println(getKeyStr(233,""));
    }


}
