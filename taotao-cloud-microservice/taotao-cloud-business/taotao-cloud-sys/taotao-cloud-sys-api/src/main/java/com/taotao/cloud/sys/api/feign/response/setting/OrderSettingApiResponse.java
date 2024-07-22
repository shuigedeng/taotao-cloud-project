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

package com.taotao.cloud.sys.api.feign.response.setting;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/** 订单设置 */
@Data
public class OrderSettingApiResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -2628613596000114786L;
    /** 自动取消 分钟 */
    private Integer autoCancel;

    /** 自动收货 天 */
    private Integer autoReceive;

    /**
     * 已完成订单允许退单：X天内，允许客户发起退货退款申请，未发货订单随时可退，未发货订单随时可退
     *
     * <p>已完成订单允许退单 天
     */
    private Integer closeAfterSale;

    /** 自动评价 天 */
    private Integer autoEvaluation;

    // ---------------售后---------------

    /** 售后自动取消 天 */
    private Integer autoCancelAfterSale;

    /**
     * 待审核退单自动审核：X天后，商家逾期未处理的待审核退单，将会自动审核通过。
     *
     * <p>待审核退单自动审核 天
     */
    private Integer autoAfterSaleReview;

    /**
     * 退单自动确认收货：X天后，商家逾期未处理的待收货退单，将会自动确认收货，非快递退货的退单，再审核通过后开始计时。
     *
     * <p>已完成订单允许退单 天
     */
    private Integer autoAfterSaleComplete;

    // ---------------投诉---------------
    /**
     * 已完成订单允许投诉：X天内，允许客户发起交易投诉 如果写0，则不允许投诉
     *
     * <p>已完成订单允许投诉 天
     */
    private Integer closeComplaint;
}
