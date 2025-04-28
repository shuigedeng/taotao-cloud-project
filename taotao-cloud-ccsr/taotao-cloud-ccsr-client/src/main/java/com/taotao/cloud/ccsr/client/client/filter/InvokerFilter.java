package com.taotao.cloud.ccsr.client.client.filter;


import com.taotao.cloud.ccsr.client.client.CcsrClient;
import com.taotao.cloud.ccsr.client.client.invoke.GrpcInvoker;
import com.taotao.cloud.ccsr.client.option.RequestOption;

import java.util.concurrent.TimeUnit;

public class InvokerFilter extends AbstractInvokerFilter<RequestOption> {

	private final CcsrClient client;

	public InvokerFilter(CcsrClient client) {
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
