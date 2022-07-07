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
package com.taotao.cloud.sys.biz.retrofit;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.biz.retrofit.model.Person;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * UserApi
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-24 15:40
 */
@RetrofitClient(baseUrl = "http://www.baidu.com")
public interface HttpApi {

	@POST("getString")
	String getString(@Body Person person);

	@GET("person")
	Result<Person> getPerson(@Query("id") Long id);

	@GET("person")
	CompletableFuture<Result<Person>> getPersonCompletableFuture(@Query("id") Long id);

	@POST("savePerson")
	Void savePersonVoid(@Body Person person);

	@GET("person")
	Response<Result<Person>> getPersonResponse(@Query("id") Long id);

	@GET("person")
	Call<Result<Person>> getPersonCall(@Query("id") Long id);

	@GET("person")
	Mono<Result<Person>> monoPerson(@Query("id") Long id);

	@GET("person")
	Single<Result<Person>> singlePerson(@Query("id") Long id);

	@GET("ping")
	Completable ping();

}
