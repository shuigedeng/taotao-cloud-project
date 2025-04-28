package com.taotao.cloud.ccsr.client.client.filter;

import com.taotao.cloud.ccsr.client.context.CcsrContext;
import com.taotao.cloud.ccsr.client.lifecycle.LeftCycle;
import  com.taotao.cloud.ccsr.client.request.Payload;
public interface Filter<S, OPTION> extends LeftCycle {

	Filter<S, OPTION> next(Filter<S, OPTION> filter);

	Filter<S, OPTION> next();

	Filter<S, OPTION> pre(Filter<S, OPTION> filter);

	Filter<S, OPTION> pre();

	S preFilter(CcsrContext context, OPTION option, Payload request);

	S postFilter(CcsrContext context, OPTION option, Payload request, S response);

}
