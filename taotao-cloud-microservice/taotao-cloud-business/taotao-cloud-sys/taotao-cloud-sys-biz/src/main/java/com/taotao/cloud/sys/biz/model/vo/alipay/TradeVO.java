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

package com.taotao.cloud.sys.biz.model.vo.alipay;

import java.sql.Date;
import java.sql.Timestamp;
import lombok.*;

/**
 * 交易详情，按需应该存入数据库
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:47:58
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TradeVO {

    /** （必填）商品描述 */
    private String body;

    /** （必填）商品名称 */
    private String subject;

    /** （必填）商户订单号，应该由后台生成 */
    private String outTradeNo;

    /** （必填）第三方订单号 */
    private String tradeNo;

    /** （必填）价格 */
    private String totalAmount;

    /** 订单状态,已支付，未支付，作废 */
    private String state;

    /** 创建时间，存入数据库时需要 */
    private Timestamp createTime;

    /** 作废时间，存入数据库时需要 */
    private Date cancelTime;
}
