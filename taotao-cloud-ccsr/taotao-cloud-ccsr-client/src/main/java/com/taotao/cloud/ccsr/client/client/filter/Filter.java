package com.taotao.cloud.ccsr.client.client.filter;

import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.lifecycle.LeftCycle;

public interface Filter<S, OPTION> extends LeftCycle {

    Filter<S, OPTION> next(Filter<S, OPTION> filter);

    Filter<S, OPTION> next();

    Filter<S, OPTION> pre(Filter<S, OPTION> filter);

    Filter<S, OPTION> pre();

    S preFilter(OHaraMcsContext context, OPTION option, Payload request);

    S postFilter(OHaraMcsContext context, OPTION option, Payload request, S response);

}
