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

package com.taotao.cloud.order.infrastructure.persistent.mapper.order;

import com.taotao.cloud.order.infrastructure.persistent.po.order.TradePO;
import com.taotao.boot.webagg.mapper.BaseSuperMapper;
import org.apache.ibatis.annotations.Update;

/** 交易数据处理层 */
public interface ITradeMapper extends BaseSuperMapper<TradePO, Long> {

    /**
     * 修改交易金额
     *
     * @param tradeSn 交易编号
     */
    @Update("UPDATE tt_trade SET flow_price =(SELECT SUM(flow_price) FROM tt_order WHERE"
            + " trade_sn=#{tradeSn}) WHERE sn=#{tradeSn}")
    void updateTradePrice(String tradeSn);
}
