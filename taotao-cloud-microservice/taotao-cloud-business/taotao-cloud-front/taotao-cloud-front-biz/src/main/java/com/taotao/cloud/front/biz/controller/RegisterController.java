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
import com.taotao.cloud.front.biz.util.ResponseBase;
import com.taotao.cloud.front.biz.util.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * RegisterController
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/1/18 下午4:56
 */
@Controller
public class RegisterController {

    // @Autowired
    // private UserServiceFegin userServiceFegin;

    private static final String LOGIN = "login";
    private static final String REGISTER = "register";

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return REGISTER;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(User user, HttpServletRequest reqest, HttpServletResponse response) throws IOException {

        // ResponseBase registerUser = userServiceFegin.registerUser(user);
        ResponseBase responseBase = new ResponseBase();
        responseBase.setCode(200);

        if (!responseBase.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            reqest.setAttribute("error", responseBase.getMessage());
            return REGISTER;
        }

        // 2.注册成功，跳转到登录页面
        response.sendRedirect(LOGIN);
        return LOGIN;
    }
}
