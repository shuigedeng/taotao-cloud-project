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
import com.taotao.cloud.order.application.service.order.check.ErrorCode;
import com.taotao.cloud.order.application.service.order.check.ProductVO;
import org.springframework.stereotype.Component;

/** 库存校验处理器 */
@Component
public class StockCheckHandler extends AbstractCheckHandler {
    @Override
    public Result handle(ProductVO param) {
        LogUtils.info("库存校验 Handler 开始...");

        // 非法库存校验
        boolean illegalStock = param.getStock() < 0;
        if (illegalStock) {
            return Result.failure(ErrorCode.PARAM_STOCK_ILLEGAL_ERROR);
        }
        // 其他校验逻辑..

        LogUtils.info("库存校验 Handler 通过...");

        // 执行下一个处理器
        return super.next(param);
    }
}
