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
//import com.taotao.boot.common.utils.log.LogUtils;
//import com.taotao.boot.web.annotation.FeignApi;
//import com.taotao.boot.webagg.controller.BaseFeignController;
//import com.taotao.cloud.sys.api.grpc.FileGrpcServiceGrpc;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 内部服务端-字典API
// *
// * @author shuigedeng
// * @version 2021.9
// * @since 2021-10-09 14:24:19
// */
//@FeignApi
//@Validated
//@RestController
//@Tag(name = "FeignFileController", description = "FeignFileController")
//public class FeignFileController implements IFeignFileApi {
//
//
//	// @Override
//	// @Operation(summary = "test", description = "test")
//	// @RequestLogger
//	// @NotAuth
//	// @Idempotent(perFix = "test")
//	// @TLogAspect(
//	//         value = {"code"},
//	//         pattern = "{{}}",
//	//         joint = ",",
//	//         str = "nihao")
//	// @Limit(key = "limitTest", period = 10, count = 3)
//	// @GuavaLimit
//	// @SentinelResource("test")
//	// @GetMapping("/test")
//	// public FeignDictResponse test(@RequestParam(value = "id") String id) {
//	//     LogUtils.info("sldfkslfdjalsdfkjalsfdjl");
//	//     Dict dict = service().findByCode(id);
//	//
//	//     Future<Dict> asyncByCode = service().findAsyncByCode(id);
//	//
//	//     Dict dict1;
//	//     try {
//	//         dict1 = asyncByCode.get();
//	//     } catch (InterruptedException | ExecutionException e) {
//	//         throw new RuntimeException(e);
//	//     }
//	//
//	//     LogUtils.info("我在等待你");
//	//
//	//     return null;
//	//     // return IDictMapStruct.INSTANCE.dictToFeignDictRes(dict);
//	// }
//
//	@Override
//	public FeignFileResponse findByCode(String code) {
//
//		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
//			.usePlaintext()
//			.build();
//		File file = FileGrpcServiceGrpc.newBlockingStub(channel)
//			.searchFile(ConditionsRequest.newBuilder().setId("112").build());
//		LogUtils.info(file.toString());
//
//		return FeignFileResponse.builder().description("sdfasdfsa").dictCode("sfd").build();
//	}
//}
