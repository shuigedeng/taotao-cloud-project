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
package com.taotao.cloud.uc.biz.repository.impl;

import com.taotao.cloud.uc.biz.entity.QSysUser;
import com.taotao.cloud.uc.biz.entity.SysUser;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * SysUserRepository
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:51:18
 */
@Repository
public class SysUserRepository extends BaseSuperRepository<SysUser, Long> {

	public SysUserRepository(EntityManager em) {
		super(SysUser.class, em);
	}

	private final static QSysUser SYS_USER = QSysUser.sysUser;

	/**
	 * updatePassword
	 *
	 * @param id          id
	 * @param newPassword newPassword
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-09 20:51:23
	 */
	public Boolean updatePassword(Long id, String newPassword) {
		return jpaQueryFactory()
			.update(SYS_USER)
			.set(SYS_USER.password, newPassword)
			.where(SYS_USER.id.eq(id))
			.execute() > 0;
	}

	//@Query(value = """
	//	select u from User u where u.age = ?#{[0]}
	//	""")
	//abstract List<SysUser> findUsersByAge(int age);

	//@Query(value = """
	//	select u from User u where u.firstname = :#{#customer.firstname
	//	""")
	//abstract List<SysUser> findUsersByCustomersFirstname(@Param("customer") Customer customer);

	//@Query(value = """
	//	      select o from BusinessObject as o
	//	      where o.owner.emailAddress like ?#{hasRole('ROLE_ADMIN') ? '%' : principal.emailAddress}
	//	""")
	//abstract List<SysUser> findBusinessObjectsForCurrentUser();
	//
	//@Query(value = """
	//	select u from User u where u.emailAddress = ?#{principal.emailAddress}
	//	""")
	//abstract List<SysUser> findCurrentUserWithCustomQuery();
	//
	//@Query(value = """
	//	select u.id, u.name from SysUser u where u.id=?1
	//	""")
	//abstract List<SysUser> getUserById(Integer id);

	//@Query(value = """
	//	select u.id, u.name from SysUser u where u.id=?1
	//	""", nativeQuery = true)
	//abstract List<SysUser> getUserById(Integer id);
	//
	//@Query(value = """
	//	     select u.id, u.name from  #{#entityName} u where u.id=?1
	//	""", nativeQuery = true)
	//abstract List<SysUser> getUserById(Integer id);

	//@Query(
	//	value = """
	//		SELECT * FROM SysUser ORDER BY id
	//		""",
	//	countQuery = """
	//		SELECT count(*) FROM SysUser
	//		""",
	//	nativeQuery = true)
	//abstract Page<SysUser> findAllUsersWithPagination(Pageable pageable);
	//
	//@Modifying
	//@Query(value = """
	//	insert into SysUser (name, age, email, status) values (:name, :age, :email, :status)
	//	""", nativeQuery = true)
	//abstract void insertUser(@Param("name") String name,
	//	@Param("age") Integer age,
	//	@Param("status") Integer status,
	//	@Param("email") String email);
	//
	//@Modifying
	//@Query(value = """
	//	update SysUser u set u.name = :name where u.id = :id
	//	""")
	//abstract boolean update(@Param("id") Integer id, @Param("name") String name);
	//
	//@Modifying
	//@Query(value = """
	//	delete from  SysUser u where u.id = :id
	//	""")
	//abstract void update(@Param("id") Integer id);
	//
	//@Query(value = """
	//	select t from Task t where t.taskName = ? and t.createTime = ?
	//	""")
	//abstract SysUser findByTaskName(String taskName, Date createTime);
	//
	//@Query(value = """
	//	select * from SysUser t where t.task_name = ?1
	//	""", nativeQuery = true)
	//abstract SysUser findByTaskName(String taskName);
	//
	////@Query(value = """
	////	select new com.ljw.test.pojo.domain.UserDept(u.id, u.name, d.id, d.name )
	////	from User u, Dept d
	////	where u.deptId=d.id
	////	""")
	////abstract List<UserDept> findAllForUserDept();
	//
	//@Query(value = """
	//	select new map(u.id as user_id, u.name as user_name, d.id as dept_id, d.name as dept_name)
	//	from SysUser as u, SysDept as d
	//	where u.deptId=d.id
	//	""")
	//abstract List<Map<String, Object>> findAllForMap();
}
