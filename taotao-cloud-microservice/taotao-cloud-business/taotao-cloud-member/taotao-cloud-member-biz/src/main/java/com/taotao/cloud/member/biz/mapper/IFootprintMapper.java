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

package com.taotao.cloud.member.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import com.taotao.cloud.member.biz.model.entity.MemberBrowse;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 浏览历史数据处理层 */
public interface IFootprintMapper extends MpSuperMapper<MemberBrowse, Long> {

    /**
     * 获取用户足迹的SkuId分页
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 用户足迹的SkuId分页
     */
    @Select("""
		select sku_id
		from tt_foot_print
		${ew.customSqlSegment}
		""")
    List<String> footprintSkuIdList(IPage<String> page, @Param(Constants.WRAPPER) Wrapper<MemberBrowse> queryWrapper);

    /**
     * 删除超过100条后的记录
     *
     * @param memberId 会员ID
     */
    @Delete(
            """
		DELETE
		FROM tt_foot_print
		WHERE (SELECT COUNT(b.id)
		       FROM (SELECT *
		             FROM tt_foot_print
		             WHERE member_id = #{memberId} ) b) >100
		               AND id = (SELECT a.id
		                         FROM (SELECT *
		                               FROM tt_foot_print
		                               WHERE member_id = #{memberId} ORDER BY create_time ASC LIMIT 1 ) AS a
		                              )
			""")
    void deleteLastFootPrint(Long memberId);
}
