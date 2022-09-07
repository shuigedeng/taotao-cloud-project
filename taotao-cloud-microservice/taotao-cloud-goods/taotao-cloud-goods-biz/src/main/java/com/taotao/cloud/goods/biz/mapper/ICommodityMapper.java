package com.taotao.cloud.goods.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.goods.api.model.vo.CommoditySkuVO;
import com.taotao.cloud.goods.biz.model.entity.Commodity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 直播商品数据层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:55:41
 */
public interface ICommodityMapper extends BaseSuperMapper<Commodity> {

	/**
	 * 获取直播商品ID列表
	 *
	 * @return {@link List }<{@link String }>
	 * @since 2022-04-27 16:55:41
	 */
	@Select("""
		SELECT live_goods_id
		FROM tt_commodity
		WHERE audit_status='0' or audit_status='1'
		""")
	List<String> getAuditCommodity();

	/**
	 * 获取直播间关联直播商品列表
	 *
	 * @param roomId 直播间ID
	 * @return {@link List }<{@link Commodity }>
	 * @since 2022-04-27 16:55:41
	 */
	@Select("""
		SELECT *
		FROM tt_commodity c INNER JOIN tt_studio_commodity sc ON sc.goods_id = c.live_goods_id
		WHERE sc.room_id =#{roomId}
		""")
	List<Commodity> getCommodityByRoomId(Integer roomId);

	/**
	 * 获取直播商品图片列表
	 *
	 * @param roomId 直播间ID
	 * @return {@link List }<{@link String }>
	 * @since 2022-04-27 16:55:41
	 */
	@Select("""
		SELECT goods_image
		FROM tt_commodity c INNER JOIN tt_studio_commodity sc ON sc.goods_id = c.live_goods_id
		WHERE sc.room_id =#{roomId}
		""")
	List<String> getSimpleCommodityByRoomId(Integer roomId);

	/**
	 * 获取直播商品VO分页
	 *
	 * @param page         分页
	 * @param queryWrapper 查询条件
	 * @return {@link IPage }<{@link CommodityGoodsVO }>
	 * @since 2022-04-27 16:55:41
	 */
	@Select("""
		SELECT c.*,gs.quantity,s.store_name 
		FROM tt_commodity c INNER JOIN tt_goods_sku gs ON c.sku_id = gs.id INNER JOIN tt_store s ON s.id=c.store_id 
		${ew.customSqlSegment}
		""")
	IPage<CommoditySkuVO> commodityVOList(IPage<CommoditySkuVO> page,
                                          @Param(Constants.WRAPPER) Wrapper<CommoditySkuVO> queryWrapper);

}
