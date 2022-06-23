package com.taotao.cloud.store.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.store.biz.model.entity.StoreLogistics;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 物流公司数据处理层
 */
public interface StoreLogisticsMapper extends BaseMapper<StoreLogistics> {

	/**
	 * 获取店铺选择的物流公司
	 *
	 * @param storeId 店铺ID
	 * @return 物流公司列表
	 */
	@Select("SELECT l.* FROM tt_logistics l RIGHT JOIN  tt_store_logistics sl ON l.id=sl.logistics_id WHERE sl.store_id=#{storeId} AND l.disabled='OPEN'")
	List<StoreLogisticsVO> getSelectedStoreLogistics(String storeId);

	/**
	 * 店铺已选择的物流公司名称列表
	 *
	 * @param storeId 店铺ID
	 * @return 店铺已选择的物流公司名称列表
	 */
	@Select("SELECT l.name FROM tt_logistics l RIGHT JOIN  tt_store_logistics sl ON l.id=sl.logistics_id WHERE sl.store_id=#{storeId} AND l.disabled='OPEN'")
	List<String> getSelectedStoreLogisticsName(String storeId);

	/**
	 * 获取店铺地址VO列表
	 *
	 * @param storeId 店铺列表
	 * @return 店铺地址VO列表
	 */
	@Select("SELECT *, ( SELECT sl.id FROM tt_store_logistics sl WHERE l.id = sl.logistics_id AND sl.store_id=#{storeId} ) AS selected FROM tt_logistics l WHERE l.disabled='OPEN';")
	List<StoreLogisticsVO> getStoreLogistics(String storeId);

}
