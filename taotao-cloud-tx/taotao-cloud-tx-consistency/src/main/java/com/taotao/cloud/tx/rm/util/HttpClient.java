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

package com.taotao.cloud.tx.rm.util;

// HttpClient远程调用工具
public class HttpClient {

    //    // GET请求的方法
    //    public static String get(String url) {
    //        String result = "";
    //        try {
    //            // 创建一个httpClient对象，并调用传入的URL接口
    //            CloseableHttpClient httpClient = HttpClients.createDefault();
    //            HttpGet httpGet = new HttpGet(url);
    ////            httpGet.addHeader("Content-type", "application/json");
    ////            httpGet.addHeader("groupId", TtcTxParticipant.getCurrentGroupId());
    ////            httpGet.addHeader("transactionalCount",
    // String.valueOf(TtcTxParticipant.getTransactionCount()));
    //            CloseableHttpResponse response = httpClient.execute(httpGet);
    //
    //            // 如果调用结果是返回OK，状态码为200
    //            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
    //                // 获取response对象中的所有响应头
    //                Header[] allHeaders = response.getAllHeaders();
    //                for (Header header : allHeaders) {
    //                    // 从中找到上游服务传递的组ID、事务数量，并赋值给自己的子事务
    //                    if ("groupId".equals(header.getName())){
    //                        String groupId = header.getValue();
    //                        TtcTxParticipant.setCurrentGroupId(groupId);
    //                        System.err.println("参数：" + groupId + "，值：" +
    // TtcTxParticipant.getCurrentGroupId());
    //                    }
    //                    if ("transactionalCount".equals(header.getName())){
    //                        String transactionalCount = header.getValue();
    //                        TtcTxParticipant.setTransactionCount(
    //                                Integer.valueOf(transactionalCount == null ? "0" :
    // transactionalCount));
    //                    }
    //                }
    //                // 向调用方返回上游服务最终的调用结果
    //                result = EntityUtils.toString(response.getEntity(), "utf-8");
    //            }
    //            response.close();
    //
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //
    //        return result;
    //    }
}
