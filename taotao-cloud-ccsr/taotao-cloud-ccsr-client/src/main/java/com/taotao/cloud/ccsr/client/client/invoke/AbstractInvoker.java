package com.taotao.cloud.ccsr.client.client.invoke;

import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.AbstractClient;
import com.taotao.cloud.ccsr.client.filter.ConvertFilter;
import com.taotao.cloud.ccsr.context.OHaraMcsContext;
import com.taotao.cloud.ccsr.lifecycle.Closeable;
import com.taotao.cloud.ccsr.option.RequestOption;
import com.taotao.cloud.ccsr.request.Payload;


public abstract class AbstractInvoker<R, OPTION extends RequestOption> implements Closeable {

    private final AbstractClient<?> client;

    public AbstractInvoker(AbstractClient<?> client) {
        this.client = client;
    }

    public abstract Response invoke(OHaraMcsContext context, Payload request);

    // TODO 异步 future invoke

    public abstract String protocol();

    public abstract R convert(OHaraMcsContext context, OPTION option, Payload request);

    protected String getNamespace() {
        return this.client.getNamespace();
    }

    @SuppressWarnings("unchecked")
    protected OPTION getOption() {
        return (OPTION) this.client.getOption();
    }
}
