package com.taotao.cloud.order.biz.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.api.dto.order.StoreFlowQueryDTO;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSale;
import com.taotao.cloud.order.biz.entity.order.StoreFlow;
import com.taotao.cloud.store.api.vo.StoreFlowPayDownloadVO;
import com.taotao.cloud.store.api.vo.StoreFlowRefundDownloadVO;

import java.util.List;

/**
 * 商家订单流水业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:53
 */
public interface IStoreFlowService extends IService<StoreFlow> {

	/**
	 * 支付订单
	 *
	 * @param orderSn 订单编号
	 * @since 2022-04-28 08:54:53
	 */
	void payOrder(String orderSn);

	/**
	 * 订单退款
	 *
	 * @param afterSale 售后单
	 * @since 2022-04-28 08:54:53
	 */
	void refundOrder(AfterSale afterSale);

	/**
	 * 获取商家流水
	 *
	 * @param storeFlowQueryDTO 查询参数
	 * @return {@link IPage }<{@link StoreFlow }>
	 * @since 2022-04-28 08:54:53
	 */
	IPage<StoreFlow> getStoreFlow(StoreFlowQueryDTO storeFlowQueryDTO);

	/**
	 * 根据参数查询一条数据
	 *
	 * @param storeFlowQueryDTO 查询参数
	 * @return {@link StoreFlow }
	 * @since 2022-04-28 08:54:53
	 */
	StoreFlow queryOne(StoreFlowQueryDTO storeFlowQueryDTO);

	/**
	 * 获取结算单地入账流水
	 *
	 * @param storeFlowQueryDTO 查询条件
	 * @return {@link List }<{@link StoreFlowPayDownloadVO }>
	 * @since 2022-04-28 08:54:53
	 */
	List<StoreFlowPayDownloadVO> getStoreFlowPayDownloadVO(StoreFlowQueryDTO storeFlowQueryDTO);

	/**
	 * 获取结算单的退款流水
	 *
	 * @param storeFlowQueryDTO 查询条件
	 * @return {@link List }<{@link StoreFlowRefundDownloadVO }>
	 * @since 2022-04-28 08:54:53
	 */
	List<StoreFlowRefundDownloadVO> getStoreFlowRefundDownloadVO(
		StoreFlowQueryDTO storeFlowQueryDTO);


	/**
	 * 根据结算单ID获取商家流水
	 *
	 * @param id     结算单ID
	 * @param type   类型
	 * @param pageVO 分页
	 * @return {@link IPage }<{@link StoreFlow }>
	 * @since 2022-04-28 08:54:53
	 */
	IPage<StoreFlow> getStoreFlow(String id, String type, PageVO pageVO);

	/**
	 * 根据结算单ID获取商家流水
	 *
	 * @param id     结算单ID
	 * @param pageVO 分页
	 * @return {@link IPage }<{@link StoreFlow }>
	 * @since 2022-04-28 08:54:53
	 */
	IPage<StoreFlow> getDistributionFlow(String id, PageVO pageVO);


	/**
	 * 获取店铺流水
	 *
	 * @param storeFlowQueryDTO 店铺流水查询参数
	 * @return {@link List }<{@link StoreFlow }>
	 * @since 2022-04-28 08:54:53
	 */
	List<StoreFlow> listStoreFlow(StoreFlowQueryDTO storeFlowQueryDTO);
}
