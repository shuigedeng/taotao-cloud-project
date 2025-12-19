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

package com.taotao.cloud.rpc.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * IpUtils
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class IpUtils {

    public static boolean valid( String ip ) {
        if (ip.equals("localhost")) {
            return true;
        }
        String z = ip.replace(".", ""); // 用空字符替代点
        int x = ip.length() - z.length(); // 点的个数
        int t = ip.indexOf("..");
        if (t < 0) // 判断连续点
        {
            boolean y = z.matches("[0-9]+"); // 判断除点外的字符是不是数字
            if (!y
                    || !Character.isDigit(ip.charAt(0))
                    || !Character.isDigit(ip.charAt(ip.length() - 1))) {
                return false;
            } else if (x == 3) // 判断点的个数
            {
                int b = ip.indexOf('.'); // 第一个点的位置
                String c = ip.substring(0, ip.indexOf('.')); // 截取第一个数
                int i = Integer.parseInt(c); // 第一个数
                String d = ip.substring(b + 1); // 截取第一个点后面的数
                int e = d.indexOf('.'); // 第二个点的位置
                String f = d.substring(0, e); // 截取第二个数
                int j = Integer.parseInt(f); // 第二个数
                String g = d.substring(e + 1); // 截取第二个点后面的数
                int h = g.indexOf('.'); // 第三个点的位置
                String l = g.substring(0, h); // 截取第三个数
                int k = Integer.parseInt(l); // 第三个数
                String m = g.substring(h + 1); // 截取第三个点后面的数
                int n = Integer.parseInt(m); // 第四个数
                if (( i >= 0 && i <= 255 )
                        && ( j >= 0 && j <= 255 )
                        && ( k >= 0 && k <= 255 )
                        && ( n >= 0 && n <= 255 )) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 目前较为稳定
     */
    public static String getPubIpAddr() {
        String ip = "";
        String chinaz = "https://ip.chinaz.com";

        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while (( read = in.readLine() ) != null) {
                inputLine.append(read + "\r\n");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("get public IP address error: ", e);
        } catch (IOException e) {
            throw new RuntimeException("get public IP address error: " + e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            String ipstr = m.group(1);
            ip = ipstr;
        }
        return ip;
    }

    /**
     * 方法描述：获取公网ip（不稳定）
     */
    public static String getPubIpAddr0() {
        try {
            URL url = new URL("https://pv.sohu.com/cityjson?ie=utf-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String s = "";
            StringBuffer sb = new StringBuffer("");
            String webContent = "";
            while (( s = br.readLine() ) != null) {
                sb.append(s + "\r\n");
            }
            br.close();
            webContent = sb.toString();
            int start = webContent.indexOf("{");
            int end = webContent.indexOf("}") + 1;
            webContent = webContent.substring(start, end);
            CitySN target = JsonUtils.jsonToPojo(webContent, CitySN.class);
            return target.getCip();
        } catch (Exception e) {
            log.warn("Primary interface query failed, secondary interface query is being called");
            return getPubIpAddr1();
        }
    }

    /**
     * 方法描述：获取公网ip（接口稳定，ip不稳定）
     */
    private static String getPubIpAddr1() {
        String url = "https://ip.renfei.net/";
        // 创建CloseableHttpClient
        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(url);
        /**
         * Accept -- value
         * xml、text/xml、application/xml ==> xml
         * text、text/plain ==> ip
         * text/html、application/xhtml+xml ==> html
         */
        httpPost.setHeader("Accept", "text/plain");
        String result = "127.0.0.1";
        try {
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getCode();
            if (statusCode != 200) {
                log.error("statusCode={}", statusCode);
                log.error("responseEntity={}", response.getEntity());
                response.close();
                throw new RuntimeException("get public IP address error");
            }
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (IOException e) {
            throw new RuntimeException("get public IP address error");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String getInterIP1() throws Exception {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static String getInterIP2() throws SocketException {
        String localip = null; // 本地IP
        String netip = null; // 外网IP
        Enumeration<NetworkInterface> netInterfaces;
        netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false; // 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {
                    // 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {
                    // 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }
        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }

    public static void main( String[] args ) throws Exception {
        for (int i = 0; i < 10; i++) {
            String pubIpAddr = getPubIpAddr();
            System.out.println(pubIpAddr);
        }
    }
}

/**
 * CitySN
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
class CitySN {

    private String cip;
    private String cid;
    private String cname;

    public String getCip() {
        return cip;
    }

    public void setCip( String cip ) {
        this.cip = cip;
    }

    public String getCid() {
        return cid;
    }

    public void setCid( String cid ) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname( String cname ) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "CitySN{" + "cip='" + cip + '\'' + ", cid=" + cid + ", cname='" + cname + '\'' + '}';
    }
}
