package com.taotao.cloud.third.client.support.retrofit.config;

import com.github.lianjiatech.retrofit.spring.boot.core.SourceOkHttpClientRegistrar;
import com.github.lianjiatech.retrofit.spring.boot.core.SourceOkHttpClientRegistry;
import com.taotao.cloud.common.utils.log.LogUtils;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

@Component
public class StandardSourceOkHttpClientRegistrar implements SourceOkHttpClientRegistrar {

	@Override
	public void register(SourceOkHttpClientRegistry registry) {

		// 替换默认的SourceOkHttpClient
		// registry.register(Constants.DEFAULT_SOURCE_OK_HTTP_CLIENT, new OkHttpClient.Builder()
		//         .addInterceptor(chain -> {
		//            LogUtils.info("============替换默认的SourceOkHttpClient=============");
		//            return chain.proceed(chain.request());
		//         })
		//         .build());

		// 添加新的SourceOkHttpClient
		registry.register("testSourceOkHttpClient", new OkHttpClient.Builder()
			.addInterceptor(chain -> {
				LogUtils.info("============使用testSourceOkHttpClient=============");
				return chain.proceed(chain.request());
			})
			.build());
	}
}
