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

package com.taotao.cloud.tenant.api.inner.command;

import com.taotao.boot.common.constant.ServiceNameConstants;
import com.taotao.boot.common.model.request.Request;
import com.taotao.boot.common.model.response.Response;
import com.taotao.boot.common.support.info.ApiInfo;
import com.taotao.boot.common.support.info.Create;
import com.taotao.boot.common.support.info.Update;
import com.taotao.cloud.tenant.api.inner.dto.command.TenantApiCommand;
import com.taotao.cloud.tenant.api.inner.dto.response.TenantQueryApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import static com.taotao.boot.common.support.info.ApiVersionEnum.V2022_07;
import static com.taotao.boot.common.support.info.ApiVersionEnum.V2022_08;

/**
 * 远程调用后台用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@HttpExchange(value = ServiceNameConstants.TAOTAO_CLOUD_SYS)
public interface TenantCommandApi {

	/**
	 * 字典列表code查询
	 *
	 * @param dictQueryApiRequest 代码
	 * @since 2022-06-29 21:40:21
	 */
	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(
				version = V2022_07,
				content = "主要修改了配置信息的接口查询",
				date = "2022-07-01 17:11:55"),
			@Update(
				version = V2022_08,
				content = "主要修改了配置信息的接口查询08",
				date = "2022-07-01 17:11:55")
		})
	@PostExchange("/sys/dict/test")
	Response<TenantQueryApiResponse> test(
		@Validated @RequestBody Request<TenantApiCommand> dictQueryApiRequest );
}
