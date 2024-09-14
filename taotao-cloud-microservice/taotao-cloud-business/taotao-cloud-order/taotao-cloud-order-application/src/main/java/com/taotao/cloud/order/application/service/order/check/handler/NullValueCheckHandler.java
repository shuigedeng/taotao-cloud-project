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

package com.taotao.cloud.order.application.service.order.check.handler;

import com.taotao.boot.common.model.Result;
import java.util.Objects;
import org.springframework.stereotype.Component;

/** 空值校验处理器 */
@Component
public class NullValueCheckHandler extends AbstractCheckHandler {

    @Override
    public Result handle(ProductVO param) {
        LogUtils.info("空值校验 Handler 开始...");

        // 降级：如果配置了降级，则跳过此处理器，执行下一个处理器
        if (super.getConfig().getDown()) {
            LogUtils.info("空值校验 Handler 已降级，跳过空值校验 Handler...");
            return super.next(param);
        }

        // 参数必填校验
        if (Objects.isNull(param)) {
            return Result.fail(ErrorCode.PARAM_NULL_ERROR);
        }
        // SkuId商品主键参数必填校验
        if (Objects.isNull(param.getSkuId())) {
            return Result.failure(ErrorCode.PARAM_SKU_NULL_ERROR);
        }
        // Price价格参数必填校验
        if (Objects.isNull(param.getPrice())) {
            return Result.failure(ErrorCode.PARAM_PRICE_NULL_ERROR);
        }
        // Stock库存参数必填校验
        if (Objects.isNull(param.getStock())) {
            return Result.failure(ErrorCode.PARAM_STOCK_NULL_ERROR);
        }

        LogUtils.info("空值校验 Handler 通过...");

        // 执行下一个处理器
        return super.next(param);
    }
}
