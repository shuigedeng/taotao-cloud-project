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

package com.taotao.cloud.wechat.biz.wechatpush.controller;

import com.taotao.cloud.wechat.biz.wechatpush.service.Pusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * [百度天气API](https://lbsyun.baidu.com/apiconsole/center#/home)
 * [彩虹屁API](https://www.tianapi.com/apiview/181)
 *
 * <p>以及最重要的[微信测试账号](https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login)
 *
 * @author shuigedeng
 * @version 2022.08
 * @since 2022-08-24 21:00:36
 */
@RestController
public class PushController {
    /** 要推送的用户openid */
    @Value("${target.openId}")
    private String target;

    @Value("${target.test.openId}")
    private String testTarget;

    @Autowired
    Pusher pusherService;

    /** 微信测试账号推送 */
    @GetMapping("/push")
    public void push() {
        pusherService.push(target);
    }

    /** 微信测试账号推送 */
    @GetMapping("/push/test")
    public void pushTest() {
        pusherService.push(testTarget);
    }

    /** 微信测试账号推送 */
    @GetMapping("/push/{id}")
    public void pushId(@PathVariable("id") String id) {
        pusherService.push(id);
    }
}
