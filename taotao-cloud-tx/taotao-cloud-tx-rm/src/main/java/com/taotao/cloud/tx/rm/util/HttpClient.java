package com.taotao.cloud.tx.rm.util;

import com.zhuzi.distributedtx.transactional.ZhuziTxParticipant;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

// HttpClient远程调用工具
public class HttpClient {

    // GET请求的方法
    public static String get(String url) {
        String result = "";
        try {
            // 创建一个httpClient对象，并调用传入的URL接口
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
//            httpGet.addHeader("Content-type", "application/json");
//            httpGet.addHeader("groupId", ZhuziTxParticipant.getCurrentGroupId());
//            httpGet.addHeader("transactionalCount", String.valueOf(ZhuziTxParticipant.getTransactionCount()));
            CloseableHttpResponse response = httpClient.execute(httpGet);

            // 如果调用结果是返回OK，状态码为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 获取response对象中的所有响应头
                Header[] allHeaders = response.getAllHeaders();
                for (Header header : allHeaders) {
                    // 从中找到上游服务传递的组ID、事务数量，并赋值给自己的子事务
                    if ("groupId".equals(header.getName())){
                        String groupId = header.getValue();
                        ZhuziTxParticipant.setCurrentGroupId(groupId);
                        System.err.println("参数：" + groupId + "，值：" + ZhuziTxParticipant.getCurrentGroupId());
                    }
                    if ("transactionalCount".equals(header.getName())){
                        String transactionalCount = header.getValue();
                        ZhuziTxParticipant.setTransactionCount(
                                Integer.valueOf(transactionalCount == null ? "0" : transactionalCount));
                    }
                }
                // 向调用方返回上游服务最终的调用结果
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
            response.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
