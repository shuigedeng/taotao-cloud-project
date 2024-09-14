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

package com.taotao.cloud.order.application.service.order.check;

import com.taotao.boot.common.model.Result;
import com.taotao.cloud.order.application.service.order.check.handler.AbstractCheckHandler;

/** 责任链模式之客户端 */
public class HandlerClient {

    /**
     * 执行链路
     *
     * @param handler 处理器
     * @param param 商品参数
     * @return
     */
    public static Result executeChain(AbstractCheckHandler handler, ProductVO param) {
        // 执行处理器
        Result handlerResult = handler.handle(param);
        if (!handlerResult.isSuccess()) {
            LogUtils.info("HandlerClient 责任链执行失败返回：" + handlerResult.toString());
            return handlerResult;
        }
        return Result.success();
    }
}
