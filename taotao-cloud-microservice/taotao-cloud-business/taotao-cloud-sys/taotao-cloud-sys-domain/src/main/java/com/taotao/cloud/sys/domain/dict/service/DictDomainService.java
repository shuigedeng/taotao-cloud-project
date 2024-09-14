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

package com.taotao.cloud.sys.domain.dict.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.PageQuery;
import com.taotao.cloud.sys.domain.dict.entity.DictEntity;

public interface DictDomainService {

	/**
	 * 新增字典.
	 *
	 * @param dictEntity 字典对象
	 * @return 新增结果
	 */
	Boolean insert(DictEntity dictEntity);

	/**
	 * 修改字典.
	 *
	 * @param dictEntity 字典对象
	 * @return 修改结果
	 */
	Boolean update(DictEntity dictEntity);

	/**
	 * 根据ID查看字典.
	 *
	 * @param id ID
	 * @return 字典
	 */
	DictEntity getById(Long id);

	/**
	 * 根据ID删除字典.
	 *
	 * @param id ID
	 * @return 删除结果
	 */
	Boolean deleteById(Long id);

	/**
	 * 查询字典列表.
	 *
	 * @param dictEntity      字典对象
	 * @param pageQuery 分页参数
	 * @return 字典列表
	 */
	IPage<DictEntity> list(DictEntity dictEntity, PageQuery pageQuery);

}
