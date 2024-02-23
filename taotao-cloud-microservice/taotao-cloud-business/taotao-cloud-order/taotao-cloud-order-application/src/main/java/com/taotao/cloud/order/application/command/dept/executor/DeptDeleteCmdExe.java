/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.taotao.cloud.sys.application.command.dept.executor;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.taotao.cloud.sys.application.command.dept.dto.DeptDeleteCmd;
import com.taotao.cloud.sys.domain.dept.service.DeptDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 删除部门执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeptDeleteCmdExe {

	private final DeptDomainService deptDomainService;

	/**
	 * 执行删除部门.
	 *
	 * @param cmd 删除部门参数
	 * @return 执行删除结果
	 */
//	@DS(TENANT)
	public Boolean execute(DeptDeleteCmd cmd) {
		return deptDomainService.deleteById(cmd.getId());
	}

}
