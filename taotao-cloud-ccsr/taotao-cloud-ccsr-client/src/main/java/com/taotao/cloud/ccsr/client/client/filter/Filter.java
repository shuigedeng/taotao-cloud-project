package com.taotao.cloud.ccsr.client.client.filter;

import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import org.ohara.msc.context.OHaraMcsContext;
import org.ohara.msc.lifecycle.LeftCycle;
import org.ohara.msc.request.Payload;

public interface Filter<S, OPTION> extends LeftCycle {

    Filter<S, OPTION> next(Filter<S, OPTION> filter);

    Filter<S, OPTION> next();

    Filter<S, OPTION> pre(Filter<S, OPTION> filter);

    Filter<S, OPTION> pre();

    S preFilter(OHaraMcsContext context, OPTION option, Payload request);

    S postFilter(OHaraMcsContext context, OPTION option, Payload request, S response);

}
