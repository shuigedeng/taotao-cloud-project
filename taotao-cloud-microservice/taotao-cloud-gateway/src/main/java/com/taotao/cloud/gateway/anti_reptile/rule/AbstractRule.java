package com.taotao.cloud.gateway.anti_reptile.rule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.server.ServerWebExchange;

public abstract class AbstractRule implements AntiReptileRule {


    @Override
    public boolean execute(ServerWebExchange exchange) {
        return doExecute(exchange);
    }

    protected abstract boolean doExecute(ServerWebExchange exchange);
}
