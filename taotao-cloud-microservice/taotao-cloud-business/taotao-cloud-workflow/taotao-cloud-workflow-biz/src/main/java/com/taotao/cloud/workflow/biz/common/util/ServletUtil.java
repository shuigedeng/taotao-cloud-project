/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.workflow.biz.common.util;

import com.alibaba.fastjson2.JSONObject;
import java.io.IOException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Cleanup;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** */
public class ServletUtil {

    /** 获取ServletPath */
    public static String getServletPath() {
        return ServletUtil.getRequest().getServletPath();
    }

    /** 获取Request Payload */
    public static String getPayload() {
        try {
            @Cleanup ServletInputStream is = ServletUtil.getRequest().getInputStream();
            int nRead = 1;
            int nTotalRead = 0;
            byte[] bytes = new byte[10240 * 20];
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0) {
                    nTotalRead = nTotalRead + nRead;
                }
            }
            String str = new String(bytes, 0, nTotalRead, Constants.UTF_8);
            return str;
        } catch (IOException e) {
            LogUtils.error(e);
            return "";
        }
    }

    /** 获取User-Agent */
    public static String getUserAgent() {
        return ServletUtil.getHeader("User-Agent");
    }

    /** 判断是否是手机端登陆 */
    public static boolean getIsMobileDevice() {
        return isMobileDevice(ServletUtil.getUserAgent());
    }

    /** 获取HTTP头信息 */
    public static String getHeader(String name) {
        if (getRequest() != null) {
            return getRequest().getHeader(name);
        }
        return null;
    }

    /** 获取表单参数 */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /** 获取String参数 */
    public static String getParameter(String name, String defaultValue) {
        return ConvertUtil.toStr(getRequest().getParameter(name), defaultValue);
    }

    /** 获取Integer参数 */
    public static Integer getParameterToInt(String name) {
        return ConvertUtil.toInt(getRequest().getParameter(name));
    }

    /** 获取Integer参数 */
    public static Integer getParameterToInt(String name, Integer defaultValue) {
        return ConvertUtil.toInt(getRequest().getParameter(name), defaultValue);
    }

    /** 获取request */
    public static HttpServletRequest getRequest() {
        if (getRequestAttributes() != null) {
            return getRequestAttributes().getRequest();
        }
        return null;
    }

    /** 获取response */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /** 获取session */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/json");
            response.setCharacterEncoding(Constants.UTF_8);
            response.getWriter().print(string);
        } catch (IOException e) {
            LogUtils.error(e);
        }
        return null;
    }

    /**
     * 是否是Ajax异步请求
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1) {
            return true;
        }
        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
            return true;
        }
        String uri = request.getRequestURI();
        if (inStringIgnoreCase(uri, ".json", ".xml")) {
            return true;
        }
        String ajax = request.getParameter("__ajax");
        if (inStringIgnoreCase(ajax, "json", "xml")) {
            return true;
        }
        return false;
    }

    /**
     * 是否包含字符串
     *
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase((s.trim()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /** 返回JSONObject对象 */
    public static JSONObject getJsonObject() throws Exception {
        String builder = ServletUtil.getPayload();
        return JSONObject.parseObject(builder);
    }

    /**
     * 判断是否是移动设备
     *
     * @param requestHeader
     * @return
     */
    public static boolean isMobileDevice(String requestHeader) {
        String[] deviceArray = new String[] {"android", "windows phone", "iphone", "ios", "ipad", "mqqbrowser"};
        if (requestHeader == null) {
            return false;
        }
        requestHeader = requestHeader.toLowerCase();
        for (int i = 0; i < deviceArray.length; i++) {
            if (requestHeader.indexOf(deviceArray[i]) > 0) {
                return true;
            }
        }
        return false;
    }
}
