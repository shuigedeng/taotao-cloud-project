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

package com.taotao.cloud.job.common.utils; // package com.taotao.cloud.job.common.utils;
//
// import okhttp3.*;
// import tech.powerjob.common.TtcJobException.PowerJobException;
//
// import java.io.IOException;
// import java.util.concurrent.TimeUnit;
//
/// **
// * 封装 OkHttpClient
// *
// * @author shuigedeng
// * @since 2020/4/6
// */
// public class HttpUtils {
//
//    private static final OkHttpClient client;
//    private static final int HTTP_SUCCESS_CODE = 200;
//
//    static {
//        client = new OkHttpClient.Builder()
//                .connectTimeout(1, TimeUnit.SECONDS)
//                .readTimeout(5, TimeUnit.SECONDS)
//                .build();
//    }
//
//    public static String get(String url) throws IOException {
//        Request request = new Request.Builder()
//                .get()
//                .url(url)
//                .build();
//        return execute(request);
//    }
//
//    public static String post(String url, RequestBody requestBody) throws IOException {
//        Request request = new Request.Builder()
//                .post(requestBody)
//                .url(url)
//                .build();
//        return execute(request);
//    }
//
//    private static String execute(Request request) throws IOException {
//        try (Response response = client.newCall(request).execute()) {
//            int responseCode = response.code();
//            if (responseCode == HTTP_SUCCESS_CODE) {
//                ResponseBody body = response.body();
//                if (body == null) {
//                    return null;
//                }else {
//                    return body.string();
//                }
//            }
//            throw new PowerJobException(String.format("http request failed,code=%d",
// responseCode));
//        }
//    }
//
// }
