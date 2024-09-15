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

package com.taotao.cloud.sys.infrastructure.persistent.dept.repository.inf;

import com.taotao.cloud.sys.infrastructure.persistent.dept.po.DeptPO;
import com.taotao.boot.web.base.repository.BaseInterfaceSuperRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * IDeptRepository
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/13 22:50
 */
public interface IDeptRepository extends BaseInterfaceSuperRepository<DeptPO, Long> {

	@Query("select d from DeptPO d where d.version <> ?1")
	DeptPO findByVersionNot(Integer version);

	@Query(
		value = """
			select h from deptPO h where
			h.userId=:#{#deptPO.userId} and
			h.tabName=:#{#deptPO.tabName} and
			h.headerName=:#{#deptPO.headerName}
			""", nativeQuery = true)
	DeptPO find(@Param("deptPO") DeptPO deptPO);
	//
	// @Query(value = """
	//	  select m.* from mining_area as m
	//			where 1 = 1
	//			and if(:name != '' , m.name like :name , 1 = 1)
	//			and if(IFNULL(:startDate, '') != '' , DATE(m.create_time) > DATE(:startDate) , 1 = 1 )
	//			and if(IFNULL(:endDate, '') != '' , DATE(m.create_time) < DATE(:endDate) , 1 = 1 )
	//			and if(IFNULL(:status, '') != '' , m.audit_status = :status , 1 = 1 )
	//			and m.deleted = :deleted
	//			and ( m.tree_code like :treeCode and m.audit_status = 3  )
	//			or ( m.tree_code = :noLikeTreeCode  )
	//	""",
	//	countQuery = """
	//		   select count(m.*) from mining_area as m
	//					where 1 = 1
	//					and if(:name != '' , m.name like :name , 1 = 1)
	//					and if(IFNULL(:startDate, '') != '' , DATE(m.create_time) > DATE(:startDate) , 1 = 1 )
	//					and if(IFNULL(:endDate, '') != '' , DATE(m.create_time) < DATE(:endDate) , 1 = 1 )
	//					and if(IFNULL(:status, '') != '' , m.audit_status = :status , 1 = 1 )
	//					and m.deleted = :deleted
	//					and ( m.tree_code like :treeCode and m.audit_status = 3  )
	//					or ( m.tree_code = :noLikeTreeCode  )
	//		""",
	//	nativeQuery = true)
	// List<Dept> findPage(String name, Date startDate, Date endDate, Dept status,
	//	String treeCode, String noLikeTreeCode, Integer deleted, Pageable pageable);
}
