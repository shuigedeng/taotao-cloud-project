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

package com.taotao.cloud.sa.just.biz.sa.cases.use;

import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 为路由拦截鉴权准备的路示例
 *
 * @author kong
 * @since 2022-10-15
 */
@RestController
public class RouterCheckController {

    // 路由拦截鉴权测试   ---- http://localhost:8081/xxx
    @RequestMapping({
        "/user/doLogin",
        "/user/doLogin2",
        "/user/info",
        "/admin/info",
        "/goods/info",
        "/orders/info",
        "/notice/info",
        "/comment/info",
        "/router/print",
        "/router/print2"
    })
    public SaResult checkLogin() {
        return SaResult.ok();
    }
}
