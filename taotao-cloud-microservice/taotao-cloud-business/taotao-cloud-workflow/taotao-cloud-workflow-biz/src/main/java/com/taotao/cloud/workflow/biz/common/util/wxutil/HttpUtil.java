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

package com.taotao.cloud.workflow.biz.common.util.wxutil;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.ImmutableMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.SSLContext;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/** */
@Slf4j
public class HttpUtil {

    private HttpUtil() {
        throw new IllegalAccessError("工具类不能实例化");
    }

    private static PoolingHttpClientConnectionManager connectionManager = null;

    private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(5000)
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(3000)
            .build();

    static {
        SSLContext sslcontext = SSLContexts.createSystemDefault();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(1000);
        // 每个路由最大的请求数量
        connectionManager.setDefaultMaxPerRoute(200);
    }

    public static CloseableHttpClient getHttpClient() {
        return getHttpClientBuilder().build();
    }

    public static CloseableHttpClient getHttpClient(SSLContext sslContext) {
        return getHttpClientBuilder(sslContext).build();
    }

    public static HttpClientBuilder getHttpClientBuilder() {
        return HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig);
    }

    public static HttpClientBuilder getHttpClientBuilder(SSLContext sslContext) {
        if (sslContext != null) {
            return getHttpClientBuilder().setSSLContext(sslContext);
        } else {
            return getHttpClientBuilder();
        }
    }

    /**
     * post 请求
     *
     * @param httpUrl 请求地址
     * @param sslContext ssl证书信息
     * @return
     */
    public static String sendHttpPost(String httpUrl, SSLContext sslContext) {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpPost(httpPost, sslContext);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public static String sendHttpPost(String httpUrl) {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpPost(httpPost, null);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param params 参数(格式:key1=value1&key2=value2)
     */
    public static String sendHttpPost(String httpUrl, String params) {
        return sendHttpPost(httpUrl, params, null);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param params 参数(格式:key1=value1&key2=value2)
     * @param sslContext ssl证书信息
     */
    public static String sendHttpPost(String httpUrl, String params, SSLContext sslContext) {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        try {
            // 设置参数
            StringEntity stringEntity = new StringEntity(params, Constants.UTF8);
            stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return sendHttpPost(httpPost, sslContext);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps 参数
     */
    public static String sendHttpPost(String httpUrl, Map<String, String> maps) {
        return sendHttpPost(httpUrl, maps, null);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps 参数
     * @param sslContext ssl证书信息
     */
    public static String sendHttpPost(String httpUrl, Map<String, String> maps, SSLContext sslContext) {
        HttpPost httpPost = wrapHttpPost(httpUrl, maps);
        return sendHttpPost(httpPost, null);
    }

    /**
     * 封装获取HttpPost方法
     *
     * @param httpUrl
     * @param maps
     * @return
     */
    public static HttpPost wrapHttpPost(String httpUrl, Map<String, String> maps) {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> m : maps.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(m.getKey(), m.getValue()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Constants.UTF8));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return httpPost;
    }

    /**
     * 发送 post请求（带文件）
     *
     * @param httpUrl 地址
     * @param file 附件,名称和File对应
     */
    public static String sendHttpPost(String httpUrl, File file) {
        return sendHttpPost(httpUrl, ImmutableMap.of("media", file), null, null);
    }

    /**
     * 发送 post请求（带文件）
     *
     * @param httpUrl 地址
     * @param file 附件,名称和File对应
     * @param maps 参数
     */
    public static String sendHttpPost(String httpUrl, File file, Map<String, String> maps) {
        return sendHttpPost(httpUrl, ImmutableMap.of("media", file), maps, null);
    }

    /**
     * 发送 post请求（带文件）,默认 files 名称数组.
     *
     * @param httpUrl 地址
     * @param fileLists 附件
     * @param maps 参数
     */
    public static String sendHttpPost(String httpUrl, List<File> fileLists, Map<String, String> maps) {
        return sendHttpPost(httpUrl, fileLists, maps, null);
    }

    /**
     * 发送 post请求（带文件）
     *
     * @param httpUrl 地址
     * @param fileMap 附件,名称和File对应
     * @param maps 参数
     */
    public static String sendHttpPost(String httpUrl, Map<String, File> fileMap, Map<String, String> maps) {
        return sendHttpPost(httpUrl, fileMap, maps, null);
    }

    /**
     * 发送 post请求（带文件）,默认 files 名称数组.
     *
     * @param httpUrl 地址
     * @param fileLists 附件
     * @param maps 参数
     * @param sslContext ssl证书信息
     */
    public static String sendHttpPost(
            String httpUrl, List<File> fileLists, Map<String, String> maps, SSLContext sslContext) {
        Map<String, File> fileMap = new HashMap<>(16);
        if (fileLists != null && !fileLists.isEmpty()) {
            for (File file : fileLists) {
                fileMap.put("media", file);
            }
        }
        return sendHttpPost(httpUrl, fileMap, maps, sslContext);
    }

    /**
     * 发送 post请求（带文件）
     *
     * @param httpUrl 地址
     * @param fileMap 附件,名称和File对应
     * @param maps 参数
     * @param sslContext ssl证书信息
     */
    public static String sendHttpPost(
            String httpUrl, Map<String, File> fileMap, Map<String, String> maps, SSLContext sslContext) {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
        if (null != maps) {
            for (Map.Entry<String, String> m : maps.entrySet()) {
                meBuilder.addPart(m.getKey(), new StringBody(m.getValue(), ContentType.TEXT_PLAIN));
            }
        }
        if (null != fileMap) {
            for (Map.Entry<String, File> m : fileMap.entrySet()) {
                FileBody fileBody = new FileBody(m.getValue());
                meBuilder.addPart(m.getKey(), fileBody);
            }
        }

        HttpEntity reqEntity = meBuilder.build();
        httpPost.setEntity(reqEntity);
        return sendHttpPost(httpPost, sslContext);
    }

    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    public static String sendHttpPost(HttpPost httpPost) {
        return sendHttpPost(httpPost, null);
    }

    /**
     * 发送Post请求
     *
     * @param httpPost
     * @param sslConext ssl证书信息
     * @return
     */
    public static String sendHttpPost(HttpPost httpPost, SSLContext sslConext) {
        CloseableHttpClient httpClient = getHttpClient(sslConext);
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, Constants.UTF8);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                // 关闭连接,释放资源
                if (entity != null) {
                    // 会自动释放连接
                    EntityUtils.consumeQuietly(entity);
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return responseContent;
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public static String sendHttpGet(String httpUrl) {
        return sendHttpGet(httpUrl, null);
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     * @param sslConext ssl证书信息
     */
    public static String sendHttpGet(String httpUrl, SSLContext sslConext) {
        // 创建get请求
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpGet(httpGet, sslConext);
    }

    /**
     * 发送Get请求
     *
     * @param httpGet
     * @return
     */
    public static String sendHttpGet(HttpGet httpGet) {
        return sendHttpGet(httpGet, null);
    }

    /**
     * 发送Get请求
     *
     * @param httpGet
     * @param sslConext ssl证书信息
     * @return
     */
    public static String sendHttpGet(HttpGet httpGet, SSLContext sslConext) {
        CloseableHttpClient httpClient = getHttpClient(sslConext);
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, Constants.UTF8);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                // 关闭连接,释放资源
                if (entity != null) {
                    // 会自动释放连接
                    EntityUtils.consumeQuietly(entity);
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return responseContent;
    }

    /**
     * 判断是否微信返回错误
     *
     * @param jsonObject
     * @return
     */
    public static boolean isWxError(JSONObject jsonObject) {
        if (null == jsonObject || jsonObject.getIntValue("errcode") != 0) {
            return true;
        }
        return false;
    }

    /**
     * http请求
     *
     * @param requestUrl url
     * @param requestMethod GET/POST
     * @param outputStr 参数
     * @return
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        return httpRequest(requestUrl, requestMethod, outputStr, null);
    }

    /**
     * http请求
     *
     * @param requestUrl url
     * @param requestMethod GET/POST
     * @param outputStr 参数
     * @return
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr, String... token) {
        JSONObject jsonObject = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(6000);
            conn.setRequestProperty("Content-Type", "application/json");
            if (StringUtil.isNotEmpty(token)) {
                conn.setRequestProperty(Constants.AUTHORIZATION, XSSEscape.escape(token[0]));
                // 处理请求头参数
                if (token.length > 1 && StringUtil.isNotEmpty(token[1])) {
                    Map<String, Object> requestHeader = JsonUtil.stringToMap(token[1]);
                    for (String field : requestHeader.keySet()) {
                        conn.setRequestProperty(field, requestHeader.get(field) + "");
                    }
                }
            }
            String agent = ServletUtil.getUserAgent();
            if (StringUtil.isNotEmpty(agent)) {
                conn.setRequestProperty(Constants.USER_AGENT, agent);
            }
            if (StringUtil.isNotEmpty(outputStr)) {
                @Cleanup OutputStream outputStream = conn.getOutputStream();
                outputStream.write(outputStr.getBytes(Constants.UTF8));
                outputStream.close();
            }
            @Cleanup InputStream inputStream = conn.getInputStream();
            @Cleanup InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Constants.UTF8);
            @Cleanup BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return jsonObject;
    }

    public static void main(String[] args) throws Exception {
        JSONObject param = new JSONObject();
        //        param.put("userId", new String[]{"admin,00b0d34d-a5d6-43f7-a7d4-c8d5fbca8f8f"});
        //        String s1 = JSONObject.toJSONString(param);
        //        String s = param.toString();
        //        String encode = URLEncoder.encode("引sdadsa迈", "UTF-8");
        //        JSONObject jsonObject =
        // httpRequest("http://127.0.0.1:30000/api/permission/Users/getUserList"
        ////                + encode
        //                , "POST", s1, "bearer
        // eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJzaW5nbGVMb2dpbiI6IjEiLCJleHAiOjE2NDY0MjEwMDYsImp0aSI6Ijg4ZDFmMzY5LWRlYzQtNDI2Ny1iNjlkLTMwMzFjNWYzMGU3YyIsImNsaWVudF9pZCI6ImFkbWluIiwidG9rZW4iOiJsb2dpbl90b2tlbl8yNjk0NDc5Nzk0ODI0ODYyMTMifQ.G0cmAtOJxC9k6SoUyc-aS4Q-Us8xE-D5ojpa-1DXmoQRqTvKb7BCYThwrMglNY53c24Qk8HvOBvivzgsfB7Dhtm9CxhK89mDqf_tK34OjHkvH95_mdstItCcKm6uwFa02AsfZRIVoa_d1cYInLCcEDK7Q9pKS9QqkYJCUVxHjeZgD430JaX_wdhtSyTqqA59-OWslTpHSDji4keQcz5Y-Vw1k4jquzNWvBn1BX6HIAnksGpApTZ9uyVB2I5gPKqIs8Z3rHEun2rbQPkwbxZBUrAvKz2pbu74q8LMi2i7HdHxj9lTV2mwbFZURt1K_WH8xt-tfz4LYnaoHz8sQ_VXEA");
        //        LogUtils.info(jsonObject);
        boolean get = httpCronRequest(
                "http://192.168.20.128:30000/api/scheduletask",
                "GET",
                null,
                "bearer"
                        + " eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJzaW5nbGVMb2dpbiI6IjEiLCJleHAiOjE2NDY4MzU2MTEsImp0aSI6ImQ2MTVhZDEyLWI0NmItNDgwOC04YmI2LWNhZTY2Y2QyZDVhYSIsImNsaWVudF9pZCI6ImFkbWluIiwidG9rZW4iOiJsb2dpbl90b2tlbl8yNzExODY5NTgxMTIyNjExMjUifQ.E-sPzi1dFoNh-q-cVHn3jti6cxDm1y2fiH8UVYtAYcsoFwZSvx6oubkUefB7xkE9VhCh_syGLbuYhZvRrUAtY-YfH0GG_sNIQXXrzjjieEtpynyBeCGjjZ2U46InjJhOXDMW9FMQS1VSuIx_Z5FuKV93M7kZcAO4ZxhxYcG1_3R4zscxx2hed8ChQvVWf8nbcnuZBUYffan26Y4Fecvi6b0yGrQE_Tmu1D0TjBnsJ5SugRcA_9IxDHIA2H8NfI4tVmeWMVqvNomjBzJaxWZkuXsA-CTzGUvaz9wpRfskh6qTrpnnNpc5p7KsCfkV_Se6KCvfnmD4eNJ27zRvHg1WJA");
        LogUtils.info(get);
    }

    /**
     * http请求
     *
     * @param requestUrl url
     * @param requestMethod GET/POST
     * @param outputStr 参数
     * @return
     */
    public static boolean httpCronRequest(String requestUrl, String requestMethod, String outputStr, String token) {
        boolean falg = false;
        try {
            URL url = new URL(requestUrl);
            final HttpURLConnection[] conn = {null};
            Callable<String> task = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    // 执行耗时代码
                    try {
                        conn[0] = (HttpURLConnection) url.openConnection();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    conn[0].setDoOutput(true);
                    conn[0].setDoInput(true);
                    conn[0].setUseCaches(false);
                    conn[0].setRequestMethod(requestMethod);
                    conn[0].setRequestProperty("Content-Type", "application/json");
                    if (StringUtil.isNotEmpty(token)) {
                        conn[0].setRequestProperty(Constants.AUTHORIZATION, token);
                    }
                    if (null != outputStr) {
                        @Cleanup OutputStream outputStream = conn[0].getOutputStream();
                        outputStream.write(outputStr.getBytes(Constants.UTF8));
                        outputStream.close();
                    }
                    @Cleanup InputStream inputStream = conn[0].getInputStream();
                    @Cleanup InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Constants.UTF8);
                    @Cleanup BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String str = null;
                    StringBuffer buffer = new StringBuffer();
                    while ((str = bufferedReader.readLine()) != null) {
                        buffer.append(str);
                    }
                    bufferedReader.close();
                    inputStreamReader.close();
                    inputStream.close();
                    conn[0].disconnect();
                    return "url连接ok";
                }
            };
            ThreadPoolTaskExecutor executor = SpringContext.getBean(ThreadPoolTaskExecutor.class);
            Future<String> future = executor.submit(task);
            try {
                // 设置超时时间
                String rst = future.get(3, TimeUnit.SECONDS);
                if ("url连接ok".equals(rst)) {
                    falg = true;
                }
            } catch (TimeoutException e) {
                log.error("连接url超时");
            } catch (Exception e) {
                log.error("获取异常," + e.getMessage());
            }
        } catch (MalformedURLException e) {
            LogUtils.error(e);
        }
        return falg;
    }
}
