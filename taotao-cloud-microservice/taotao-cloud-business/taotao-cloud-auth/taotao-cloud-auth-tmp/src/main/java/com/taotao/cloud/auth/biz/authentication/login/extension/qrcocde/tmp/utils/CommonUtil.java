package com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.utils;

import java.util.UUID;

public class CommonUtil {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String buildTicketKey(String uuid) {
        return LoginConstant.PREFIX_TICKET + LoginConstant.SPLIT + uuid;
    }

    public static String buildUserKey(String userId) {
        return LoginConstant.PREFIX_USER + LoginConstant.SPLIT + userId;
    }

    public static String buildAccessTokenKey(String token) {
        return LoginConstant.ACCESS_TOKEN__PREFIX + token;
    }

    public static String buildOnceTokenKey(String token) {
        return LoginConstant.ONCE_TOKEN__PREFIX + token;
    }
}
