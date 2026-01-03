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

package com.taotao.cloud.doris.spring.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.doris.demo.spring.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DemoController
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@RestController
@RequestMapping("/rest/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/skulist/{skuId}")
    public Object getSkuList( @PathVariable(value = "skuId") String skuId ) {
        Map<String, Object> params = new HashMap<>();
        if (skuId != null) {
            params.put("skuId", skuId);
        }
        return demoService.getSkuList(params);
    }

    @GetMapping("/skulist")
    public Object getSkuList() {
        Map<String, Object> params = new HashMap<>();
        return demoService.getSkuList(params);
    }
}
