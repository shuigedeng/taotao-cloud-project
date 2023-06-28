///*
// * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.taotao.cloud.sys.biz.controller.mybatis;
//
//import com.taotao.cloud.common.model.Result;
//import com.taotao.cloud.security.springsecurity.annotation.NotAuth;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.AllArgsConstructor;
//import org.apache.pulsar.shade.io.swagger.annotations.ApiOperation;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * mybatis流式查询
// *
// * @author shuigedeng
// * @version 2021.9
// * @since 2021-10-09 14:24:19
// */
//@Validated
//@RestController
//@AllArgsConstructor
//@RequestMapping("/sys/mybatis/cursor")
//@Tag(name = "pc端-mybatis流式查询API", description = "pc端-mybatis流式查询API")
//public class MybatisCursorController {
//
//    private final WholesalerBaseService wholesalerBaseService;
//
//    @NotAuth
//    @GetMapping("/add/{type}")
//    @ApiOperation(value = "通过code查询所有字典列表", notes = "通过code查询所有字典列表")
//    public Result<Boolean> add(@PathVariable String type) {
//
//        // 方式一: 基于mybatis的游标规则  类似于oracle的fetchsize缓冲区
//        wholesalerBaseService.fixReuseSendMsgNew();
//        wholesalerBaseService.scheduleGenerate();
//
//        // 方式二: SqlSession 方式
//        wholesalerBaseService.cursorTest();
//
//        // 方式三: TransactionTemplate 方式
//        wholesalerBaseService.transactionTemplateTest();
//
//        // 方式四: @Transactional 注解
//        wholesalerBaseService.transactionalTest();
//
//        return null;
//    }
//}
