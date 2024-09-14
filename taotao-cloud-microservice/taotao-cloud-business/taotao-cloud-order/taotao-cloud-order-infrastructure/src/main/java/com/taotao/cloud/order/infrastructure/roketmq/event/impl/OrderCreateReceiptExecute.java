/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.order.infrastructure.roketmq.event.impl;

import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.cloud.order.sys.model.dto.cart.TradeDTO;
import com.taotao.cloud.order.sys.model.vo.order.OrderVO;
import com.taotao.cloud.order.sys.model.vo.order.ReceiptVO;
import com.taotao.cloud.order.infrastructure.model.entity.order.Receipt;
import com.taotao.cloud.order.infrastructure.roketmq.event.TradeEvent;
import com.taotao.cloud.order.infrastructure.service.business.order.IReceiptService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // 根据交易sn查询订单信息
        List<OrderVO> orderList = tradeDTO.getOrderVO();
        // 获取发票信息
        ReceiptVO receiptVO = tradeDTO.getReceiptVO();
        // 如果需要获取发票则保存发票信息
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
            // 保存发票
            receiptService.saveBatch(receipts);
        }
    }
}
