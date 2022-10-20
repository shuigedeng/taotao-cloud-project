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
package com.taotao.cloud.sys.biz.controller.business.manager;

import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.sys.api.model.dto.dict.DictSaveDTO;
import com.taotao.cloud.sys.api.model.dto.dict.DictUpdateDTO;
import com.taotao.cloud.sys.api.model.vo.dict.DictQueryVO;
import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
import com.taotao.cloud.sys.biz.service.business.IDictService;
import com.taotao.cloud.web.base.controller.BusinessSuperController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台管理端-字典管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:24:19
 */
@Validated
@RestController
@RequestMapping("/sys/manager/dict")
@Tag(name = "平台管理端-字典管理API", description = "平台管理端-字典管理API")
public class ManagerDictController extends
	BusinessSuperController<IDictService, Dict, Long, BaseQuery, DictSaveDTO, DictUpdateDTO, DictQueryVO> {

	///**
	// * 通过code查询所有字典列表
	// *
	// * @param code 　code
	// * @return Result
	// */
	//@PreAuth
	//@Log(value = "字典列表code查询", exception = "字典列表请求异常")
	//@GetMapping("/list-code")
	//@ApiOperation(value = "字典列表code查询", notes = "字典列表code查询")
	//public Result<?> listCode(String code) {
	//	return sysDictService.getList(code);
	//}
	//
	///**
	// * 根据code和key获取字典value
	// *
	// * @param code    code
	// * @param dictKey key
	// * @return Result
	// */
	//@PreAuth
	//@Log(value = "字典列表key查询", exception = "字典列表key查询请求异常")
	//@GetMapping("/get-dict-value")
	//@ApiOperation(value = "字典列表key查询", notes = "字典列表key查询")
	//public Result<?> getDictValue(String code, String dictKey) {
	//	return sysDictService.getValue(code, dictKey);
	//}
	//
	//
	///**
	// * 字典删除
	// *
	// * @param ids 多个id采用逗号分隔
	// * @return Result
	// */
	//@PreAuth
	//@Log(value = "字典删除", exception = "字典删除请求异常")
	//@PostMapping("/del")
	//@ApiOperation(value = "字典删除", notes = "字典删除")
	//@ApiImplicitParams({
	//	@ApiImplicitParam(name = "ids", required = true, value = "多个用,号隔开", paramType = "form")
	//})
	//@Transactional(rollbackFor = Exception.class)
	//public Result<?> del(@RequestParam String ids) {
	//	Collection idsCollection = CollectionUtil.stringToCollection(ids);
	//	if (sysDictService.removeByIds(idsCollection)) {
	//		//批量删除字典列表的同时，也要删除字典项的内容
	//		for (Object obj : idsCollection) {
	//			sysDictService.remove(
	//				new LambdaQueryWrapper<SysDict>().eq(SysDict::getParentId, obj));
	//		}
	//		return Result.success("删除成功");
	//	}
	//	return Result.fail("删除失败");
	//}

}

