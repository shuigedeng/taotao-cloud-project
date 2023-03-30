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

package com.taotao.cloud.sa.just.biz.sa.cases.up;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sa-Token 记住我模式登录
 *
 * @author kong
 * @since 2022-10-17
 */
@RestController
@RequestMapping("/RememberMe/")
public class RememberMeController {

    // 记住我登录    ---- http://localhost:8081/RememberMe/doLogin?name=zhang&pwd=123456
    @RequestMapping("doLogin")
    public SaResult doLogin(String name, String pwd) {
        if ("zhang".equals(name) && "123456".equals(pwd)) {
            StpUtil.login(10001, true);
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }

    // 不记住我登录    ---- http://localhost:8081/RememberMe/doLogin2?name=zhang&pwd=123456
    @RequestMapping("doLogin2")
    public SaResult doLogin2(String name, String pwd) {
        if ("zhang".equals(name) && "123456".equals(pwd)) {
            StpUtil.login(10001, false);
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }

    // 七天免登录    ---- http://localhost:8081/RememberMe/doLogin3?name=zhang&pwd=123456
    @RequestMapping("doLogin3")
    public SaResult doLogin3(String name, String pwd) {
        if ("zhang".equals(name) && "123456".equals(pwd)) {
            StpUtil.login(10001, 60 * 60 * 24 * 7);
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }
}
