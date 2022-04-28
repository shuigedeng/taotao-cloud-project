package com.taotao.cloud.goods.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.goods.biz.entity.GoodsSku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 规格项数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:57:22
 */
public interface IGoodsSkuMapper extends BaseMapper<GoodsSku> {

	/**
	 * 根据商品id获取全部skuId的集合
	 *
	 * @param goodsId goodsId
	 * @return {@link List }<{@link String }>
	 * @since 2022-04-27 16:57:22
	 */
	@Select("""
		SELECT id
		FROM li_goods_sku
		WHERE goods_id = #{goodsId}
		""")
	List<String> getGoodsSkuIdByGoodsId(@Param(value = "goodsId") Long goodsId);

}
