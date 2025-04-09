package com.taotao.cloud.ccsr.client.client.filter;


import org.ohara.msc.client.OHaraMcsClient;
import org.ohara.msc.client.invoke.AbstractInvoker;
import org.ohara.msc.client.invoke.GrpcInvoker;
import org.ohara.msc.option.RequestOption;

import java.util.concurrent.TimeUnit;

public class InvokerFilter extends AbstractInvokerFilter<RequestOption> {

    private final OHaraMcsClient client;

    public InvokerFilter(OHaraMcsClient client) {
        super(client);
        this.client = client;
    }

    @Override
    protected void doInit() throws Exception {
        registerInvoker(new GrpcInvoker(client));
        // TODO registerInvoker(new HttpInvoker(client));
    }

    @Override
    protected void doDestroy(Integer timeout, TimeUnit unit) {
        String protocol = client.getOption().protocol();
        getInvoker(protocol).shutdown();
    }
}
