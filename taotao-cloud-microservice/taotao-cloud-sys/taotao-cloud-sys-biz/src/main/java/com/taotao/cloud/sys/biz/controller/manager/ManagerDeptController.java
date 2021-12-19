/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.sys.biz.controller.manager;

import cn.hutool.core.util.PageUtil;
import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.sys.api.dto.dept.DeptSaveDTO;
import com.taotao.cloud.sys.api.dto.dept.DeptUpdateDTO;
import com.taotao.cloud.sys.api.vo.dept.DeptQueryVO;
import com.taotao.cloud.sys.biz.entity.Dept;
import com.taotao.cloud.sys.biz.service.IDeptService;
import com.taotao.cloud.web.base.controller.SuperController;
import com.taotao.cloud.web.tree.ForestNodeMerger;
import groovy.util.logging.Log;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.pulsar.shade.io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台管理-部门管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:19:11
 */
@Validated
@RestController
@RequestMapping("/manager/dept")
@Tag(name = "后台管理-部门管理API", description = "后台管理-部门管理API")
public class ManagerDeptController extends
	SuperController<IDeptService, Dept, Long, BaseQuery, DeptSaveDTO, DeptUpdateDTO, DeptQueryVO> {

	///**
	// * 部门树
	// *
	// * @return Result
	// */
	//@PreAuth
	//@Log(value = "部门树", exception = "部门树请求异常")
	//@GetMapping("/tree")
	//@ApiOperation(value = "部门树", notes = "部门树")
	//public Result<?> tree() {
	//	return Result.data(ForestNodeMerger.merge(sysDepartService.tree()));
	//}
	//
	//@GetMapping
	//@ApiOperation(value = "获取树状结构")
	//public ResultMessage<List<DepartmentVO>> getByPage(Department entity,
	//	SearchVO searchVo) {
	//	return ResultUtil.data(departmentService.tree(PageUtil.initWrapper(entity, searchVo)));
	//
	//}
}
