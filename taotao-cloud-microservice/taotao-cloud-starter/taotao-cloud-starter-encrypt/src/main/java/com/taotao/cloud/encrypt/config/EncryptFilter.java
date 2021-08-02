package com.taotao.cloud.encrypt.config;

import com.taotao.cloud.encrypt.handler.EncryptHandler;
import com.taotao.cloud.encrypt.handler.InitHandler;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

/**
 * 加密过滤器
 *
 * @author gaoyang
 */
public class EncryptFilter implements Filter {

	private EncryptHandler encryptHandler;

	private static AtomicBoolean isEncryptAnnotation = new AtomicBoolean(false);
	private final static Set<String> encryptCacheUri = new HashSet<>();

	public EncryptFilter(EncryptHandler encryptHandler) {
		this.encryptHandler = encryptHandler;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		if (this.isEncryptAnnotation.get()) {
			if (checkUri(((HttpServletRequest) servletRequest).getRequestURI())) {
				this.chain(servletRequest, servletResponse, filterChain);
			} else {
				filterChain.doFilter(servletRequest, servletResponse);
			}
		} else {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			String contentType = request.getContentType();
			if (request.getRequestURI().startsWith("/actuator") || contentType == null || !contentType.toLowerCase().equals(MediaType.APPLICATION_JSON_VALUE)) {
				filterChain.doFilter(servletRequest, servletResponse);
			} else {
				this.chain(servletRequest, servletResponse, filterChain);
			}
		}
	}

	private void chain(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		EncryptRequestWrapper request = new EncryptRequestWrapper((HttpServletRequest) servletRequest, encryptHandler);
		EncryptResponseWrapper response = new EncryptResponseWrapper((HttpServletResponse) servletResponse);
		filterChain.doFilter(request, response);
		byte[] responseData = response.getResponseData();
		if (responseData.length == 0) {
			return;
		}
		log.info("接收的报文：" + new String(responseData));
		byte[] encryptByte = encryptHandler.encode(URLEncoder.encode(new String(responseData), "UTF-8").getBytes());
		log.info("加密后的报文：" + new String(encryptByte));
		servletResponse.setContentLength(-1);
		servletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		ServletOutputStream outputStream = servletResponse.getOutputStream();
		log.info("outputStream: " + outputStream.toString());
		outputStream.write(encryptByte);
		outputStream.flush();
	}


	private boolean checkUri(String uri) {
		uri = uri.replaceAll("//+", "/");
		if (uri.endsWith("/")) {
			uri = uri.substring(0, uri.length() - 1);
		}
		if (encryptCacheUri.contains(uri)) {
			return true;
		}
		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		InitHandler.handler(filterConfig, encryptCacheUri, isEncryptAnnotation);
	}


	@Override
	public void destroy() {

	}

}
