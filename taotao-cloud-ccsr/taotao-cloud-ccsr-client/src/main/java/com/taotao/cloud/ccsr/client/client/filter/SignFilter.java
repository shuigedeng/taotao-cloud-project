package com.taotao.cloud.ccsr.client.client.filter;

import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.AbstractClient;
import com.taotao.cloud.ccsr.client.context.OHaraMcsContext;
import com.taotao.cloud.ccsr.client.option.RequestOption;

public class SignFilter<OPTION extends RequestOption> extends AbstractFilter<OPTION> {

    public SignFilter(AbstractClient<OPTION> client) {
        super(client);
    }

    @Override
    protected Response doPreFilter(OHaraMcsContext context, OPTION option, Payload request) {
        // TODO 待后续实现客户端和服务端的签名校验
        return null;
    }

    @Override
    protected Response doPostFilter(OHaraMcsContext context, OPTION option, Payload request, Response response) {
        // TODO
        return response;
    }
}
