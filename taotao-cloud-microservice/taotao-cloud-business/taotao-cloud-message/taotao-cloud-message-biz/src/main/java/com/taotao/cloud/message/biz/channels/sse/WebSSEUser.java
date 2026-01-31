package com.taotao.cloud.message.biz.channels.sse;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSSEUser
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class WebSSEUser {

    private static Map<String, Chater> userChaterMap = new ConcurrentHashMap<>();

    public static void add( String userName, Chater chater ) {
        userChaterMap.put(userName, chater);
    }

    /**
     * 根据昵称拿Chater
     */
    public static Chater getChater( String userName ) {
        return userChaterMap.get(userName);
    }

    /**
     * 移除失效的Chater
     */
    public static void removeUser( String userName ) {
        userChaterMap.remove(userName);
    }

    public static Set<String> getUserList() {
        return userChaterMap.keySet();
    }
}
