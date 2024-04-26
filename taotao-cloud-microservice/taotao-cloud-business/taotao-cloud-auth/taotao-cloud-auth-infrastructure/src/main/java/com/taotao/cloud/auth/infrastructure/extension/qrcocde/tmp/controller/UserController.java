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

package com.taotao.cloud.auth.infrastructure.extension.qrcocde.tmp.controller;

import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.entity.Response;
import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.entity.User;
import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.service.UserService;
import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/getUser", method = RequestMethod.GET)
    @ResponseBody
    public Response getUser() {
        User user = hostHolder.getUser();
        if (user == null) {
            return Response.createErrorResponse("用户未登录");
        }
        return Response.createResponse(null, user);
    }
}
