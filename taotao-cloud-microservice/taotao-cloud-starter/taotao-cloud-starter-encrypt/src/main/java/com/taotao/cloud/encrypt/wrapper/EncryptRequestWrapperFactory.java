package com.taotao.cloud.encrypt.wrapper;

import com.taotao.cloud.encrypt.handler.EncryptHandler;
import org.springframework.http.MediaType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 请求加密包装器工厂
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:12:35
 */
public class EncryptRequestWrapperFactory {

	/**
	 * 得到包装
	 *
	 * @param request        请求
	 * @param encryptService 加密服务
	 * @return {@link HttpServletRequest }
	 * @since 2022-07-06 15:12:35
	 */
	public static HttpServletRequest getWrapper(HttpServletRequest request,
		EncryptHandler encryptService) throws IOException, ServletException {
		String contentType = request.getContentType();
		int contentLength = request.getContentLength();
		if (contentType == null || contentLength == 0) {
			return request;
		}
		contentType = contentType.toLowerCase();
		if (contentIsJson(contentType)) {
			return new EncryptBodyRequestWrapper(request, encryptService);
		}
		return request;
	}

	/**
	 * 获取缓存经
	 *
	 * @param request 请求
	 * @return {@link HttpServletRequest }
	 * @since 2022-07-06 15:12:35
	 */
	public static HttpServletRequest getCacheWarpper(HttpServletRequest request)
		throws IOException, ServletException {
		if (!"POST".equalsIgnoreCase(request.getMethod()) ||
			!contentIsJson(request.getContentType())) {
			return request;
		}
		return new CacheRequestWrapper(request);
	}

	/**
	 * json内容
	 *
	 * @param contentType 内容类型
	 * @return boolean
	 * @since 2022-07-06 15:12:36
	 */
	public static boolean contentIsJson(String contentType) {
		return contentType.equals(MediaType.APPLICATION_JSON_VALUE.toLowerCase()) ||
			contentType.equals(MediaType.APPLICATION_JSON_UTF8_VALUE.toLowerCase());
	}
}
