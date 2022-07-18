package com.taotao.cloud.third.client.support.retrofit;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.Intercept;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.third.client.support.retrofit.config.TimeStampInterceptor;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@RetrofitClient(baseUrl = "${test.baseUrl}")
@Intercept(handler = TimeStampInterceptor.class, include = {"/api/**"}, exclude = "/api/test/savePerson")
//@Intercept(handler = TimeStamp2Interceptor.class) // 需要多个，直接添加即可
public interface CustomInterceptApi {

    @GET("person")
    Result<String> getPerson(@Query("id") Long id);

    @POST("savePerson")
    Result<String> savePerson(@Body Result person);
}
