package com.taotao.cloud.gateway.springcloud.anti_reptile.rule;

import org.springframework.web.server.ServerWebExchange;

public abstract class AbstractRule implements AntiReptileRule {


    @Override
    public boolean execute(ServerWebExchange exchange) {
        return doExecute(exchange);
    }

    protected abstract boolean doExecute(ServerWebExchange exchange);
}
