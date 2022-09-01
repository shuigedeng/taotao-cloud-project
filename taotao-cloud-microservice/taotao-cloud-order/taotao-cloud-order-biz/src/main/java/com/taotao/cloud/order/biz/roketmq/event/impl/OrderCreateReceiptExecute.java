package com.taotao.cloud.order.biz.roketmq.event.impl;

import cn.hutool.json.JSONUtil;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.order.api.model.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.model.vo.order.OrderVO;
import com.taotao.cloud.order.api.model.vo.order.ReceiptVO;
import com.taotao.cloud.order.biz.model.entity.order.Receipt;
import com.taotao.cloud.order.biz.roketmq.event.TradeEvent;
import com.taotao.cloud.order.biz.service.order.IReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单创建发票相关处理
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-16 17:36:24
 */
@Service
public class OrderCreateReceiptExecute implements TradeEvent {

	@Autowired
	private IReceiptService receiptService;

	@Override
	public void orderCreate(TradeDTO tradeDTO) {
		//根据交易sn查询订单信息
		List<OrderVO> orderList = tradeDTO.getOrderVO();
		//获取发票信息
		ReceiptVO receiptVO = tradeDTO.getReceiptVO();
		//如果需要获取发票则保存发票信息
		if (Boolean.TRUE.equals(tradeDTO.getNeedReceipt()) && !orderList.isEmpty()) {
			List<Receipt> receipts = new ArrayList<>();
			for (OrderVO orderVO : orderList) {
				Receipt receipt = new Receipt();
				BeanUtils.copyProperties(receiptVO, receipt);
				receipt.setMemberId(orderVO.orderBase().memberId());
				receipt.setMemberName(orderVO.orderBase().memberName());
				receipt.setStoreId(orderVO.orderBase().storeId());
				receipt.setStoreName(orderVO.orderBase().storeName());
				receipt.setOrderSn(orderVO.orderBase().sn());
				receipt.setReceiptDetail(JSONUtil.toJsonStr(orderVO.orderItems()));
				receipt.setReceiptPrice(orderVO.orderBase().flowPrice());
				receipt.setReceiptStatus(0);
				receipts.add(receipt);
			}
			//保存发票
			receiptService.saveBatch(receipts);
		}
	}
}
