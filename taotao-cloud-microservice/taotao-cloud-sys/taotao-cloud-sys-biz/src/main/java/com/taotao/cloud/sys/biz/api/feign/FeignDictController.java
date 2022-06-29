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

import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.netty.annotation.PathVariable;
import com.taotao.cloud.sys.api.feign.IFeignDictService;
import com.taotao.cloud.sys.api.feign.response.DictResponse;
import com.taotao.cloud.sys.biz.mapstruct.IDictMapStruct;
import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
import com.taotao.cloud.sys.biz.service.IDictService;
import com.taotao.cloud.web.base.controller.SimpleController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端-字典API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:24:19
 */
@Validated
@RestController
@RequestMapping("/sys/remote/dict")
@Tag(name = "内部服务端-字典API", description = "内部服务端-字典API")
public class FeignDictController extends SimpleController<IDictService, Dict, Long> {

	/**
	 * @see IFeignDictService#findByCode(String)
	 */
	@Operation(summary = "字典列表code查询", description = "字典列表请求异常")
	@RequestLogger
	@GetMapping("/code/{code}")
	public DictResponse findByCode(@PathVariable String code) {
		Dict dict = service().findByCode(code);
		return IDictMapStruct.INSTANCE.dictToDictResponse(dict);
	}

}

