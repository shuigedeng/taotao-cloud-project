package com.taotao.cloud.goods.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.goods.biz.entity.GoodsSku;
import java.util.List;
import org.apache.ibatis.annotations.Select;

/**
 * 规格项数据处理层
 */
public interface GoodsSkuMapper extends BaseMapper<GoodsSku> {

	/**
	 * 根据商品id获取全部skuId的集合
	 *
	 * @param goodsId goodsId
	 * @return 全部skuId的集合
	 */
	@Select("SELECT id FROM li_goods_sku WHERE goods_id = #{goodsId}")
	List<String> getGoodsSkuIdByGoodsId(String goodsId);

}
