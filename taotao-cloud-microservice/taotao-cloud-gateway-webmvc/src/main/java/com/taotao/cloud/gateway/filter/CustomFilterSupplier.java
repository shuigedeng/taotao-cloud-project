package com.taotao.cloud.gateway.filter;

import org.springframework.cloud.gateway.server.mvc.filter.SimpleFilterSupplier;

/**
 * @author Mr han
 * @date 2025/12/15
 * @description 创建字对应的filterSupplier，用于注册提供自定义的filter
 */
public class CustomFilterSupplier extends SimpleFilterSupplier {
    public CustomFilterSupplier() {
        super(SampleHandlerFilterFunctions.class);
    }
}
