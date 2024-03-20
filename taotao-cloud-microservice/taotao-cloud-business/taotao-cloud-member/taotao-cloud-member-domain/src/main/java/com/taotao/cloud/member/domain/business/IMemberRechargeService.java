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

package com.taotao.cloud.member.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.member.biz.model.entity.MemberRecharge;
import com.taotao.cloud.order.api.model.page.recharge.RechargePageQuery;
import java.math.BigDecimal;

/**
 * 预存款充值业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-05-31 13:21:47
 */
public interface IMemberRechargeService extends IService<MemberRecharge> {

    /**
     * 创建充值订单
     *
     * @param price 价格
     * @return {@link MemberRecharge }
     * @since 2022-05-31 13:21:47
     */
    MemberRecharge recharge(BigDecimal price);

    /**
     * 查询充值订单列表
     *
     * @param rechargePageQuery 查询条件
     * @return {@link IPage }<{@link MemberRecharge }>
     * @since 2022-05-31 13:21:47
     */
    IPage<MemberRecharge> rechargePage(RechargePageQuery rechargePageQuery);

    /**
     * 支付成功
     *
     * @param sn 充值订单编号
     * @param receivableNo 流水no
     * @param paymentMethod 支付方式
     * @since 2022-05-31 13:21:47
     */
    void paySuccess(String sn, String receivableNo, String paymentMethod);

    /**
     * 根据充值订单号查询充值信息
     *
     * @param sn 充值订单号
     * @return {@link MemberRecharge }
     * @since 2022-05-31 13:21:47
     */
    MemberRecharge getRecharge(String sn);

    /**
     * 充值订单取消
     *
     * @param sn 充值订单sn
     * @since 2022-05-31 13:21:47
     */
    void rechargeOrderCancel(String sn);
}
