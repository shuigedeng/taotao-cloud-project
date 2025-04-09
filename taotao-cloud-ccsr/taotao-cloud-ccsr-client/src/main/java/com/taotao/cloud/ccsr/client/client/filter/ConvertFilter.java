package com.taotao.cloud.ccsr.client.client.filter;

import com.taotao.cloud.ccsr.api.grpc.auto.MetadataType;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import org.ohara.msc.client.AbstractClient;
import org.ohara.msc.common.enums.ResponseCode;
import org.ohara.msc.common.utils.GsonUtils;
import org.ohara.msc.common.utils.MD5Utils;
import org.ohara.msc.context.OHaraMcsContext;
import org.ohara.msc.listener.ConfigData;
import org.ohara.msc.option.RequestOption;
import org.ohara.msc.request.Payload;

public class ConvertFilter<OPTION extends RequestOption> extends AbstractFilter<OPTION> {

    public ConvertFilter(AbstractClient<OPTION> client) {
        super(client);
    }

    @Override
    protected Response doPreFilter(OHaraMcsContext context, OPTION option, Payload request) {

        try {
            String configDataString = GsonUtils.getInstance().toJson(request.getConfigData());
            String md5 = MD5Utils.calculateMD5(configDataString);
            context.setConfigDataString(configDataString);
            context.setNamespace(request.getNamespace());
            context.setMd5(md5);
        } catch (Exception e) {
            return ResponseHelper.error(ResponseCode.CLIENT_ERROR.getCode(), e.getMessage());
        }

        return null;
    }

    @Override
    protected Response doPostFilter(OHaraMcsContext context, OPTION option, Payload request, Response response) {
        // TODO
        return response;
    }
}
