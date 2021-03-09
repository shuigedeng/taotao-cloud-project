package com.taotao.cloud.java.javaee.s2.c9_springcloud.p5_zuul.java.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

@Component
public class TestZuulFilter2 extends ZuulFilter {


    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        // 开启当前过滤器
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("prefix过滤器2222执行~~~");
        return null;
    }
}
