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

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sa-Token 前后端分离模式示例
 *
 * @author kong
 * @since 2022-10-17
 */
@RestController
@RequestMapping("/NotCookie/")
public class NotCookieController {

    // 前后端一体模式的登录样例    ---- http://localhost:8081/NotCookie/doLogin?name=zhang&pwd=123456
    @RequestMapping("doLogin")
    public SaResult doLogin(String name, String pwd) {
        if ("zhang".equals(name) && "123456".equals(pwd)) {
            // 会话登录
            StpUtil.login(10001);
            return SaResult.ok();
        }
        return SaResult.error("登录失败");
    }

    // 前后端分离模式的登录样例    ---- http://localhost:8081/NotCookie/doLogin2?name=zhang&pwd=123456
    @RequestMapping("doLogin2")
    public SaResult doLogin2(String name, String pwd) {

        if ("zhang".equals(name) && "123456".equals(pwd)) {

            // 会话登录
            StpUtil.login(10001);

            // 与常规登录不同点之处：这里需要把 Token 信息从响应体中返回到前端
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return SaResult.data(tokenInfo);
        }
        return SaResult.error("登录失败");
    }
}
