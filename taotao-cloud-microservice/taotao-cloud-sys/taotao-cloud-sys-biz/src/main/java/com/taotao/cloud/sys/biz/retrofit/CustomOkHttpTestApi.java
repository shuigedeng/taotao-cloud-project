
package com.taotao.cloud.sys.biz.retrofit;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.taotao.cloud.common.model.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;

@RetrofitClient(baseUrl = "${test.baseUrl}", sourceOkHttpClient = "testSourceOkHttpClient")
public interface CustomOkHttpTestApi {

    @GET("person")
    Result<String> getPerson(@Query("id") Long id);
}
