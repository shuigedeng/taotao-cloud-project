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

package com.taotao.cloud.sys.application.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.sys.application.command.dept.dto.DeptUpdateCmd;
import com.taotao.cloud.sys.application.command.dept.dto.clientobject.DeptCO;
import com.taotao.cloud.sys.application.command.dict.dto.DictDeleteCmd;
import com.taotao.cloud.sys.application.command.dict.dto.DictGetQry;
import com.taotao.cloud.sys.application.command.dict.dto.DictInsertCmd;
import com.taotao.cloud.sys.application.command.dict.dto.DictListQry;
import com.taotao.cloud.sys.application.command.dict.dto.DictOptionListQry;
import com.taotao.cloud.sys.application.command.dict.dto.DictUpdateCmd;
import com.taotao.cloud.sys.application.command.dict.dto.clientobject.DictCO;
import com.taotao.cloud.sys.application.command.dict.dto.clientobject.OptionCO;
import java.util.List;

/**
 * 字典管理.
 */
public interface DictsService {



	/**
	 * 新增字典.
	 * @param cmd 新增字典参数
	 * @return 新增结果
	 */
	Boolean insert(DictInsertCmd cmd);

	/**
	 * 修改字典.
	 * @param cmd 修改字典参数
	 * @return 修改结果
	 */
	Boolean update(DictUpdateCmd cmd);

	/**
	 * 根据ID删除字典.
	 * @param cmd 根据ID删除字典参数
	 * @return 删除结果
	 */
	Boolean deleteById(DictDeleteCmd cmd);

	/**
	 * 根据ID查看字典.
	 * @param qry 根据ID查看字典参数
	 * @return 字典
	 */
	DictCO getById(DictGetQry qry);

	/**
	 * 查询字典下拉框选择项列表.
	 * @param qry 查询字典下拉框选择项列表参数
	 * @return 字典下拉框选择项列表
	 */
	List<OptionCO> optionList(DictOptionListQry qry);

	/**
	 * 查询字典列表.
	 * @param qry 查询字典列表参数
	 * @return 字典列表
	 */
	IPage<DictCO> list(DictListQry qry);

}
