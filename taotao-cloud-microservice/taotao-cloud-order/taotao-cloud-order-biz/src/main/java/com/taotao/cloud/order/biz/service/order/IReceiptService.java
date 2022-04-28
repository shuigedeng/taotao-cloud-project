package com.taotao.cloud.order.biz.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.api.dto.order.OrderReceiptDTO;
import com.taotao.cloud.order.api.query.order.ReceiptPageQuery;
import com.taotao.cloud.order.biz.entity.order.Receipt;

/**
 * 发票业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:50
 */
public interface IReceiptService extends IService<Receipt> {

	/**
	 * 根据条件获取发票信息列表
	 *
	 * @param searchParams 发票查询参数
	 * @param pageVO       分页参数
	 * @return {@link IPage }<{@link OrderReceiptDTO }>
	 * @since 2022-04-28 08:54:50
	 */
	IPage<OrderReceiptDTO> getReceiptData(ReceiptPageQuery searchParams, PageVO pageVO);

	/**
	 * 根据订单编号获取发票信息
	 *
	 * @param orderSn 订单编号
	 * @return {@link Receipt }
	 * @since 2022-04-28 08:54:50
	 */
	Receipt getByOrderSn(String orderSn);

	/**
	 * 获取发票详情
	 *
	 * @param id 发票id
	 * @return {@link Receipt }
	 * @since 2022-04-28 08:54:50
	 */
	Receipt getDetail(String id);

	/**
	 * 保存发票
	 *
	 * @param receipt 发票信息
	 * @return {@link Receipt }
	 * @since 2022-04-28 08:54:50
	 */
	Receipt saveReceipt(Receipt receipt);

	/**
	 * 开具发票
	 *
	 * @param receiptId 发票id
	 * @return {@link Receipt }
	 * @since 2022-04-28 08:54:50
	 */
	Receipt invoicing(Long receiptId);


}
