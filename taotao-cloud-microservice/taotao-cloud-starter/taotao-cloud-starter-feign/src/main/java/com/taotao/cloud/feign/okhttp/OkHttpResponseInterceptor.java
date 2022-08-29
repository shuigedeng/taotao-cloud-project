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

package com.taotao.cloud.feign.okhttp;

import com.taotao.cloud.common.utils.log.LogUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Objects;

/**
 * <p>Description: OkHttp 响应拦截器 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-02 09:32:18
 */
public class OkHttpResponseInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);

        if (response.code() == 200) {
            ResponseBody responseBody = response.body();
            if (responseBody != null && responseBody.contentLength() != 0 && Objects.requireNonNull(responseBody.contentType()).type().equals(MediaType.APPLICATION_JSON_VALUE)) {
                String str = responseBody.string();
	            LogUtils.info("sdfasdf");
                //JsonObject jsonObject = JsonParser.parseString(str).getAsJsonObject();
                //String data = jsonObject.get("data").getAsString();
                //if (StringUtils.isNotBlank(data)) {
                //    ResponseBody body = ResponseBody.create(data, okhttp3.MediaType.get(MediaType.APPLICATION_JSON_VALUE));
                //    return response.newBuilder().body(body).build();
                //}
            }
        }

        return response;
    }
}
