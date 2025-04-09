package com.taotao.cloud.ccsr.client.client.invoke;

import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import org.ohara.msc.client.AbstractClient;
import org.ohara.msc.client.filter.ConvertFilter;
import org.ohara.msc.context.OHaraMcsContext;
import org.ohara.msc.lifecycle.Closeable;
import org.ohara.msc.option.RequestOption;
import org.ohara.msc.request.Payload;


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
