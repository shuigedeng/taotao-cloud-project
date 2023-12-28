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
 * Sa-Token 身份切换
 *
 * @author kong
 * @since 2022-10-17
 */
@RestController
@RequestMapping("/SwitchTo/")
public class SwitchToController {

    /*
     * 前提：首先调用登录接口进行登录，代码在 com.pj.cases.use.LoginAuthController 中有详细解释，此处不再赘述
     * 		---- http://localhost:8081/acc/doLogin?name=zhang&pwd=123456
     */

    // 身份切换    ---- http://localhost:8081/SwitchTo/switchTo?userId=10044
    @RequestMapping("switchTo")
    public SaResult switchTo(long userId) {
        // 将当前会话 [身份临时切换] 为其它账号
        // 		--- 切换的有效期为本次请求
        StpUtil.switchTo(userId);

        // 此时再调用此方法会返回 10044 (我们临时切换到的账号id)
        LogUtils.info(StpUtil.getLoginId());
        ;

        // 结束 [身份临时切换]
        StpUtil.endSwitch();

        // 此时再打印账号，就又回到了原来的值：10001
        LogUtils.info(StpUtil.getLoginId());

        return SaResult.ok();
    }

    // 以 lambda 表达式的方式身份切换    ---- http://localhost:8081/SwitchTo/switchTo2?userId=10044
    @RequestMapping("switchTo2")
    public SaResult switchTo2(long userId) {

        // 输出 10001
        LogUtils.info("------- [身份临时切换] 调用前，当前登录账号id是：" + StpUtil.getLoginId());

        // 以 lambda 表达式的方式身份切换，作用范围只在这个 lambda 表达式内有效
        StpUtil.switchTo(userId, () -> {
            LogUtils.info("是否正在身份临时切换中: " + StpUtil.isSwitch()); // 输出 true
            LogUtils.info("获取当前登录账号id: " + StpUtil.getLoginId()); // 输出 10044
        });

        // 结束后，再次获取当前登录账号，输出10001
        LogUtils.info("------- [身份临时切换] 调用结束，当前登录账号id是：" + StpUtil.getLoginId());

        return SaResult.ok();
    }
}
