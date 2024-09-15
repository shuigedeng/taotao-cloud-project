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

package com.taotao.cloud.sys.infrastructure.persistent.dept.mapper;

import com.taotao.cloud.sys.infrastructure.persistent.dept.po.DeptPO;
import com.taotao.boot.web.base.mapper.BaseSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * CompanyMapper
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/13 22:50
 */
public interface DeptMapper extends BaseSuperMapper<DeptPO, Long> {

	/**
	 * 根据角色ID查看部门.
	 *
	 * @param roleId 角色ID
	 * @return 部门
	 */
	List<Long> selectIdsByRoleId(@Param("roleId") Long roleId);

	/**
	 * 根据部门父节点ID查看部门.
	 *
	 * @param id 部门ID
	 * @return 部门
	 */
	String selectPathById(@Param("id") Long id);

	Integer selectVersion(@Param("id") Long id);

	/**
	 * 根据PATH模糊查询部门子节点列表.
	 *
	 * @param path 部门PATH
	 * @return 部门子节点列表
	 */
	List<DeptPO> selectListByPath(@Param("path") String path);

}
