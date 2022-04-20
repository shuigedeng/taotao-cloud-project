package com.taotao.cloud.order.biz.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.api.dto.order.OrderComplaintDTO;
import com.taotao.cloud.order.api.dto.order.OrderComplaintOperationDTO;
import com.taotao.cloud.order.api.dto.order.OrderComplaintPageQuery;
import com.taotao.cloud.order.api.dto.order.StoreAppealDTO;
import com.taotao.cloud.order.api.vo.order.OrderComplaintVO;
import com.taotao.cloud.order.biz.entity.order.OrderComplaint;

/**
 * 交易投诉业务层
 **/
public interface OrderComplaintService extends IService<OrderComplaint> {

	/**
	 * 分页获取交易投诉信息
	 *
	 * @param searchParams 查询参数
	 * @param pageVO       分页参数
	 * @return 交易投诉信息
	 */
	IPage<OrderComplaint> getOrderComplainByPage(OrderComplaintPageQuery orderComplaintPageQuery);

	/**
	 * 获取交易投诉详情
	 *
	 * @param id 交易投诉ID
	 * @return 交易投诉详情
	 */
	OrderComplaintVO getOrderComplainById(Long id);

	/**
	 * 获取交易投诉详情
	 *
	 * @param storeId 店铺id
	 * @return 交易投诉详情
	 */
	OrderComplaint getOrderComplainByStoreId(Long storeId);

	/**
	 * 添加交易投诉
	 *
	 * @param orderComplaintDTO 交易投诉信息
	 * @return 添加结果
	 */
	OrderComplaint addOrderComplain(OrderComplaintDTO orderComplaintDTO);

	/**
	 * 更新交易投诉
	 *
	 * @param orderComplaint 交易投诉信息
	 * @return 更新结果
	 */
	Boolean updateOrderComplain(OrderComplaint orderComplaint);

	/**
	 * 修改交易投诉状态
	 *
	 * @param operationParam 操作参数
	 * @return 修改的交易投诉
	 */
	Boolean updateOrderComplainByStatus(OrderComplaintOperationDTO orderComplaintOperationDTO);

	/**
	 * 待处理投诉数量
	 *
	 * @return 待处理投诉数量
	 */
	long waitComplainNum();

	/**
	 * 取消交易投诉
	 *
	 * @param id 交易投诉ID
	 * @return 操作状态
	 */
	Boolean cancel(Long id);

	/**
	 * 店铺申诉
	 *
	 * @param storeAppealVO
	 * @return 操作状态
	 */
	Boolean appeal(StoreAppealDTO storeAppealDTO);
}
