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
package com.taotao.cloud.uc.biz.controller;

import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.uc.api.dto.dept.DeptSaveDTO;
import com.taotao.cloud.uc.api.dto.dept.DeptUpdateDTO;
import com.taotao.cloud.uc.api.vo.dept.DeptQueryVO;
import com.taotao.cloud.uc.biz.entity.SysDept;
import com.taotao.cloud.uc.biz.service.ISysDeptService;
import com.taotao.cloud.web.base.controller.SuperController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:19:11
 */
@Validated
@RestController
@RequestMapping("/uc/dept")
@Tag(name = "部门管理API", description = "部门管理API")
public class SysDeptController extends
	SuperController<ISysDeptService, SysDept, Long, BaseQuery, DeptSaveDTO, DeptUpdateDTO, DeptQueryVO> {


}
