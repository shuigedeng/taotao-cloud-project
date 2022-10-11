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
package com.taotao.cloud.sys.biz.controller.business.buyer;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.idempotent.annotation.Idempotent;
import com.taotao.cloud.security.annotation.NotAuth;
import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
import com.taotao.cloud.sys.biz.service.business.IDictService;
import com.taotao.cloud.web.base.controller.SimpleController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.pulsar.shade.io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * pc端-字典API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:24:19
 */
@Validated
@RestController
@RequestMapping("/sys/buyer/dict")
@Tag(name = "pc端-字典API", description = "pc端-字典API")
public class BuyerDictController extends SimpleController<IDictService, Dict, Long> {

	@NotAuth
	@GetMapping("/add")
	@ApiOperation(value = "通过code查询所有字典列表", notes = "通过code查询所有字典列表")
	@Idempotent(perFix = "findByCode")
	public Result<Boolean> add() {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Boolean result = service().add();
		return success(result);
	}

	@GetMapping("/test/{code}")
	@ApiOperation(value = "通过code查询所有字典列表", notes = "通过code查询所有字典列表")
	public Result<Boolean> testCode(@PathVariable("code") String code) {
		Dict byCode = service().findByCode(code);
		LogUtils.info(String.valueOf(byCode));
		return Result.success(true);
	}

}

