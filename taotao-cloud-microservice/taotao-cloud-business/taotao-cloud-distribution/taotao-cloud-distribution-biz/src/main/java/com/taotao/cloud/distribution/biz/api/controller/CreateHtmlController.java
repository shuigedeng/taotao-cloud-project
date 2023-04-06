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

package com.taotao.cloud.distribution.biz.api.controller;

import com.itstyle.distribution.common.entity.Result;
import com.itstyle.distribution.service.ICreateHtmlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "生成静态商品页")
@RestController
@RequestMapping("/createHtml")
public class CreateHtmlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateHtmlController.class);

    @Autowired
    private ICreateHtmlService createHtmlService;

    @ApiOperation(value = "生成静态商品页", nickname = "科帮网")
    @PostMapping("/start")
    public Result start() {
        LOGGER.info("生成秒杀活动静态页");
        return createHtmlService.createAllHtml();
    }
}
