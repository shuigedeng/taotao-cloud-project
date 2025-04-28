package com.taotao.cloud.ccsr.client.starter;

import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.client.CcsrClient;
import com.taotao.cloud.ccsr.client.listener.ConfigListener;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;
import org.springframework.beans.factory.DisposableBean;
import com.taotao.cloud.ccsr.client.request.Payload;
import java.util.concurrent.TimeUnit;

public class CcsrService implements DisposableBean {

	private final CcsrClient ccsrClient;

	public CcsrService(CcsrClient ccsrClient) {
		SpiExtensionFactory.getExtensions(ConfigListener.class).forEach(ConfigListener::register);
		this.ccsrClient = ccsrClient;
	}

	public Response request(Payload request, EventType eventType) {
		request.setEventType(eventType);
		return ccsrClient.request(request);
	}

	@Override
	public void destroy() {
		if (ccsrClient != null) {
			Log.print("销毁CcsrClient...");
			ccsrClient.destroy(3, TimeUnit.SECONDS);
		}
	}
}
