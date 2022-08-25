package com.taotao.cloud.monitor.kuding.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CurrentRequestHeaderResolver {

	default Map<String, String> headers(HttpServletRequest httpRequest, List<String> headerNames) {
		Map<String, String> map = new HashMap<String, String>();
		if (headerNames.size() > 0) {
			headerNames.forEach(x -> {
				String value = httpRequest.getHeader(x);
				if (value != null) {
					map.put(x, value);
				}
			});
		} else {
			Enumeration<String> enumeration = httpRequest.getHeaderNames();
			while (enumeration.hasMoreElements()) {
				String str = enumeration.nextElement();
				map.put(str, httpRequest.getHeader(str));
			}
		}
		return map;
	}
}
