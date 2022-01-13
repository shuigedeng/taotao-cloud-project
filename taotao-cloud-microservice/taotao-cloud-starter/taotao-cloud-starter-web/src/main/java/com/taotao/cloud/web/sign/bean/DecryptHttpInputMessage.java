package com.taotao.cloud.web.sign.bean;

import java.io.InputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

/**
 * <p>解密信息输入流</p>
 *
 * @since 2019年4月16日13:45:42
 */
public class DecryptHttpInputMessage implements HttpInputMessage {

	private InputStream body;

	private HttpHeaders headers;

	public DecryptHttpInputMessage() {
	}

	public DecryptHttpInputMessage(InputStream body, HttpHeaders headers) {
		this.body = body;
		this.headers = headers;
	}

	/**
	 * @return InputStream body
	 */
	@Override
	public InputStream getBody() {
		return body;
	}

	/**
	 * @return HttpHeaders headers
	 */
	@Override
	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setBody(InputStream body) {
		this.body = body;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}
}
