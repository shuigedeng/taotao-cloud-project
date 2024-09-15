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

package com.taotao.cloud.goods.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.goods.biz.model.vo.GoodsSkuParamsVO;
import com.taotao.cloud.goods.infrastructure.persistent.po.GoodsPO;
import com.taotao.boot.web.base.mapper.BaseSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 规格项数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:56:00
 */
//添加@Repository注解是为了引导IntelliJ IDEA作出正确的判断.
@Repository
public interface IGoodsMapper extends BaseSuperMapper<GoodsPO, Long> {

	/**
	 * 根据店铺ID获取商品ID列表
	 *
	 * @param storeId 店铺ID
	 * @return {@link List }<{@link Long }>
	 * @since 2022-04-27 16:56:00
	 */
	@Select("""
		SELECT id
		FROM tt_goods
		WHERE store_id = #{storeId}
		""")
	List<Long> getGoodsIdByStoreId(@Param("storeId") Long storeId);

	/**
	 * 添加商品评价数量
	 *
	 * @param commentNum 评价数量
	 * @param goodsId    商品ID
	 * @since 2022-04-27 16:56:00
	 */
	@Update("""
		UPDATE tt_goods
		SET comment_num = comment_num + #{commentNum}
		WHERE id = #{goodsId}
		""")
	void addGoodsCommentNum(@Param("commentNum") Integer commentNum,
		@Param("goodsId") Long goodsId);

	/**
	 * 查询商品VO分页
	 *
	 * @param page         分页
	 * @param queryWrapper 查询条件
	 * @return {@link IPage }<{@link GoodsSkuParamsVO }>
	 * @since 2022-04-27 16:56:00
	 */
	@Select("""
		select g.*
		from tt_goods as g
		""")
	IPage<GoodsSkuParamsVO> queryByParams(
		IPage<GoodsSkuParamsVO> page,
		@Param(Constants.WRAPPER) Wrapper<GoodsSkuParamsVO> queryWrapper);
}
