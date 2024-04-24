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

package com.taotao.cloud.order.infrastructure.roketmq.event;

import com.taotao.cloud.order.infrastructure.model.entity.aftersale.AfterSale;

/**
 * 售后单改变状态
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-16 17:33:52
 */
public interface AfterSaleStatusChangeEvent {

    /**
     * 售后单改变
     *
     * @param afterSale 售后
     * @since 2022-05-16 17:33:52
     */
    void afterSaleStatusChange(AfterSale afterSale);
}
