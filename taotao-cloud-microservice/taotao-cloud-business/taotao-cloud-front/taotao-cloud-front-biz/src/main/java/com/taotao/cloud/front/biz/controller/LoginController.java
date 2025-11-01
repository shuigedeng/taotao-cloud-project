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

package com.taotao.cloud.front.biz.controller;

import com.taotao.cloud.front.biz.util.Constants;
import com.taotao.cloud.front.biz.util.CookieUtil;
import com.taotao.cloud.front.biz.util.ResponseBase;
import com.taotao.cloud.front.biz.util.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * LoginController
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/1/18 下午4:56
 */
@Controller
public class LoginController {

    private static final String LOGIN = "login";

    private static final String INDEX = "redirect:/";

    private static final String ERROR = "error";

    private static final String RELATION = "qqrelation";

    // @Autowired
    // private UserServiceFegin userServiceFegin;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = CookieUtil.getUid(request, Constants.COOKIE_MEMBER_TOKEN);
        LogUtils.info("++++++++++++++++++ login token: " + token);

        if (StrUtil.isNotEmpty(token)) {
            response.sendRedirect(INDEX);
        }

        return LOGIN;
    }

    @PostMapping("/login")
    public String login(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1.调用登录接口
        // ResponseBase login = userServiceFegin.login(user);

        ResponseBase responseBase = new ResponseBase();
        responseBase.setCode(200);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(Constants.MEMBER_TOKEN, UUID.randomUUID().toString());
        responseBase.setData(map);

        if (!responseBase.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            request.setAttribute("error", "账号或密码错误!");
            return LOGIN;
        }

        // 2.登录成功，获取token信息
        LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) responseBase.getData();
        String token = linkedHashMap.get(Constants.MEMBER_TOKEN);
        if (StrUtil.isEmpty(token)) {
            request.setAttribute("error", "token已经失效!");
            return LOGIN;
        }
        CookieUtil.addCookie(response, Constants.COOKIE_MEMBER_TOKEN, token, Constants.COOKIE_TOKEN_MEMBER_TIME);
        response.sendRedirect(INDEX);
        return null;
    }

    // @GetMapping("/localQqLogin")
    // public String localQqLogin(HttpServletRequest request) throws QQConnectException {
    // 	String authorizeURL = new Oauth().getAuthorizeURL(request);
    // 	return "redirect:" + authorizeURL;
    // }

    // @GetMapping("/qqLoginCallback")
    // public String qqLoginCallback(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
    // 	throws QQConnectException {
    // 	AccessToken tokenObj = new Oauth().getAccessTokenByRequest(request);
    // 	if (tokenObj == null) {
    // 		request.setAttribute("error", "qq授权失败!");
    // 		return ERROR;
    // 	}
    // 	String accessToken = tokenObj.getAccessToken();
    // 	if (StringUtils.isEmpty(accessToken)) {
    // 		request.setAttribute("error", "qq授权失败!");
    // 		return ERROR;
    // 	}
    //
    // 	OpenID openID = new OpenID(accessToken);
    // 	String userOpenID = openID.getUserOpenID();
    // 	ResponseBase userByOpenId = userServiceFegin.findUserByOpenId(userOpenID);
    // 	// 用戶沒有关联QQ账号
    // 	if (userByOpenId.getCode().equals(Constants.HTTP_RES_CODE_201)) {
    // 		// 跳转到管理账号
    // 		httpSession.setAttribute("qqOpenid", userOpenID);
    // 		return RELATION;
    // 	}
    // 	LinkedHashMap linkedHashMap = (LinkedHashMap) userByOpenId.getData();
    // 	String token = (String) linkedHashMap.get(Constants.MEMBER_TOKEN);
    // 	CookieUtil.addCookie(response, Constants.COOKIE_MEMBER_TOKEN, token, Constants.COOKIE_TOKEN_MEMBER_TIME);
    // 	return INDEX;
    // }

    // @PostMapping("/qqRelation")
    // public String qqRelation(User user, HttpServletRequest request, HttpServletResponse response, HttpSession
    // httpSession) {
    // 	// 1.获取openid
    // 	String qqOpenid = (String) httpSession.getAttribute("qqOpenid");
    // 	if (StringUtils.isEmpty(qqOpenid)) {
    // 		request.setAttribute("error", "没有获取到openid");
    // 		return "error";
    // 	}
    // 	// 2.调用登录接口，获取token信息
    // 	user.setOpenid(qqOpenid);
    // 	ResponseBase loginBase = userServiceFegin.qqLoginOpenId(user);
    // 	if (!loginBase.getCode().equals(Constants.HTTP_RES_CODE_200)) {
    // 		request.setAttribute("error", "账号或者密码错误!");
    // 		return LOGIN;
    // 	}
    //
    // 	LinkedHashMap linkedHashMap = (LinkedHashMap) loginBase.getData();
    // 	String token = (String) linkedHashMap.get(Constants.MEMBER_TOKEN);
    // 	if (StringUtils.isEmpty(token)) {
    // 		request.setAttribute("error", "会话已经失效!");
    // 		return LOGIN;
    // 	}
    // 	// 3.将token信息存放在cookie里面
    // 	CookieUtil.addCookie(response, Constants.COOKIE_MEMBER_TOKEN, token, Constants.COOKIE_TOKEN_MEMBER_TIME);
    // 	return INDEX;
    // }

}
