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

package com.taotao.cloud.payment.biz.bootx.code.pay;

/**
 * 支付网关同步状态
 *
 * @author xxm
 * @date 2021/4/21
 */
public interface PaySyncStatus {

    /** -1 不需要同步 */
    int NOT_SYNC = -1;
    /** 1 远程支付成功 */
    int TRADE_SUCCESS = 1;
    /** 2 交易创建，等待买家付款 */
    int WAIT_BUYER_PAY = 2;
    /** 3 已关闭 */
    int TRADE_CLOSED = 3;
    /** 4 查询不到订单 */
    int NOT_FOUND = 4;
    /** 5 查询失败 */
    int FAIL = 5;
}
