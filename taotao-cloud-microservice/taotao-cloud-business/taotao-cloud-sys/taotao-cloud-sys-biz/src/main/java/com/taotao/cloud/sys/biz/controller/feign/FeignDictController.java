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
//package com.taotao.cloud.sys.biz.controller.feign;
//
//import com.alibaba.csp.sentinel.annotation.SentinelResource;
//import com.taotao.boot.common.exception.BusinessException;
//import com.taotao.boot.common.utils.log.LogUtils;
//import com.taotao.boot.idempotent.annotation.Idempotent;
//import com.taotao.boot.ratelimit.annotation.GuavaLimit;
//import com.taotao.boot.ratelimit.annotation.Limit;
//import com.taotao.boot.security.spring.annotation.NotAuth;
//import com.taotao.cloud.sys.api.feign.IFeignDictApi;
//import com.taotao.cloud.sys.api.feign.response.FeignDictResponse;
//import com.taotao.cloud.sys.biz.model.convert.DictConvert;
//import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
//import com.taotao.cloud.sys.biz.service.business.IDictService;
//import com.taotao.boot.webagg.controller.BaseFeignController;
//import com.taotao.boot.web.request.annotation.RequestLogger;
//import com.yomahub.tlog.core.annotation.TLogAspect;
//import io.swagger.v3.oas.annotations.Operation;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 内部服务端-字典API
// *
// * @author shuigedeng
// * @version 2021.9
// * @since 2021-10-09 14:24:19
// */
//@Validated
//@RestController
//@RequestMapping("/sys/feign/dict")
//public class FeignDictController extends BaseFeignController<IDictService, Dict, Long> implements IFeignDictApi {
//
//    @Override
//    @NotAuth
//    @Idempotent(perFix = "findByCode")
//    @Limit(key = "limitTest", period = 10, count = 3)
//    @SentinelResource("findByCode")
//    public FeignDictResponse findByCode(@RequestParam(value = "code") String code) {
//        if ("sd".equals(code)) {
//            throw new BusinessException("我出错了");
//            // try {
//            //	Thread.sleep(100000000000L);
//            // } catch (InterruptedException e) {
//            //	throw new RuntimeException(e);
//            // }
//        }
//        Dict dict = service().findByCode(code);
//        return DictConvert.INSTANCE.convert(dict);
//    }
//
//    @Override
//    @Operation(summary = "test", description = "test")
//    @RequestLogger
//    @NotAuth
//    @Idempotent(perFix = "test")
//    @TLogAspect(
//            value = {"code"},
//            pattern = "{{}}",
//            joint = ",",
//            str = "nihao")
//    @Limit(key = "limitTest", period = 10, count = 3)
//    @GuavaLimit
//    @SentinelResource("test")
//    @GetMapping("/test")
//    public FeignDictResponse test(@RequestParam(value = "id") String id) {
//        LogUtils.info("sldfkslfdjalsdfkjalsfdjl");
//        Dict dict = service().findByCode(id);
//
//        Future<Dict> asyncByCode = service().findAsyncByCode(id);
//
//        Dict dict1;
//        try {
//            dict1 = asyncByCode.get();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//
//        LogUtils.info("我在等待你");
//
//        return null;
//        // return IDictMapStruct.INSTANCE.dictToFeignDictRes(dict);
//    }
//}
