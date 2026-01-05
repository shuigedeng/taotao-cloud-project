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

package com.taotao.cloud.order.biz.service.business.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.sys.model.dto.order.OrderReceiptDTO;
import com.taotao.cloud.order.sys.model.page.order.ReceiptPageQuery;
import com.taotao.cloud.order.biz.model.entity.order.Receipt;

/**
 * 发票业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:50
 */
public interface ReceiptService extends IService<Receipt> {

    /**
     * 根据条件获取发票信息列表
     *
     * @param receiptPageQuery 发票查询参数
     * @return {@link IPage }<{@link OrderReceiptDTO }>
     * @since 2022-04-28 08:54:50
     */
    IPage<OrderReceiptDTO> pageQuery(ReceiptPageQuery receiptPageQuery);

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
     * @return {@link Boolean }
     * @since 2022-04-28 08:54:50
     */
    Boolean saveReceipt(Receipt receipt);

    /**
     * 开具发票
     *
     * @param receiptId 发票id
     * @return {@link Receipt }
     * @since 2022-04-28 08:54:50
     */
    Receipt invoicing(Long receiptId);
}
