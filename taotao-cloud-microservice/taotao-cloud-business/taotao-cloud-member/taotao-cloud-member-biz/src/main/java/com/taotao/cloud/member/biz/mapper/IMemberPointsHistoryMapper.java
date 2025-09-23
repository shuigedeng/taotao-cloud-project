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

import com.taotao.cloud.member.biz.model.entity.MemberPointsHistory;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import org.apache.ibatis.annotations.Select;

/** 会员积分历史数据处理层 */
public interface IMemberPointsHistoryMapper extends MpSuperMapper<MemberPointsHistory, Long> {

    /**
     * 获取所有用户的积分历史VO
     *
     * @param pointType 积分类型
     * @return 积分
     */
    @Select("""
		SELECT SUM( variable_point )
		FROM tt_member_points_history
		WHERE point_type = #{pointType}
		""")
    Long getALLMemberPointsHistoryVO(String pointType);

    /**
     * 获取用户的积分数量
     *
     * @param pointType 积分类型
     * @param memberId 会员ID
     * @return 积分数量
     */
    @Select(
            """
		SELECT SUM( variable_point )
		FROM tt_member_points_history
		WHERE point_type = #{pointType} AND member_id=#{memberId}
		""")
    Long getMemberPointsHistoryVO(String pointType, String memberId);
}
