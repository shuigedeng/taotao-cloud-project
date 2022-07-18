package com.taotao.cloud.third.client.support.retrofit;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.third.client.support.retrofit.sign.Sign;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


@RetrofitClient(baseUrl = "${test.baseUrl}")
@Sign(accessKeyId = "${test.accessKeyId}", accessKeySecret = "${test.accessKeySecret}", exclude = {"/api/test/person"})
public interface CustomSignApi {

	@GET("person")
	Result<String> getPerson(@Query("id") Long id);

	@POST("savePerson")
	Result<String> savePerson(@Body Result person);
}
