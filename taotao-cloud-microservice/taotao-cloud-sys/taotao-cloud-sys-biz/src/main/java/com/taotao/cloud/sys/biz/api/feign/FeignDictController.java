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
package com.taotao.cloud.sys.biz.api.feign;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.feign.annotation.FeignApi;
import com.taotao.cloud.security.annotation.NotAuth;
import com.taotao.cloud.sys.api.feign.IFeignDictService;
import com.taotao.cloud.sys.api.feign.response.FeignDictRes;
import com.taotao.cloud.sys.biz.mapstruct.IDictMapStruct;
import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
import com.taotao.cloud.sys.biz.service.IDictService;
import com.taotao.cloud.web.base.controller.SimpleController;
import com.taotao.cloud.web.idempotent.Idempotent;
import com.taotao.cloud.web.limit.Limit;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 内部服务端-字典API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:24:19
 */
@FeignApi
@Validated
@RestController
@RequestMapping("/sys/remote/dict")
public class FeignDictController extends SimpleController<IDictService, Dict, Long> {

	/**
	 * 字典列表code查询
	 *
	 * @param code 代码
	 * @return {@link FeignDictRes }
	 * @see IFeignDictService#findByCode(String)
	 * @since 2022-07-02 10:17:59
	 */
	@NotAuth
	@Idempotent(perFix = "findByCode")
	@Limit(key = "limitTest", period = 10, count = 3)
	@SentinelResource("findByCode")
	@GetMapping("/code")
	public FeignDictRes findByCode(@RequestParam(value = "code") String code) {
		if ("sd".equals(code)) {
			throw new BusinessException("我出错了");
			//try {
			//	Thread.sleep(100000000000L);
			//} catch (InterruptedException e) {
			//	throw new RuntimeException(e);
			//}
		}
		Dict dict = service().findByCode(code);
		return IDictMapStruct.INSTANCE.dictToFeignDictRes(dict);
	}

	@NotAuth
	@GetMapping("/test")
	public FeignDictRes test(@RequestParam(value = "code") String code) {
		Dict dict = service().findByCode(code);
		return IDictMapStruct.INSTANCE.dictToFeignDictRes(dict);
	}
}

