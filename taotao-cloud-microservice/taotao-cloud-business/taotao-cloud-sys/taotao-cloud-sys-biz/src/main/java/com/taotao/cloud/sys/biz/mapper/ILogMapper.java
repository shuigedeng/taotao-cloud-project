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

package com.taotao.cloud.sys.biz.mapper;

import com.taotao.boot.webagg.mapper.BaseSuperMapper;
import com.taotao.cloud.sys.biz.model.entity.Log;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ILogMapper
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-11 16:33:34
 */
public interface ILogMapper extends BaseSuperMapper<Log, Long> {

	@Delete("delete from log where log_type = #{logType}")
	void deleteByLogType(@Param("logType") String logType);

	@Select(
		"""
			<script>
			select l.id,
				   l.create_time as createTime,
				   l.description,
				   l.request_ip as requestIp,
				   l.address,u.nickname
			from log l left join yx_user u on u.uid=l.uid
			where l.type=1
			<if test ="nickname !=null">
				and u.nickname LIKE CONCAT('%',#{nickname},'%')
			</if>
			order by l.id desc
			</script>
			""")
	List<Log> findAllByPageable(@Param("nickname") String nickname);

	@Select(
		"""
			select count(*)
			FROM (select request_ip
						 FROM log
						 where create_time between #{date1} and #{date2}
						 GROUP BY request_ip
				 ) as s
			""")
	long findIp(@Param("date1") String date1, @Param("date2") String date2);
}
