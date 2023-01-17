package com.taotao.cloud.store.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.store.biz.model.entity.StoreAddress;

/**
 * 店铺地址（自提点）详细业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-01 15:00:08
 */
public interface IStoreAddressService extends IService<StoreAddress> {

	/**
	 * 获取当前商家的自提点列表
	 *
	 * @param storeId   存储id
	 * @param PageQuery 分页
	 * @return {@link IPage }<{@link StoreAddress }>
	 * @since 2022-06-01 15:00:08
	 */
	IPage<StoreAddress> getStoreAddress(String storeId, PageQuery PageQuery);

	/**
	 * 添加商家自提点
	 *
	 * @param storeId      存储id
	 * @param storeAddress 自提点
	 * @return {@link StoreAddress }
	 * @since 2022-06-01 15:00:08
	 */
	StoreAddress addStoreAddress(String storeId, StoreAddress storeAddress);

	/**
	 * 修改商家自提点
	 *
	 * @param storeId      存储id
	 * @param storeAddress 自提点
	 * @return {@link StoreAddress }
	 * @since 2022-06-01 15:00:08
	 */
	StoreAddress editStoreAddress(String storeId, StoreAddress storeAddress);

	/**
	 * 删除商家自提点
	 *
	 * @param id 自提点ID
	 * @return {@link Boolean }
	 * @since 2022-06-01 15:00:08
	 */
	Boolean removeStoreAddress(String id);

}
