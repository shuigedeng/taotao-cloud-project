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
import com.taotao.cloud.member.sys.model.vo.StoreRatingVO;
import com.taotao.cloud.member.biz.model.entity.MemberEvaluation;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 会员商品评价数据处理层 */
public interface IMemberEvaluationMapper extends MpSuperMapper<MemberEvaluation, Long> {

    /**
     * 会员评价分页
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 会员评价分页
     */
    @Select("""
		select me.*
		from tt_member_evaluation as me
		${ew.customSqlSegment}
		""")
    IPage<MemberEvaluation> getMemberEvaluationList(
            IPage<MemberEvaluation> page, @Param(Constants.WRAPPER) Wrapper<MemberEvaluation> queryWrapper);

    /**
     * 评价数量
     *
     * @param goodsId 商品ID
     * @return 会员评价
     */
    @Select(
            """
		select grade,count(1) as num
		from tt_member_evaluation
		Where goods_id=#{goodsId} and status='OPEN'
		GROUP BY grade
		""")
    List<Map<String, Object>> getEvaluationNumber(Long goodsId);

    /**
     * 获取店铺评分
     *
     * @param queryWrapper 查询条件
     * @return 店铺评分
     */
    @Select(
            """
		SELECT round( AVG( delivery_score ), 2 ) AS delivery_score
				,round( AVG( description_score ), 2 ) AS description_score
				,round( AVG( service_score ), 2 ) AS service_score
		FROM tt_member_evaluation
		${ew.customSqlSegment}
		""")
    StoreRatingVO getStoreRatingVO(@Param(Constants.WRAPPER) Wrapper<MemberEvaluation> queryWrapper);

    /**
     * 商品会员评价数量
     *
     * @param queryWrapper 查询条件
     * @return 评价数量
     */
    @Select("""
		SELECT goods_id,COUNT(goods_id) AS num
		FROM tt_member_evaluation
		GROUP BY goods_id
		""")
    List<Map<String, Object>> memberEvaluationNum(@Param(Constants.WRAPPER) Wrapper<MemberEvaluation> queryWrapper);
}
