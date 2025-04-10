package com.taotao.cloud.ccsr.client.client.invoke;

import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.client.AbstractClient;
import com.taotao.cloud.ccsr.client.context.OHaraMcsContext;
import com.taotao.cloud.ccsr.client.lifecycle.Closeable;
import com.taotao.cloud.ccsr.client.option.RequestOption;

import  com.taotao.cloud.ccsr.client.request.Payload;
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
