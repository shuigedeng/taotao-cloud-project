package com.taotao.cloud.web.docx4j.output.utils;

import org.springframework.http.HttpHeaders;

/**
 * {@link javax.servlet.http.HttpServletRequest}工具
 */
public interface HttpRequestUtil {
    /**
     * 判断当前请求是否来自ie系列浏览器
     * @return true/false
     */
    static boolean isIeBrowser() {
        // 用户代理头
        String userAgent = HttpRequestUtil.getHeader(HttpHeaders.USER_AGENT).toUpperCase();

        return
            // 是否是ie浏览器
            userAgent.contains("MSIE")
                // 是否是edge浏览器
                || userAgent.contains("EDGE")
                // 是否是ie内核浏览器
                || userAgent.contains("TRIDENT");
    }

    /**
     * 获得请求头
     * @param name 请求头名称
     * @return 请求头值
     */
    static String getHeader(String name) {
        return HttpServletUtil.getCurrentRequest().getHeader(name);
    }
}
