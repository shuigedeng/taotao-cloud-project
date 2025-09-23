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
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.member.biz.model.entity.MemberSign;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 会员签到数据处理层 */
public interface IMemberSignMapper extends MpSuperMapper<MemberSign, Long> {

    /**
     * 获取会员之前签到信息
     *
     * @param memberId 会员ID
     * @return 会员签到列表
     */
    @Select("""
		SELECT *
		FROM tt_member_sign
		WHERE TO_DAYS( NOW( ) ) - TO_DAYS( create_time) = 1 and member_id = #{memberId}
		""")
    List<MemberSign> getBeforeMemberSign(Long memberId);

    /**
     * 获取会员签到
     *
     * @param queryWrapper 查询条件
     * @return 会员签到列表
     */
    @Select("""
		select *
		from tt_member_sign
		${ew.customSqlSegment}
		""")
    List<MemberSign> getTodayMemberSign(@Param(Constants.WRAPPER) Wrapper<MemberSign> queryWrapper);

    /**
     * 获取当月的会员签到记录
     *
     * @param memberId 会员ID
     * @param time 时间
     * @return 会员签到列表
     */
    @Select("""
		SELECT *
		FROM tt_member_sign
		WHERE DATE_FORMAT(create_time,'%Y%m') = #{time} and member_id = #{memberId}
		""")
    List<MemberSign> getMonthMemberSign(Long memberId, String time);
}
