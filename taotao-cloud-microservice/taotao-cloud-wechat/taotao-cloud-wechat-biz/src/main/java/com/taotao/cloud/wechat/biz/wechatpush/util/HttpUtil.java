package com.taotao.cloud.wechat.biz.wechatpush.util;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * 请求http的帮助类
 */
public class HttpUtil {
    static final int retry = 3;
    static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    static ConnectionKeepAliveStrategy myStrategy;

    public HttpUtil() {
    }

    public static String doPost(String url, String data) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).setKeepAliveStrategy(myStrategy).setDefaultRequestConfig(RequestConfig.custom().setStaleConnectionCheckEnabled(true).build()).build();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(20000).setConnectionRequestTimeout(10000).build();
        httpPost.setConfig(requestConfig);
        String context = "";
        if (data != null && data.length() > 0) {
            StringEntity body = new StringEntity(data, "utf-8");
            httpPost.setEntity(body);
        }

        httpPost.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            context = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception var16) {
            var16.getStackTrace();
        } finally {
            try {
                response.close();
                httpPost.abort();
            } catch (Exception var15) {
                var15.getStackTrace();
            }

        }

        return context;
    }

    public static String getUrl(String url) throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String var7;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Connection", "close");
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(18000).setConnectTimeout(5000).setConnectionRequestTimeout(18000).build();
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);

            try {
                Object entity;
                if (response1.getStatusLine().getStatusCode() != 200) {
                    if (response1.getStatusLine().getStatusCode() != 404) {
                        return null;
                    }

                    entity = "";
                    return (String)entity;
                }

                entity = response1.getEntity();
                String result = EntityUtils.toString((HttpEntity)entity);
                EntityUtils.consume((HttpEntity)entity);
                var7 = result;
            } finally {
                response1.close();
            }
        } finally {
            httpclient.close();
        }

        return var7;
    }

    static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception var2) {
            }
        }

    }

    static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

    }

    static {
        connectionManager.setMaxTotal(1000);
        connectionManager.setDefaultMaxPerRoute(1000);
        myStrategy = new ConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                BasicHeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator("Keep-Alive"));

                String param;
                String value;
                do {
                    if (!it.hasNext()) {
                        return 60000L;
                    }

                    HeaderElement he = it.nextElement();
                    param = he.getName();
                    value = he.getValue();
                } while(value == null || !param.equalsIgnoreCase("timeout"));

                return Long.parseLong(value) * 1000L;
            }
        };
    }
}
