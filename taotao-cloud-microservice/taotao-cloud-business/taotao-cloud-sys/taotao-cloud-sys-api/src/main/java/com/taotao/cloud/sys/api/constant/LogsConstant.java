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

package com.taotao.cloud.sys.api.constant;

/**
 * 说明：订单的状态常量池
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:16:36
 */
public interface LogsConstant {

    /** 主状态-待付款 --10 (等待支付) */
    Integer WAIT_PAYMENT = 10;

    /** 待付款子状态-未支付 --1010 (等待支付) */
    Integer UN_PAYMENTED = 1010;

    /** 主状态-收发货 --20 */
    Integer WAIT_RECEIVE = 20;

    /** 待收货--待卖家发货 --2010 */
    Integer UN_DELIVERY = 2010;

    /** 待收货--备货中 --2030 */
    Integer STOCK = 2030;

    /** 待收货--待买家收货 --2020 (已发货) */
    Integer UN_RECEIVE = 2020;

    /** 主状态-确认收货-交易完成 --30 */
    Integer COMPLETED = 30;

    /** 交易完成-确认收货-待评价 --3010 */
    Integer COMPLETE_UN_EVALUATED = 3010;

    /** 交易完成--已评价 --3020 */
    Integer COMPLETE_EVALUATED = 3020;

    /** 主状态-交易成功--40 (交易成功) */
    Integer SUCCESS = 40;

    /** 交易成功--待评价 --4010 (交易成功--没有退款) */
    Integer SUCCESS_UN_EVALUATED = 4010;

    /** 交易成功--待评价 --4030 (交易成功--部分退款) */
    Integer SUCCESS_PARTIAL_REFUND = 4030;

    /** 交易成功--已评价 --4020 (交易成功) */
    Integer SUCCESS_EVALUATED = 4020;

    /** 主状态-交易关闭 --50 (交易关闭) */
    Integer CLOSE = 50;

    /** 交易关闭--未支付用户取消 --5010 (交易关闭) */
    Integer USER_CACNEL = 5010;

    /** 交易关闭--商户取消 --5020 (交易关闭) */
    Integer MCH_CANCEL = 5020;

    /** 交易关闭--退款成功 --5030 (交易关闭) */
    Integer REFUND_AMOUNT = 5030;

    /** 交易关闭--定时任务取消订单 --5040 (交易关闭) */
    Integer SYSTEM_CANCEL = 5040;

    /** 不可评价 --0 */
    Integer DISABLE = 0;

    /** 可评价 --1 */
    Integer ABLE = 1;

    /** 可追评 --2 */
    Integer APPEND = 2;

    /** 数据类型 2全部，0待收货 1已收货 3售后 */
    String ALL = "2";

    String TO_RECEIVED = "0";
    String RECEIVED = "1";
    String POST_SALE = "3";

    /** 订单状态（1 已收货 0待收货） */
    Integer ORDER_COLLECT_STATUS_NOT_FINISHED = 0;

    Integer ORDER_COLLECT_STATUS_FINISHED = 1;

    /** 是否有剩余 0有 1无 */
    String MALL_COLLECT_DETAIL_HAS_SURPLAS = "0";

    String MALL_COLLECT_DETAIL_NO_SURPLAS = "1";

    /** 异常状态 0缺货 1补货 2拒收 3超时 */
    String UNEXPECT_STATUS_LACK = "0";

    String UNEXPECT_STATUS_REPENLISH = "1";
    String UNEXPECT_STATUS_REJECT = "2";
    String UNEXPECT_STATUS_OVERTIME = "3";

    /** 今日或者昨天 0今天 1 昨天 */
    String MALL_COLLECT_TODAY = "0";

    String MALL_COLLECT_YESTERDAY = "1";
}
