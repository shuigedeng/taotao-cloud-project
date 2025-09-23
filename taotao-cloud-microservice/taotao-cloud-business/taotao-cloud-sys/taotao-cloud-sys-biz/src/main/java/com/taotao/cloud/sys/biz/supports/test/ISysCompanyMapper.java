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

package com.taotao.cloud.sys.biz.supports.test; /// *
// * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
// package com.taotao.cloud.member.biz.mapper;
//
// import com.baomidou.mybatisplus.core.conditions.Wrapper;
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.baomidou.mybatisplus.core.toolkit.Constants;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.taotao.cloud.sys.biz.entity.SysCompany;
// import com.taotao.cloud.sys.biz.entity.User;
// import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
// import java.time.LocalDate;
// import java.util.List;
// import org.apache.ibatis.annotations.Many;
// import org.apache.ibatis.annotations.Mapper;
// import org.apache.ibatis.annotations.Param;
// import org.apache.ibatis.annotations.Result;
// import org.apache.ibatis.annotations.Results;
// import org.apache.ibatis.annotations.Select;
// import org.apache.ibatis.annotations.Update;
//
/// **
// * CompanyMapper
// *
// * @author shuigedeng
// * @version 2022.03
// * @since 2021/10/13 22:50
// */
// @Mapper
// public interface ISysCompanyMapper extends MpSuperMapper<SysCompany, Long> {
//
//	@Select("""
//		select t.id,t.login_name,t.name,b.name as login_station_name
//		from t_user t
//		left join t_remote_station b on t.login_station_id=b.id
//		WHERE 1=1
//		and if(#{id} is null,1=1,t.id=#{id})
//		and if(#{name} is null,1=1,t.name like CONCAT('%',#{name},'%'))
//		""")
//	List<User> selectUserListByPage(Page<User> page, @Param("id") Long id,
//		@Param("name") String name);
//
//	@Update("""
//		<script>
//		update radius.t_user_plan
//		<set>
//		<if test='plan.state != null'>
//			state = #{plan.state}
//		</if>
//		<if test='plan.effectiveDate != null'>
//			effective_date=#{plan.effectiveDate},
//		</if>
//		<if test='plan.expireDate != null'>
//			expire_date=#{plan.expireDate},
//		</if>
//		<if test='plan.dataLimit != null'>
//			data_limit=#{plan.dataLimit},
//		</if>
//		</set>
//		where user_plan_id in
//		<foreach collection='ids' item='id' open='(' separator=',' close=')'>
//		#{id}
//		</foreach>
//		and state = 0
//		and (YEAR(effective_date)=YEAR(CURTIME()) and MONTH(effective_date)=MONTH(CURTIME()))
//		</script>
//		""")
//	int updateUserByIds(@Param("ids") List<Long> ids, @Param("plan") User plan);
//
//	// 根据id集合查询所有对象
//	@Select("<script>" +
//		" select t.* from t_user t " +
//		" where t.id in " +
//		" <foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
//		" #{id}" +
//		" </foreach>" +
//		" </script>")
//	List<User> selectAllUserbyIds(@Param("ids") List<String> ids);
//
//	@Select("<script>" +
//		" select t.* from t_user_plan t" +
//		" where t.type=0" +
//		" <if test='startTime != null'>" +
//		" AND t.effective_date >= #{startTime} " +
//		" </if>" +
//		" <if test='endTime != null'>" +
//		" AND t.effective_date &lt;= #{endTime} " +
//		" </if>" +
//		" </script>"
//	)
//	IPage<User> selectUserPlanByPage(Page<User> page,
//		@Param("startTime") LocalDate startTime,
//		@Param("endTime") LocalDate endTime);
//
//	@Select("""
//		<script>
//		select t.* from t_log t
//		<where>
//		<if test='typeName!= null'>
//		and t.type_name like CONCAT('%',#{typeName},'%')
//		</if>
//		<if test='typeCode!= null'>
//		and t.type_code like CONCAT('%',#{typeCode},'%')
//		</if>
//		</where>
//		</script>
//		""")
//	IPage<User> selectLogByPage(Page<User> page, @Param("typeName") String typeName,
//		@Param("typeCode") String typeCode);
//
//	// 分页查询
//	@Select("""
//		<script>
//		select t.*,a.name_cn as company_name
//		from t_company t
//		join t_customer_company a on t.company_id=a.id
//		where <![CDATA[t.status <> 2]]>
//		<if test='nameCn != null and nameCn.trim() != &quot;&quot;'>
//		AND t.name_cn like CONCAT('%',#{nameCn},'%')
//		</if>
//		</script>
//		""")
//	IPage<User> selectCompanybyPage(Page<User> page,
//		@Param("nameCn") String nameCn);
//
//	@Select(value = "SELECT * FROM tableA a LEFT JOIN tableB b on a.key = b.key
// ${ew.customSqlSegment}")
//	List method1(@Param(Constants.WRAPPER) QueryWrapper wrapper);
//
//	IPage method2(Page page, @Param(Constants.WRAPPER) QueryWrapper wrapper);
//
//	/**
//	 * 通过id查询用户的全部信息
//	 *
//	 * @param id 用户id
//	 * @return 用户信息模型
//	 */
//	@Select({"""
//		select * from user where id = #{id}
//		"""})
//	@Results(id = "studentMap", value = {
//		@Result(column = "id", property = "id", id = true),
//		@Result(column = "username", property = "username"),
//		@Result(column = "name", property = "name"),
//		@Result(property = "roleList", javaType = List.class, column = "id",
//			many = @Many(select = "com.phj.rbacdome.dao.RoleMapper.getRoleListById"))
//	})
//	public User getUserById(@Param("id") Integer id);
//
//	@Select("select res.* ,tree.SORT from TS_CUR_RESOURCE  res " +
//		"left join TR_CUR_TREE tree on res.ID = tree.QUESTION_RESOURCE_ID ${ew.customSqlSegment}")
//	List<User> selectResourceByIds(@Param(Constants.WRAPPER) Wrapper<User> wrapper);
//
// }
