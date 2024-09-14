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

package com.taotao.cloud.sys.api.feign;

import com.taotao.boot.common.constant.ServiceName;
import com.taotao.boot.common.support.info.ApiInfo;
import com.taotao.boot.common.support.info.Create;
import com.taotao.boot.common.support.info.Update;
import com.taotao.cloud.openfeign.annotation.FeignInner;
import com.taotao.cloud.openfeign.annotation.FeignRetry;
import com.taotao.cloud.sys.api.feign.fallback.DictApiFallback;
import com.taotao.cloud.sys.api.feign.response.DictApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.taotao.boot.common.support.info.ApiVersionEnum.V2022_07;
import static com.taotao.boot.common.support.info.ApiVersionEnum.V2022_08;


/**
 * 远程调用后台用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(
	name = ServiceName.TAOTAO_CLOUD_SYS,
	fallbackFactory = DictApiFallback.class)
public interface DictApi {

	/**
	 * 字典列表code查询
	 *
	 * @param code 代码
	 * @return {@link DictApiResponse }
	 * @since 2022-06-29 21:40:21
	 */
	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		})
	@FeignRetry(maxAttempt=  6, backoff = @FeignRetry.Backoff(delay = 500L, maxDelay = 20000L, multiplier = 4))
	@FeignInner
	@GetMapping("/sys/feign/dict/code")
	DictApiResponse findByCode(@RequestParam(value = "code") String code);

	/**
	 * 字典列表code查询
	 *
	 * @param id 代码
	 * @return {@link DictApiResponse }
	 * @since 2022-06-29 21:40:21
	 */
	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		})
	@GetMapping("/sys/feign/dict/code")
	DictApiResponse test(@RequestParam(value = "id") String id);
}
