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

package com.taotao.cloud.workflow.biz.common.util;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.workflow.biz.common.base.ActionResultCode;
import com.taotao.cloud.workflow.biz.common.base.UserInfo;
import com.taotao.cloud.workflow.biz.common.model.OnlineUserModel;
import com.taotao.cloud.workflow.biz.common.model.OnlineUserProvider;
import com.taotao.cloud.workflow.biz.common.util.jwt.JwtUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserProvider {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CacheKeyUtil cacheKeyUtil;

    /** 获取token */
    public static String getToken() {
        return getAuthorize();
    }

    /**
     * 获取
     *
     * @param token
     * @return
     */
    public UserInfo get(String token) {
        UserInfo userInfo = new UserInfo();
        token = JwtUtil.getRealToken(token);
        String tokens = null;
        if (token != null) {
            tokens = token;
        } else {
            tokens = UserProvider.getToken();
            tokens = JwtUtil.getRealToken(tokens);
        }
        if (tokens != null) {
            userInfo = JsonUtil.getJsonToBean(String.valueOf(redisUtil.getString(tokens)), UserInfo.class);
        }
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    /**
     * 获取
     *
     * @return
     */
    public UserInfo get() {
        String token = UserProvider.getToken();
        return this.get(token);
    }

    /**
     * 通过上下文的租户查redis来获取token
     *
     * @return
     */
    public UserInfo get(String userId, String tenantId) {
        UserInfo userInfo;
        tenantId = "null".equals(String.valueOf(tenantId)) ? "" : tenantId;
        String token = tenantId + "login_online_" + userId;
        if (ServletUtil.getIsMobileDevice()) {
            token = tenantId + "login_online_mobile_" + userId;
        }
        String onlineInfo = String.valueOf(redisUtil.getString(token));
        userInfo = JsonUtil.getJsonToBean(String.valueOf(redisUtil.getString(onlineInfo)), UserInfo.class);
        return userInfo == null ? new UserInfo() : userInfo;
    }

    /** 获取Authorize */
    public static String getAuthorize() {
        return ServletUtil.getHeader("Authorization");
    }

    /**
     * 创建
     *
     * @param userInfo
     * @return
     */
    public void add(UserInfo userInfo) {
        String userId = userInfo.getUserId();
        long time = DateUtil.getTime(userInfo.getOverdueTime()) - DateUtil.getTime(new Date());

        String authorize = String.valueOf(redisUtil.getString(cacheKeyUtil.getUserAuthorize() + userId));
        String loginOnlineKey = cacheKeyUtil.getLoginOnline() + userId;
        redisUtil.remove(authorize);
        // 记录Token
        redisUtil.insert(userInfo.getId(), userInfo, time);
        // 记录在线
        if (ServletUtil.getIsMobileDevice()) {
            redisUtil.insert(cacheKeyUtil.getMobileLoginOnline() + userId, userInfo.getId(), time);
            // 记录移动设备CID,用于消息推送
            if (ServletUtil.getHeader("clientId") != null) {
                String clientId = ServletUtil.getHeader("clientId");
                Map<String, String> map = new HashMap<>(16);
                map.put(userInfo.getUserId(), clientId);
                redisUtil.insert(cacheKeyUtil.getMobileDeviceList(), map);
            }
        } else {
            redisUtil.insert(loginOnlineKey, userInfo.getId(), time);
        }
    }

    /** 移除在线 */
    public void removeWebSocket(UserInfo userInfo) {
        // 清除websocket登录状态
        boolean isMobileDevice = ServletUtil.getIsMobileDevice();
        String userId = String.valueOf(userInfo.getUserId());
        String tenandId = "null".equals(String.valueOf(userInfo.getTenantId())) ? "" : userInfo.getTenantId();
        OnlineUserModel user = OnlineUserProvider.getOnlineUserList().stream()
                .filter(t -> userId.equals(t.getUserId())
                        && tenandId.equals(t.getTenantId())
                        && isMobileDevice == t.getIsMobileDevice())
                .findFirst()
                .orElse(null);
        if (user != null) {
            JSONObject object = new JSONObject();
            object.put("method", "logout");
            object.put("msg", ActionResultCode.SessionOffLine.getMessage());
            user.getWebSocket().getAsyncRemote().sendText(object.toJSONString());
        }
    }

    /** 移除 */
    public void remove() {
        UserInfo userInfo = this.get();
        String userId = userInfo.getUserId();

        if (ServletUtil.getIsMobileDevice()) {
            redisUtil.removeHash(cacheKeyUtil.getMobileDeviceList(), userId);
        }
        if (userInfo.getId() != null) {
            redisUtil.remove(userInfo.getId());
        }
        redisUtil.remove(cacheKeyUtil.getUserAuthorize() + userId);
        redisUtil.remove(cacheKeyUtil.getLoginOnline() + userId);
        redisUtil.remove(cacheKeyUtil.getSystemInfo());
    }

    /** 移除当前登陆人的token */
    public void removeCurrent() {
        UserInfo userInfo = this.get();
        String userId = userInfo.getUserId();
        if (ServletUtil.getIsMobileDevice()) {
            String key =
                    String.valueOf(redisUtil.getString(cacheKeyUtil.getMobileLoginOnline() + userInfo.getUserId()));
            redisUtil.remove(key);
            redisUtil.remove(cacheKeyUtil.getMobileLoginOnline() + userInfo.getUserId());
        } else {
            String key = String.valueOf(redisUtil.getString(cacheKeyUtil.getLoginOnline() + userInfo.getUserId()));
            redisUtil.remove(key);
            redisUtil.remove(cacheKeyUtil.getLoginOnline() + userInfo.getUserId());
        }
        redisUtil.remove(cacheKeyUtil.getUserAuthorize() + userId);
        redisUtil.remove(cacheKeyUtil.getSystemInfo());
    }

    /** 移除在线 */
    public void removeOnLine(String userId) {

        if (userId == null) {
            return;
        }
        String onlineToken = String.valueOf(redisUtil.getString(cacheKeyUtil.getLoginOnline() + userId));
        String mobileOnlineToken = String.valueOf(redisUtil.getString(cacheKeyUtil.getMobileLoginOnline() + userId));
        if (!StringUtils.isEmpty(onlineToken)) {
            redisUtil.remove(cacheKeyUtil.getLoginOnline() + userId);
            redisUtil.remove(onlineToken);
        }
        if (!StringUtils.isEmpty(mobileOnlineToken)) {
            redisUtil.remove(cacheKeyUtil.getMobileLoginOnline() + userId);
            redisUtil.removeHash(cacheKeyUtil.getMobileDeviceList(), userId);
        }
    }

    /** 是否在线 */
    public boolean isOnLine() {
        UserInfo userInfo = this.get();
        String online;
        if (ServletUtil.getIsMobileDevice()) {
            online = userInfo.getTenantId() + "login_online_mobile_" + userInfo.getUserId();
        } else {
            online = userInfo.getTenantId() + "login_online_" + userInfo.getUserId();
        }
        // 判断是否在线
        if (redisUtil.exists(online)) {
            // 判断在线的token是否正确
            if (userInfo.getId().equals(redisUtil.getString(online).toString())) {
                return true;
            }
        }
        return false;
    }

    /** 是否过期 */
    public boolean isOverdue() {
        UserInfo userInfo = this.get();
        return StringUtil.isEmpty(userInfo.getId()) ? false : true;
    }

    /** 是否登陆 */
    public boolean isLogined() {

        UserInfo userInfo = this.get();
        String userOnline = (ServletUtil.getIsMobileDevice() == true
                        ? cacheKeyUtil.getMobileLoginOnline()
                        : cacheKeyUtil.getLoginOnline())
                + userInfo.getUserId();
        Object online = redisUtil.getString(userOnline);
        return online == null ? true : false;
    }
}
