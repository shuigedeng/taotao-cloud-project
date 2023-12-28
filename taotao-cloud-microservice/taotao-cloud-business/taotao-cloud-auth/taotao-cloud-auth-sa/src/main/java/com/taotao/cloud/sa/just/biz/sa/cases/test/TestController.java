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

package com.taotao.cloud.sa.just.biz.sa.cases.test;

import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试专用 Controller
 *
 * @author kong
 */
@RestController
@RequestMapping("/test/")
public class TestController {

    // 测试   浏览器访问： http://localhost:8081/test/test
    @RequestMapping("test")
    public SaResult test() {
        LogUtils.info("------------进来了");
        return SaResult.ok();
    }

    // 测试   浏览器访问： http://localhost:8081/test/test2
    @RequestMapping("test2")
    public SaResult test2() {
        LogUtils.info("------------进来了");
        return SaResult.ok();
    }
}
