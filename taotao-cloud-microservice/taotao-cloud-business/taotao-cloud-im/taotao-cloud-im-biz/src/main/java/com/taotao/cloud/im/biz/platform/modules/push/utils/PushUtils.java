/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.im.biz.platform.modules.push.utils;

import com.platform.modules.push.config.PushConfig;
import com.platform.modules.push.dto.PushMsgDto;
import com.platform.modules.push.dto.PushTokenDto;
import com.platform.modules.push.vo.PushAliasVo;
import com.platform.modules.push.vo.PushResultVo;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/** 推送服务 */
@Slf4j
public class PushUtils {

    /** 基础URL */
    private static final String BASE_URL = "https://restapi.getui.com/v2/$appId/";
    /** 鉴权URL */
    private static final String AUTH_URL = BASE_URL + "auth";
    /** 单推alias推送 */
    private static final String PUSH_ALIAS_URL = BASE_URL + "push/single/alias";
    /** 用户别名 */
    private static final String USER_ALIAS = BASE_URL + "user/alias";
    /** 常量 */
    private static final String APP_ID = "$appId";
    /** 超时时间 */
    private static final Integer TIMEOUT = 5000;
    /** TTL 3 * 86400000 (普通账号3天，VIP7天) */
    private static final Long TTL = 259200000L;

    /** 获取鉴权token，获得返回值之后，应存入缓存 */
    public static PushTokenDto createToken(PushConfig pushConfig) {
        String url = formatUrl(pushConfig.getAppId(), AUTH_URL);
        String timestamp = String.valueOf(DateUtil.current());
        String sign = SecureUtil.sha256(pushConfig.getAppKey() + timestamp + pushConfig.getMasterSecret());
        Dict dict = Dict.create().set("sign", sign).set("timestamp", timestamp).set("appkey", pushConfig.getAppKey());
        String jsonStr = HttpUtil.post(url, JSONUtil.toJsonStr(dict), TIMEOUT);
        log.info(jsonStr);
        JSONObject data = JSONUtil.parseObj(jsonStr).getJSONObject("data");
        return JSONUtil.toBean(data, PushTokenDto.class).setAppId(pushConfig.getAppId());
    }

    /** 设置alias */
    public static void setAlias(PushTokenDto tokenDto, PushAliasVo aliasVo) {
        setAlias(tokenDto, Arrays.asList(aliasVo));
    }

    /** 设置alias */
    public static void setAlias(PushTokenDto tokenDto, List<PushAliasVo> list) {
        String url = formatUrl(tokenDto.getAppId(), USER_ALIAS);
        String body = JSONUtil.toJsonStr(Dict.create().set("data_list", list));
        String jsonStr = HttpUtil.createPost(url)
                .body(body)
                .setReadTimeout(TIMEOUT)
                .header("token", tokenDto.getToken())
                .execute()
                .body();
        log.info(jsonStr);
    }

    /** 解除alias */
    public static void delAlias(PushTokenDto tokenDto, PushAliasVo aliasVo) {
        delAlias(tokenDto, Arrays.asList(aliasVo));
    }

    /** 解除alias */
    public static void delAlias(PushTokenDto tokenDto, List<PushAliasVo> list) {
        String url = formatUrl(tokenDto.getAppId(), USER_ALIAS);
        String body = JSONUtil.toJsonStr(Dict.create().set("data_list", list));
        String jsonStr = HttpUtil.createRequest(Method.DELETE, url)
                .body(body)
                .setReadTimeout(TIMEOUT)
                .header("token", tokenDto.getToken())
                .execute()
                .body();
        log.info(jsonStr);
    }

    /**
     * 单推alias
     *
     * @return
     */
    public static PushResultVo pushAlias(PushTokenDto tokenDto, PushMsgDto msgDto, Long userId) {
        String url = formatUrl(tokenDto.getAppId(), PUSH_ALIAS_URL);
        String body = JSONUtil.toJsonStr(initBody(msgDto, userId));
        String jsonStr = HttpUtil.createPost(url)
                .header("token", tokenDto.getToken())
                .body(body)
                .timeout(TIMEOUT)
                .execute()
                .body();
        log.info(jsonStr);
        boolean result = 0 == JSONUtil.parseObj(jsonStr).getInt("code");
        boolean online = jsonStr.contains("online");
        return new PushResultVo().setResult(result).setOnline(online);
    }

    /** 组装推送对象 */
    private static Dict initBody(PushMsgDto msgDto, Long userId) {
        Dict message = Dict.create();
        // 通知
        if (msgDto.getTransmission() == null) {
            message.set(
                    "notification", msgDto.setClick_type(msgDto.getClickType().getCode()));
        }
        // 透传
        else {
            message.set("transmission", JSONUtil.toJsonStr(msgDto.getTransmission()));
        }
        Dict dict = Dict.create()
                .set("request_id", IdUtil.objectId())
                .set("settings", Dict.create().set("ttl", TTL))
                .set("audience", Dict.create().set("alias", Arrays.asList(userId)))
                .set("push_message", message);
        return dict;
    }

    /** 格式化url */
    private static String formatUrl(String appId, String url) {
        return url.replace(APP_ID, appId);
    }
}
