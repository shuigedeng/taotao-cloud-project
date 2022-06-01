package com.taotao.cloud.store.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.store.api.dto.AdminStoreApplyDTO;
import com.taotao.cloud.store.api.dto.CollectionDTO;
import com.taotao.cloud.store.api.dto.StoreBankDTO;
import com.taotao.cloud.store.api.dto.StoreCompanyDTO;
import com.taotao.cloud.store.api.dto.StoreEditDTO;
import com.taotao.cloud.store.api.dto.StoreOtherInfoDTO;
import com.taotao.cloud.store.api.query.StorePageQuery;
import com.taotao.cloud.store.api.vo.StoreVO;
import com.taotao.cloud.store.biz.entity.Store;

/**
 * 店铺业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-01 15:01:47
 */
public interface StoreService extends IService<Store> {

	/**
	 * 分页条件查询
	 * 用于展示店铺列表
	 *
	 * @param storePageQuery 存储页面查询
	 * @return {@link IPage }<{@link StoreVO }>
	 * @since 2022-06-01 15:01:47
	 */
	IPage<StoreVO> findByConditionPage(StorePageQuery storePageQuery);

	/**
	 * 获取当前登录店铺信息
	 *
	 * @return {@link StoreVO }
	 * @since 2022-06-01 15:01:47
	 */
	StoreVO getStoreDetail();

	/**
	 * 增加店铺
	 * 用于后台添加店铺
	 *
	 * @param adminStoreApplyDTO 后台添加店铺信息
	 * @return {@link Store }
	 * @since 2022-06-01 15:01:47
	 */
	Store add(AdminStoreApplyDTO adminStoreApplyDTO);

	/**
	 * 编辑店铺
	 *
	 * @param storeEditDTO 店铺修改信息
	 * @return {@link Store }
	 * @since 2022-06-01 15:01:47
	 */
	Store edit(StoreEditDTO storeEditDTO);

	/**
	 * 审核店铺
	 *
	 * @param id     店铺ID
	 * @param passed 审核结果
	 * @return boolean
	 * @since 2022-06-01 15:01:47
	 */
	boolean audit(String id, Integer passed);

	/**
	 * 关闭店铺
	 *
	 * @param id 店铺ID
	 * @return boolean
	 * @since 2022-06-01 15:01:47
	 */
	boolean disable(String id);

	/**
	 * 开启店铺
	 *
	 * @param id 店铺ID
	 * @return boolean
	 * @since 2022-06-01 15:01:47
	 */
	boolean enable(String id);

	/**
	 * 申请店铺第一步
	 * 设置店铺公司信息，如果没有店铺新建店铺
	 *
	 * @param storeCompanyDTO 店铺公司信息
	 * @return boolean
	 * @since 2022-06-01 15:01:47
	 */
	boolean applyFirstStep(StoreCompanyDTO storeCompanyDTO);

	/**
	 * 申请店铺第二步
	 *
	 * @param storeBankDTO 店铺银行信息
	 * @return boolean
	 * @since 2022-06-01 15:01:47
	 */
	boolean applySecondStep(StoreBankDTO storeBankDTO);

	/**
	 * 申请店铺第三步
	 * 设置店铺信息，经营范围
	 *
	 * @param storeOtherInfoDTO 店铺其他信息
	 * @return boolean
	 * @since 2022-06-01 15:01:47
	 */
	boolean applyThirdStep(StoreOtherInfoDTO storeOtherInfoDTO);

	/**
	 * 更新店铺商品数量
	 *
	 * @param storeId 店铺ID
	 * @since 2022-06-01 15:01:47
	 */
	void updateStoreGoodsNum(Long storeId);

	/**
	 * 更新店铺收藏数量
	 *
	 * @param collectionDTO 收藏信息
	 * @since 2022-06-01 15:01:47
	 */
	void updateStoreCollectionNum(CollectionDTO collectionDTO);
}
