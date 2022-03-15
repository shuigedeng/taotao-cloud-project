package com.taotao.cloud.goods.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.goods.api.vo.GoodsVO;
import com.taotao.cloud.goods.biz.entity.Goods;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 规格项数据处理层
 */
public interface GoodsMapper extends BaseMapper<Goods> {

	/**
	 * 根据店铺ID获取商品ID列表
	 *
	 * @param storeId 店铺ID
	 * @return 商品ID列表
	 */
	@Select("SELECT id FROM li_goods WHERE store_id = #{storeId}")
	List<String> getGoodsIdByStoreId(String storeId);

	/**
	 * 添加商品评价数量
	 *
	 * @param commentNum 评价数量
	 * @param goodsId    商品ID
	 */
	@Update("UPDATE li_goods SET comment_num = comment_num + #{commentNum} WHERE id = #{goodsId}")
	void addGoodsCommentNum(Integer commentNum, String goodsId);

	/**
	 * 查询商品VO分页
	 *
	 * @param page         分页
	 * @param queryWrapper 查询条件
	 * @return 商品VO分页
	 */
	@Select("select g.* from li_goods as g ")
	IPage<GoodsVO> queryByParams(IPage<GoodsVO> page,
		@Param(Constants.WRAPPER) Wrapper<GoodsVO> queryWrapper);
}
