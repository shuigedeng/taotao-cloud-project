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
import com.taotao.cloud.order.application.service.order.check.CheckHandlerConfig;
import com.taotao.cloud.order.application.service.order.check.ProductVO;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/** 抽象类处理器 */
@Component
public abstract class AbstractCheckHandler {

    /** 当前处理器持有下一个处理器的引用 */
    @Getter
    @Setter
    private AbstractCheckHandler nextHandler;

    /**
     * 处理器执行方法
     *
     * @param param
     * @return
     */
    public abstract Result handle(ProductVO param);

    /** 处理器配置 */
    @Setter
    @Getter
    protected CheckHandlerConfig config;

    /**
     * 链路传递
     *
     * @param param
     * @return
     */
    protected Result next(ProductVO param) {
        // 下一个链路没有处理器了，直接返回
        if (Objects.isNull(nextHandler)) {
            return Result.success();
        }

        // 执行下一个处理器
        return nextHandler.handle(param);
    }
}
