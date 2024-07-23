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

package com.taotao.cloud.order.application.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.application.command.cart.dto.TradeAddCmd;
import com.taotao.cloud.order.infrastructure.persistent.po.order.TradePO;

/**
 * 交易业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:55
 */
public interface ITradeService extends IService<TradePO> {

    /**
     * 创建交易
	 * <p>1.订单数据校验
	 * <p>2.积分预处理
	 * <p>3.优惠券预处理
	 * <p>4.添加交易
	 * <p>5.添加订单
	 * <p>6.将交易写入缓存供消费者调用
	 * <p>7.发送交易创建消息
     *
     * @param tradeAddCmd 购物车视图
     * @return {@link TradePO }
     * @since 2022-04-28 08:54:55
     */
    TradePO createTrade(TradeAddCmd tradeAddCmd);

    /**
     * 获取交易详情
     *
     * @param sn 交易编号
     * @return {@link TradePO }
     * @since 2022-04-28 08:54:55
     */
    TradePO getBySn(String sn);

    /**
     * 整笔交易付款
     *
     * @param tradeSn 交易编号
     * @param paymentName 支付方式
     * @param receivableNo 第三方流水号
     * @since 2022-04-28 08:54:55
     */
    void payTrade(String tradeSn, String paymentName, String receivableNo);
}
