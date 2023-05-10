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

package com.taotao.cloud.tenant.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.tenant.api.feign.fallback.FeignTenantFallbackImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author
 * @version 0.0.1
 * @date 2022-04-07 14:50
 */
@FeignClient(
	contextId = "tenantServiceApi",
	value = ServiceName.TAOTAO_CLOUD_TENANT,
	fallbackFactory = FeignTenantFallbackImpl.class)
public interface TenantServiceApi {

	/**
	 * 校验租户信息是否合法
	 *
	 * @param id 租户id
	 */
	@GetMapping(value = "/tenant/validTenant/{id}")
	void validTenant(@PathVariable("id") Long id);
}
