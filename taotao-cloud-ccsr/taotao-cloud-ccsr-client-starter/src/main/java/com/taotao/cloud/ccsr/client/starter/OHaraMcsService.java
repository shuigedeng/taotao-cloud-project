package com.taotao.cloud.ccsr.client.starter;

import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.client.OHaraMcsClient;
import com.taotao.cloud.ccsr.client.listener.ConfigListener;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;
import org.springframework.beans.factory.DisposableBean;
import com.taotao.cloud.ccsr.client.request.Payload;
import java.util.concurrent.TimeUnit;

public class OHaraMcsService implements DisposableBean {

	private final OHaraMcsClient oHaraMcsClient;

	public OHaraMcsService(OHaraMcsClient oHaraMcsClient) {
		SpiExtensionFactory.getExtensions(ConfigListener.class).forEach(ConfigListener::register);
		this.oHaraMcsClient = oHaraMcsClient;
	}

	public Response request(Payload request, EventType eventType) {
		request.setEventType(eventType);
		return oHaraMcsClient.request(request);
	}

	@Override
	public void destroy() {
		if (oHaraMcsClient != null) {
			Log.print("销毁OHaraMcsClient...");
			oHaraMcsClient.destroy(3, TimeUnit.SECONDS);
		}
	}
}
