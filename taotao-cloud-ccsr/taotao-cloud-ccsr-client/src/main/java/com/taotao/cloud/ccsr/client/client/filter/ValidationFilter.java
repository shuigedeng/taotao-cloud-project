package com.taotao.cloud.ccsr.client.client.filter;

import com.taotao.cloud.ccsr.api.grpc.auto.MetadataType;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.client.client.AbstractClient;
import com.taotao.cloud.ccsr.client.context.CcsrContext;
import com.taotao.cloud.ccsr.client.option.RequestOption;
import com.taotao.cloud.ccsr.common.enums.ResponseCode;
import  com.taotao.cloud.ccsr.client.request.Payload;
public class ValidationFilter<OPTION extends RequestOption> extends AbstractFilter<OPTION> {

    public ValidationFilter(AbstractClient<OPTION> client) {
        super(client);
    }

    @Override
    protected Response doPreFilter(CcsrContext context, OPTION option, Payload request) {
        try {
            validate(request);
        } catch (Exception e) {
            return ResponseHelper.error(ResponseCode.CLIENT_ERROR.getCode(), e.getMessage());
        }
        return null;
    }

    @Override
    protected Response doPostFilter(CcsrContext context, OPTION option, Payload request, Response response) {
        try {
            validate(response);
        } catch (Exception e) {
            return ResponseHelper.error(ResponseCode.CLIENT_ERROR.getCode(), e.getMessage());
        }
        return response;
    }

    private void validate(Payload request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
        if (request.getNamespace() == null) {
            throw new IllegalArgumentException("request namespace is null");
        }
        if (request.getGroup() == null) {
            throw new IllegalArgumentException("request group is null");
        }
        if (request.getTag() == null) {
            request.setTag("");
        }
        if (request.getType() == null) {
            request.setType(MetadataType.JSON);
        }
        if (request.getDataId() == null) {
            throw new IllegalArgumentException("request dataId is null");
        }
//        if (request.getConfigData() == null) {
//            throw new IllegalArgumentException("request configData is null");
//        }
        if (request.getGmtCreate() == null) {
            request.setGmtCreate(System.currentTimeMillis());
        }
        if (request.getGmtModified() == null) {
            request.setGmtModified(System.currentTimeMillis());
        }
        if (request.getEventType() == null) {
            throw new IllegalArgumentException("request eventType is null");
        }
    }

    private void validate(Response response) {
        if (response == null) {
            throw new IllegalArgumentException("response is null");
        }
    }
}
