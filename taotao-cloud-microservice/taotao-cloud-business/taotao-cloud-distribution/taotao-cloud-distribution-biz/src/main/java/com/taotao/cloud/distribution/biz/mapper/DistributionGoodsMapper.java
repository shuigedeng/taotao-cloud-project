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

package com.taotao.cloud.distribution.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.distribution.api.model.vo.DistributionGoodsVO;
import com.taotao.cloud.distribution.biz.model.entity.DistributionGoods;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 分销商品数据处理层 */
public interface DistributionGoodsMapper extends MpSuperMapper<DistributionGoods, Long> {

    /**
     * 获取分销员未选择商品VO分页
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param distributionId 分销员ID
     * @return 分销员未选择商品VO分页
     */
    @Select(
            """
		SELECT dg.*
		FROM tt_distribution_goods dg
		WHERE dg.id NOT IN(SELECT distribution_goods_id
		 					FROM tt_distribution_selected_goods
		  					WHERE distribution_id=${distributionId}
		  				 )
		${ew.customSqlSegment}
		""")
    IPage<DistributionGoodsVO> notSelectGoods(
            IPage<DistributionGoodsVO> page,
            @Param(Constants.WRAPPER) Wrapper<DistributionGoodsVO> queryWrapper,
            String distributionId);

    /**
     * 获取分销员已选择分销商品VO分页
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param distributionId 分销员ID
     * @return 分销员已选择分销商品VO分页
     */
    @Select(
            """
		SELECT dg.*
		FROM tt_distribution_goods dg
		WHERE dg.id IN (SELECT distribution_goods_id
						FROM tt_distribution_selected_goods
						WHERE distribution_id=${distributionId}
					)
		 ${ew.customSqlSegment}
		""")
    IPage<DistributionGoodsVO> selectGoods(
            IPage<DistributionGoodsVO> page,
            @Param(Constants.WRAPPER) Wrapper<DistributionGoodsVO> queryWrapper,
            String distributionId);

    /**
     * 获取分销商品VO分页
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 分销商品VO分页
     */
    @Select("""
		SELECT dg.*
		FROM tt_distribution_goods dg
		${ew.customSqlSegment}
		""")
    IPage<DistributionGoodsVO> getDistributionGoodsVO(
            IPage<DistributionGoodsVO> page, @Param(Constants.WRAPPER) Wrapper<DistributionGoodsVO> queryWrapper);
}
