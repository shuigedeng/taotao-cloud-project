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

package com.taotao.cloud.realtime.warehouse.datageneration.business_code;

import com.taotao.cloud.realtime.warehouse.datageneration.business_code.generator.*;
import com.taotao.cloud.realtime.warehouse.datageneration.business_code.util.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * BusinessCommandLineRunner
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Configuration
@Order(1)
public class BusinessCommandLineRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BusinessCommandLineRunner.class);

    @Value("${generator.batch-size:1000}")
    private int batchSize;

    @Value("${generator.interval:5000}")
    private long interval;

    @Autowired
    @Qualifier(value = "asyncThreadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private DbUtil dbUtil;

    @Autowired
    private BaseDataGenerator baseDataGenerator;

    @Autowired
    private ProductDataGenerator productDataGenerator;

    @Autowired
    private ActivityDataGenerator activityDataGenerator;

    @Autowired
    private CouponDataGenerator couponDataGenerator;

    @Autowired
    private OrderDataGenerator orderDataGenerator;

    @Autowired
    private UserBehaviorGenerator userBehaviorGenerator;

    @Autowired
    private WarehouseDataGenerator warehouseDataGenerator;

    @Autowired
    private CMSDataGenerator cmsDataGenerator;

    @Autowired
    private UserDataGenerator userDataGenerator;

    @Override
    public void run( String... args ) throws Exception {
        threadPoolTaskExecutor.submit(() -> {
            try {
                baseDataGenerator.generateBaseData(batchSize);

                while (true) {
                    userDataGenerator.generateUserData(batchSize / 10);
                    productDataGenerator.generateProductData(batchSize / 10, batchSize / 20);
                    activityDataGenerator.generateActivityData(batchSize / 10, batchSize / 20);
                    couponDataGenerator.generateCouponData(batchSize / 10, batchSize / 20);
                    orderDataGenerator.generateOrderData(batchSize);
                    userBehaviorGenerator.generateUserBehaviorData(batchSize);
                    warehouseDataGenerator.generateWarehouseData(batchSize / 5);
                    cmsDataGenerator.generateCMSData(batchSize / 20, batchSize / 40, batchSize / 100);
                    Thread.sleep(interval);
                }
            } catch (Exception e) {
                logger.error("Error generating data", e);
            } finally {
                dbUtil.close();
            }
        });

    }
}
