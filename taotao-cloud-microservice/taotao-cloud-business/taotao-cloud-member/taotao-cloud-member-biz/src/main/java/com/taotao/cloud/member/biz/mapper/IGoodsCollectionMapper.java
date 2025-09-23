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
import com.taotao.cloud.member.sys.model.vo.GoodsCollectionVO;
import com.taotao.cloud.member.biz.model.entity.MemberGoodsCollection;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 会员收藏数据处理层 */
public interface IGoodsCollectionMapper extends MpSuperMapper<MemberGoodsCollection, Long> {

    /**
     * 商品收藏VO分页
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 商品收藏VO分页
     */
    @Select(
            """
		select gc.id AS id,gs.id as sku_id,gs.goods_id as goods_id,gs.goods_name as goods_name,gs.thumbnail as image,gs.price,gs.market_enable AS market_enable
		from tt_goods_collection gc INNER JOIN tt_goods_sku gs ON gc.sku_id=gs.id
		${ew.customSqlSegment}
		""")
    IPage<GoodsCollectionVO> goodsCollectionVOList(
            IPage<GoodsCollectionVO> page, @Param(Constants.WRAPPER) Wrapper<GoodsCollectionVO> queryWrapper);
}
