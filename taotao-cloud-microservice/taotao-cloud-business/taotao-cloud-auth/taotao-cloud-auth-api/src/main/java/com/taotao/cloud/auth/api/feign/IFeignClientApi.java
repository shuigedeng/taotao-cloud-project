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

 package com.taotao.cloud.auth.api.feign;

 import com.taotao.cloud.auth.api.feign.fallback.FeignClientFallback;
 import com.taotao.cloud.auth.api.feign.request.FeignClientQueryRequest;
 import com.taotao.cloud.auth.api.feign.response.FeignClientResponse;
 import com.taotao.cloud.common.constant.ServiceName;
 import org.springframework.cloud.openfeign.FeignClient;

 /**
  * 远程调用客户端
  *
  * @author shuigedeng
  * @version 2022.03
  * @since 2020/5/2 16:42
  */
 @FeignClient(
	 contextId = "remoteUserService",
	 value = ServiceName.TAOTAO_CLOUD_AUTH,
	 fallbackFactory = FeignClientFallback.class)
 public interface IFeignClientApi {

	 FeignClientResponse query(FeignClientQueryRequest feignClientQueryRequest);
 }
