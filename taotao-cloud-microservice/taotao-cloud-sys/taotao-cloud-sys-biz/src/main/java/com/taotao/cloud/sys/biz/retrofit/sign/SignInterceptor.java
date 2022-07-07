
package com.taotao.cloud.sys.biz.retrofit.sign;

import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;
import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
public class SignInterceptor extends BasePathMatchInterceptor {

    private String accessKeyId;

    private String accessKeySecret;

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    @Override
    public Response doIntercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newReq = request.newBuilder()
                .addHeader("accessKeyId", accessKeyId)
                .addHeader("accessKeySecret", accessKeySecret)
                .build();
        return chain.proceed(newReq);
    }
}
