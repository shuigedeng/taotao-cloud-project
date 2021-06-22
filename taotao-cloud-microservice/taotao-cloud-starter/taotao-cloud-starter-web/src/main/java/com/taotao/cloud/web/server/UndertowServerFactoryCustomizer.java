package com.taotao.cloud.web.server;

import io.undertow.connector.ByteBufferPool;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import java.io.IOException;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

/**
 * 解决undertow警告Buffer pool was not set on WebSocketDeploymentInfo
 *
 * @author pangu
 */
public class UndertowServerFactoryCustomizer implements
	WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

	@Override
	public void customize(UndertowServletWebServerFactory factory) {
		UndertowDeploymentInfoCustomizer undertowDeploymentInfoCustomizer = deploymentInfo -> {
			WebSocketDeploymentInfo webSocketDeploymentInfo = (WebSocketDeploymentInfo) deploymentInfo.getServletContextAttributes().get(WebSocketDeploymentInfo.ATTRIBUTE_NAME);
			deploymentInfo
				.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo",
					webSocketDeploymentInfo);

			XnioWorker worker = getXnioWorker();
			webSocketDeploymentInfo.setWorker(worker);

			ByteBufferPool buffers = new DefaultByteBufferPool(Boolean.getBoolean("io.undertow.websockets.direct-buffers"), 1024, 100, 12);
			webSocketDeploymentInfo.setBuffers(buffers);
		};
		factory.addDeploymentInfoCustomizers(undertowDeploymentInfoCustomizer);

//		factory.addDeploymentInfoCustomizers(deploymentInfo -> {
//			WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
//			webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(false, 1024));
//			deploymentInfo
//				.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo",
//					webSocketDeploymentInfo);
//		});
	}

	private XnioWorker getXnioWorker() {
		XnioWorker worker = null;
		try {
			worker = Xnio.getInstance().createWorker(OptionMap.create(Options.THREAD_DAEMON, true));
		} catch (IOException ignored) {
		}
		return worker;
	}
}
