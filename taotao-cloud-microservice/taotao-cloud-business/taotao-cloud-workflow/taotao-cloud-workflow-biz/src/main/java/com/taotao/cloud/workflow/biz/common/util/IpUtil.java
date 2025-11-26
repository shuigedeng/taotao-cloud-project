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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

/** */
@Slf4j
public class IpUtil {

    /** 检测ip信息的网站 */
    private static final String IP_URL =
            "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query={ip}&resource_id=6006";
    /** IP的正则 */
    private static Pattern pattern = Pattern.compile("(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\."
            + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\."
            + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\."
            + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})");

    /**
     * 内网IP
     *
     * @return
     */
    private static List<Pattern> ipFilterRegexList = new ArrayList<>();

    static {
        Set<String> ipFilter = new HashSet<String>();
        ipFilter.add("^10\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])"
                + "\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])"
                + "\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])$");
        // B类地址范围: 172.16.0.0---172.31.255.255
        ipFilter.add("^172\\.(1[6789]|2[0-9]|3[01])\\"
                + ".(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])\\"
                + ".(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])$");
        // C类地址范围: 192.168.0.0---192.168.255.255
        ipFilter.add("^192\\.168\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])\\"
                + ".(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])$");
        ipFilter.add("127.0.0.1");
        ipFilter.add("0.0.0.0");
        ipFilter.add("localhost");
        for (String tmp : ipFilter) {
            ipFilterRegexList.add(Pattern.compile(tmp));
        }
    }

    public static String getIpAddr() {
        HttpServletRequest request = ServletUtil.getRequest();
        String xIp = request.getHeader("X-Real-IP");
        String xFor = request.getHeader("X-Forwarded-For");
        if (StringUtil.isNotEmpty(xFor) && !"unKnown".equalsIgnoreCase(xFor)) {
            int index = xFor.indexOf(",");
            if (index != -1) {
                return xFor.substring(0, index);
            } else {
                return xFor;
            }
        }
        xFor = xIp;
        if (StringUtil.isNotEmpty(xFor) && !"unKnown".equalsIgnoreCase(xFor)) {
            return xFor;
        }
        if (StringUtil.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtil.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtil.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = request.getRemoteAddr();
        }
        String ip = "0:0:0:0:0:0:0:1".equals(xFor) ? "127.0.0.1" : xFor;
        return ip;
    }

    /**
     * 检查IP是否合法
     *
     * @param ip
     * @return
     */
    public static boolean isValid(String ip) {
        Matcher m = pattern.matcher(ip);
        return m.matches();
    }

    /** 获取ip信息 */
    private static JSONObject getIpInfo(String ip) {
        JSONObject data = null;
        if (!ipIsInner(ip)) {
            long begin = System.currentTimeMillis();
            try {
                String ipUrl = IP_URL.replace("{ip}", ip);
                URL url = new URL(ipUrl);
                HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setRequestMethod("GET");
                httpUrlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                httpUrlConnection.setDoInput(true);
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setReadTimeout(5000);
                @Cleanup InputStream inputStream = httpUrlConnection.getInputStream();
                @Cleanup ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int len = 0;
                while (true) {
                    len = inputStream.read(b);
                    if (len == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(b, 0, len);
                }
                long end = System.currentTimeMillis() - begin;
                byte[] lens = byteArrayOutputStream.toByteArray();
                String result = new String(lens, "GBK");
                data = JSONObject.parseObject(result);
                if (null == data) {
                    return data;
                }
                data = JSONObject.parseObject(data.getJSONArray("data").get(0).toString());
            } catch (Exception e) {
                log.error("ip信息获取失败，请检查ip接口工具IPUtil。");
            }
        }
        return data;
    }

    /** 获取ip所在的城市和宽带属于哪一家 */
    public static String getIpCity(String ip) {
        String ipInfo = null;
        if (ip != null) {
            JSONObject data = getIpInfo(ip);
            if (null == data) {
                if ("127.0.0.1".equals(ip) || "localhost".equals(ip)) {
                    ipInfo = "本地连接";
                } else {
                    ipInfo = "本地局域网";
                }
                return ipInfo;
            }
            ipInfo = data.getString("location");
            return ipInfo;
        }
        return ipInfo;
    }

    /**
     * 判断IP是否内网IP
     *
     * @param ip @Title: ipIsInner
     * @return: boolean
     */
    public static boolean ipIsInner(String ip) {
        boolean isInnerIp = false;
        for (Pattern tmp : ipFilterRegexList) {
            Matcher matcher = tmp.matcher(ip);
            if (matcher.find()) {
                isInnerIp = true;
                break;
            }
        }
        return isInnerIp;
    }
}
