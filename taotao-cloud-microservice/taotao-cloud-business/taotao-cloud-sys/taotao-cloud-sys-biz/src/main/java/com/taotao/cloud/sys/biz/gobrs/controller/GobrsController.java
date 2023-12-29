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

package com.taotao.cloud.sys.biz.gobrs.controller;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sys.biz.gobrs.service.GobrsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Gobrs controller.
 *
 * @program: gobrs -async-core @ClassName GobrsController
 * @description: Controller
 * @author: sizegang
 * @create: 2022 -03-20
 */
@RestController
@RequestMapping("gobrs")
public class GobrsController {

    @Autowired
    private GobrsService gobrsService;

    /**
     * Gobrs test string.
     *
     * @return the string
     */
    @RequestMapping("testGobrs")
    public String gobrsTest() {
        gobrsService.gobrsTest();
        return "success";
    }

    @RequestMapping("updateRule")
    public String updateRule() {
        gobrsService.updateRule();
        return "success";
    }

    /** Future. */
    @RequestMapping("future")
    public void future() {}

    /** Sets gobrs async. */
    @RequestMapping("gobrsAsync")
    public void setGobrsAsync() {
        // 开始时间: 获取当前时间毫秒数
        long start = System.currentTimeMillis();
        gobrsService.gobrsAsync();
        // 结束时间: 当前时间 - 开始时间
        long coust = System.currentTimeMillis() - start;
        LogUtils.info("gobrs-Async " + coust);
    }
}
