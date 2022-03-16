package com.taotao.cloud.store.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.store.biz.entity.StoreLogistics;
import java.util.List;

/**
 * 店铺-物流公司业务层
 */
public interface StoreLogisticsService extends IService<StoreLogistics> {

	/**
	 * 获取当前店铺的物流公司列表
	 *
	 * @param storeId 店铺id
	 * @return 物流公司列表
	 */
	List<StoreLogisticsVO> getStoreLogistics(String storeId);

	/**
	 * 获取当前店铺已选择的物流公司列表
	 *
	 * @param storeId 店铺id
	 * @return 物流公司列表
	 */
	List<StoreLogisticsVO> getStoreSelectedLogistics(String storeId);

	/**
	 * 获取当前店铺已选择的物流公司名称列表
	 *
	 * @param storeId 店铺id
	 * @return 物流公司列表
	 */
	List<String> getStoreSelectedLogisticsName(String storeId);

	/**
	 * 添加店铺-物流公司
	 *
	 * @param logisticsId 物流公司设置id
	 * @param storeId     店铺id
	 * @return 店铺物流公司
	 */
	StoreLogistics add(String logisticsId, String storeId);


}
