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

import com.alibaba.fastjson.JSON;
import com.taotao.boot.common.model.Result;
import com.taotao.cloud.order.application.service.order.check.handler.AbstractCheckHandler;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CheckService {

    /**
     * 使用Spring注入:所有继承了AbstractCheckHandler抽象类的Spring
     * Bean都会注入进来。Map的Key对应Bean的name,Value是name对应相应的Bean
     */
    @Resource
    private Map<String, AbstractCheckHandler> handlerMap;

    /// **
    // * 创建商品
    // * @return
    // */
    // public Result createProduct(ProductVO param) {
    //
    //    //参数校验，使用责任链模式
    //    Result paramCheckResult = this.paramCheckChain(param);
    //    if (!paramCheckResult.isSuccess()) {
    //        return paramCheckResult;
    //    }
    //
    //    //创建商品
    //    return this.saveProduct(param);
    // }

    /**
     * 参数校验：责任链模式
     *
     * @param param
     * @return
     */
    public Result paramCheckChain(ProductVO param) {

        // 获取处理器配置：通常配置使用统一配置中心存储，支持动态变更
        CheckHandlerConfig handlerConfig = this.getHandlerConfigFile();

        // 获取处理器
        AbstractCheckHandler handler = this.getHandler(handlerConfig);

        // 责任链：执行处理器链路
        Result executeChainResult = HandlerClient.executeChain(handler, param);
        if (!executeChainResult.isSuccess()) {
            LogUtils.info("创建商品 失败...");
            return executeChainResult;
        }

        // 处理器链路全部成功
        return Result.success();
    }

    /**
     * 获取处理器配置：通常配置使用统一配置中心存储，支持动态变更
     *
     * @return
     */
    private CheckHandlerConfig getHandlerConfigFile() {
        // 配置中心存储的配置
        String configJson =
                "{\"handler\":\"nullValueCheckHandler\",\"down\":false,\"next\":{\"handler\":\"priceCheckHandler\",\"next\":{\"handler\":\"stockCheckHandler\",\"next\":null}}}";
        // 转成Config对象
        CheckHandlerConfig handlerConfig = JSON.parseObject(configJson, CheckHandlerConfig.class);
        return handlerConfig;
    }

    /**
     * 获取处理器
     *
     * @param config
     * @return
     */
    private AbstractCheckHandler getHandler(CheckHandlerConfig config) {
        // 配置检查：没有配置处理器链路，则不执行校验逻辑
        if (Objects.isNull(config)) {
            return null;
        }
        // 配置错误
        String handler = config.getHandler();
        if (StringUtils.isBlank(handler)) {
            return null;
        }
        // 配置了不存在的处理器
        AbstractCheckHandler abstractCheckHandler = handlerMap.get(config.getHandler());
        if (Objects.isNull(abstractCheckHandler)) {
            return null;
        }

        // 处理器设置配置Config
        abstractCheckHandler.setConfig(config);

        // 递归设置链路处理器
        abstractCheckHandler.setNextHandler(this.getHandler(config.getNext()));

        return abstractCheckHandler;
    }

    private Result saveProduct(ProductVO param) {
        LogUtils.info("保存商品 成功...");
        return Result.success(param);
    }
}
