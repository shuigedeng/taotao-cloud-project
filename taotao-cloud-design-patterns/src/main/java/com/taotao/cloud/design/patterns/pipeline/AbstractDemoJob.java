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

package com.taotao.cloud.design.patterns.pipeline;

import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.json.JSONUtil;

/**
 * 管道任务-抽象层
 *
 */
@Slf4j
public abstract class AbstractDemoJob implements PipelineJob<DemoPipelineProduct> {

    /**
     * 公共执行逻辑
     *
     * @param product 产品
     * @return
     */
    @Override
    public DemoPipelineProduct execute(DemoPipelineProduct product) {
        DemoPipelineProduct.DemoSignalEnum newSignal;
        try {
            newSignal = execute(product.getTradeId(), product.getProductData());
        } catch (Exception e) {
            product.setException(e);
            newSignal = DemoPipelineProduct.DemoSignalEnum.BUSINESS_ERROR;
        }
        product.setSignal(newSignal);
        defaultLogPrint(product.getTradeId(), product);
        return product;
    }

    /**
     * 子类执行逻辑
     *
     * @param tradeId     流程Id
     * @param productData 请求数据
     * @return
     * @throws Exception 异常
     */
    abstract DemoPipelineProduct.DemoSignalEnum execute(
            String tradeId, DemoPipelineProduct.DemoProductData productData) throws Exception;

    /**
     * 默认的日志打印
     */
    public void defaultLogPrint(String tradeId, DemoPipelineProduct product) {
        if (!DemoPipelineProduct.DemoSignalEnum.NORMAL.equals(product.getSignal())) {
            log.info(
                    "流水线任务处理异常：流程Id=【{}】,信号量=【{}】,任务=【{}】,参数=【{}】",
                    tradeId,
                    product.getSignal(),
                    ClassUtil.getClassName(this, true),
                    JSONUtil.toJsonStr(product.getProductData()),
                    product.getException());
        }
    }
}
