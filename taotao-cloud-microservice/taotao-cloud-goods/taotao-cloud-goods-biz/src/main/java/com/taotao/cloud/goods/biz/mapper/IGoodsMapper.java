package com.taotao.cloud.goods.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.goods.api.vo.GoodsVO;
import com.taotao.cloud.goods.biz.entity.Goods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 规格项数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:56:00
 */
public interface IGoodsMapper extends BaseMapper<Goods> {

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
	 * @return {@link IPage }<{@link GoodsVO }>
	 * @since 2022-04-27 16:56:00
	 */
	@Select("""
		select g.*
		from tt_goods as g
		""")
	IPage<GoodsVO> queryByParams(IPage<GoodsVO> page,
		@Param(Constants.WRAPPER) Wrapper<GoodsVO> queryWrapper);
}
