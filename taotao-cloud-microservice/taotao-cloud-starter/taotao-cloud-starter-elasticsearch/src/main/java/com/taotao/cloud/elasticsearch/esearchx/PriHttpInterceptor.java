package com.taotao.cloud.elasticsearch.esearchx;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Http 请求拦截器
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:09:42
 */
class PriHttpInterceptor implements Interceptor {
    public static final PriHttpInterceptor instance = new PriHttpInterceptor();

    @Override
    public Response intercept(Chain chain) throws IOException {
        PriHttpTimeout timeout = chain.request().tag(PriHttpTimeout.class);

        if (timeout != null) {
            if (timeout.connectTimeout > 0) {
                chain.withConnectTimeout(timeout.connectTimeout, TimeUnit.SECONDS);
            }

            if (timeout.writeTimeout > 0) {
                chain.withWriteTimeout(timeout.writeTimeout, TimeUnit.SECONDS);
            }

            if (timeout.readTimeout > 0) {
                chain.withReadTimeout(timeout.readTimeout, TimeUnit.SECONDS);
            }
        }

        return chain.proceed(chain.request());
    }
}
