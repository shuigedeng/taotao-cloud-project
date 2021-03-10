package com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.factory;

import com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.client.SearchClient;
import com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.fallback.SearchClientFallBack;
import feign.hystrix.FallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchClientFallBackFactory implements FallbackFactory<SearchClient> {

    @Autowired
    private SearchClientFallBack searchClientFallBack;

    @Override
    public SearchClient create(Throwable throwable) {
        throwable.printStackTrace();
        return searchClientFallBack;
    }
}
