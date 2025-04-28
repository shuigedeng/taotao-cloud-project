package com.taotao.cloud.ccsr.client.client.filter;

import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.client.AbstractClient;
import com.taotao.cloud.ccsr.client.context.CcsrContext;
import com.taotao.cloud.ccsr.client.option.RequestOption;
import  com.taotao.cloud.ccsr.client.request.Payload;
public class SignFilter<OPTION extends RequestOption> extends AbstractFilter<OPTION> {

    public SignFilter(AbstractClient<OPTION> client) {
        super(client);
    }

    @Override
    protected Response doPreFilter(CcsrContext context, OPTION option, Payload request) {
        // TODO 待后续实现客户端和服务端的签名校验
        return null;
    }

    @Override
    protected Response doPostFilter(CcsrContext context, OPTION option, Payload request, Response response) {
        // TODO
        return response;
    }
}
