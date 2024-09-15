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

package com.taotao.cloud.sys.facade.controller.buy;

import com.taotao.boot.common.model.Result;
import com.taotao.boot.security.spring.annotation.NotAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.sql.SQLIntegrityConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * pc端-字典API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:24:19
 */
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/sys/buyer/dict")
@Tag(name = "pc端-字典API", description = "pc端-字典API")
public class BuyerDictController {

	// @Autowired
	// private ProducerService producerService;

	@NotAuth
	@GetMapping("/add/{type}")
	@Operation(summary = "通过code查询所有字典列表")
	public Result<Boolean> add(@PathVariable String type)
		throws SQLIntegrityConstraintViolationException {
		return Result.success(true);
	}

	@NotAuth
	@GetMapping("/add1")
	@Operation(summary = "通过code查询所有字典列表")
	public Result<Boolean> add1() {
		return Result.success(true);
	}

	@GetMapping("/test/codexxxxx")
	@Operation(summary = "通过code查询所有字典列表")
	public Result<Boolean> testCode(@RequestParam String code) {
		// try {
		//	producerService.sendStringMsg();
		//	producerService.sendClassMsg();
		// } catch (PulsarClientException e) {
		//	LogUtils.error(e);
		// }

//        Dict byCode = service().findByCode(code);
//        LogUtils.info(String.valueOf(byCode));
		return Result.success(true);
	}

//    @NotAuth
//    @GetMapping("/testMybatisQueryStructure")
//    // @ApiOperation(value = "字典列表code查询", notes = "字典列表code查询")
//    public Result<Dict> testMybatisQueryStructure(@RequestParam Long dictId) {
//        DictQuery dictQuery = new DictQuery();
//        dictQuery.setDictId(dictId);
//        return Result.success(service().testMybatisQueryStructure(dictQuery));
//    }
}
