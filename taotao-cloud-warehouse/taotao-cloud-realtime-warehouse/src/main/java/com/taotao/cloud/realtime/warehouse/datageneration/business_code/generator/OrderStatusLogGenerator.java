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

package com.taotao.cloud.realtime.warehouse.datageneration.business_code.generator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.taotao.cloud.realtime.warehouse.datageneration.business_code.util.DbUtil;
import com.taotao.cloud.realtime.warehouse.datageneration.business_code.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * OrderStatusLogGenerator
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Component
public class OrderStatusLogGenerator {

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusLogGenerator.class);

    @Autowired
    private DbUtil dbUtil;

    public void generateOrderStatusLog( int count ) {
        // 获取最大ID
        String maxIdSql = "SELECT COALESCE(MAX(id), 0) FROM order_status_log";
        int startId = dbUtil.queryForInt(maxIdSql) + 1;

        String sql =
                "INSERT INTO order_status_log (id, order_id, order_status, operate_time) "
                        + "VALUES (?, ?, ?, ?)";

        List<Object[]> params = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int id = startId + i;
            // 从已有订单中选择
            int orderId = RandomUtil.generateNumber(1, 4863); // 根据order_info表的AUTO_INCREMENT值
            String orderStatus =
                    String.valueOf(RandomUtil.generateNumber(1001, 1006)); // 根据base_dic表中的状态码
            LocalDateTime operateTime = LocalDateTime.now();

            params.add(new Object[]{id, orderId, orderStatus, operateTime});
        }
        dbUtil.batchInsert(sql, params);
    }
}
