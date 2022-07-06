package com.taotao.cloud.encrypt.wrapper;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.encrypt.handler.EncryptHandler;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * 加密请求包装器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:12:08
 */
public class EncryptRequestWrapper extends HttpServletRequestWrapper {

	private byte[] body;
	private EncryptHandler encryptHandler;

	public EncryptRequestWrapper(HttpServletRequest request, EncryptHandler encryptHandler) throws IOException, ServletException {
		super(request);
		this.encryptHandler = encryptHandler;
		if (!request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE) && !request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
			throw new ServletException("contentType error");
		}
		ServletInputStream inputStream = request.getInputStream();
		int contentLength = Integer.parseInt(request.getHeader("Content-Length"));
		byte[] bytes = new byte[contentLength];
		int readCount = 0;
		while (readCount < contentLength) {
			readCount += inputStream.read(bytes, readCount, contentLength - readCount);
		}
		body = bytes;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		LogUtil.info("接收到的请求密文：" + new String(body));
		byte[] decode = encryptHandler.decode(body);
		String urlDecodeStr = URLDecoder.decode(new String(decode), StandardCharsets.UTF_8);
		LogUtil.info("解密后的报文：" + urlDecodeStr);
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(urlDecodeStr.getBytes());
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return byteArrayInputStream.available() == 0;
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}

			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}
		};
	}
}
