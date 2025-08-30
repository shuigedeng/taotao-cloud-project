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

package com.taotao.cloud.xxljob.service.impl;

import com.taotao.cloud.xxljob.core.model.XxlJobUser;
import com.taotao.cloud.xxljob.core.util.CookieUtil;
import com.taotao.cloud.xxljob.core.util.I18nUtil;
import com.taotao.cloud.xxljob.core.util.JacksonUtil;
import com.taotao.cloud.xxljob.dao.XxlJobUserDao;
import com.xxl.job.core.biz.model.ReturnT;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author xuxueli 2019-05-04 22:13:264
 */
@Service
public class LoginService {

    public static final String LOGIN_IDENTITY_KEY = "XXL_JOB_LOGIN_IDENTITY";

    @Resource private XxlJobUserDao xxlJobUserDao;

    // ---------------------- token tool ----------------------

    private String makeToken(XxlJobUser xxlJobUser) {
        String tokenJson = JacksonUtil.writeValueAsString(xxlJobUser);
        String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
        return tokenHex;
    }

    private XxlJobUser parseToken(String tokenHex) {
        XxlJobUser xxlJobUser = null;
        if (tokenHex != null) {
            String tokenJson =
                    new String(
                            new BigInteger(tokenHex, 16).toByteArray()); // username_password(md5)
            xxlJobUser = JacksonUtil.readValue(tokenJson, XxlJobUser.class);
        }
        return xxlJobUser;
    }

    // ---------------------- login tool, with cookie and db ----------------------

    public ReturnT<String> login(
            HttpServletRequest request,
            HttpServletResponse response,
            String username,
            String password,
            boolean ifRemember) {

        // param
        if (username == null
                || username.trim().length() == 0
                || password == null
                || password.trim().length() == 0) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_empty"));
        }

        // valid passowrd
        XxlJobUser xxlJobUser = xxlJobUserDao.loadByUserName(username);
        if (xxlJobUser == null) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!passwordMd5.equals(xxlJobUser.getPassword())) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }

        String loginToken = makeToken(xxlJobUser);

        // do login
        CookieUtil.set(response, LOGIN_IDENTITY_KEY, loginToken, ifRemember);
        return ReturnT.ofSuccess();
    }

    /**
     * logout
     *
     * @param request
     * @param response
     */
    public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
        return ReturnT.ofSuccess();
    }

    /**
     * logout
     *
     * @param request
     * @return
     */
    public XxlJobUser ifLogin(HttpServletRequest request, HttpServletResponse response) {
        String cookieToken = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
        if (cookieToken != null) {
            XxlJobUser cookieUser = null;
            try {
                cookieUser = parseToken(cookieToken);
            } catch (Exception e) {
                logout(request, response);
            }
            if (cookieUser != null) {
                XxlJobUser dbUser = xxlJobUserDao.loadByUserName(cookieUser.getUsername());
                if (dbUser != null) {
                    if (cookieUser.getPassword().equals(dbUser.getPassword())) {
                        return dbUser;
                    }
                }
            }
        }
        return null;
    }
}
