package com.taotao.cloud.ccsr.client.client.filter;

import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.client.client.AbstractClient;
import com.taotao.cloud.ccsr.client.context.CcsrContext;
import com.taotao.cloud.ccsr.client.option.RequestOption;
import com.taotao.cloud.ccsr.common.enums.ResponseCode;
import com.taotao.cloud.ccsr.common.utils.GsonUtils;
import com.taotao.cloud.ccsr.common.utils.MD5Utils;
import  com.taotao.cloud.ccsr.client.request.Payload;
public class ConvertFilter<OPTION extends RequestOption> extends AbstractFilter<OPTION> {

	public ConvertFilter(AbstractClient<OPTION> client) {
		super(client);
	}

	@Override
	protected Response doPreFilter(CcsrContext context, OPTION option, Payload request) {

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
	protected Response doPostFilter(CcsrContext context, OPTION option, Payload request, Response response) {
		// TODO
		return response;
	}
}
