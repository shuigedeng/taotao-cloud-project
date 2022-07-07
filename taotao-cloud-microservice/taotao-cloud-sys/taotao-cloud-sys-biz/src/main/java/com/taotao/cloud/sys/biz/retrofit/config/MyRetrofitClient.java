
package com.taotao.cloud.sys.biz.retrofit.config;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.github.lianjiatech.retrofit.spring.boot.log.LogLevel;
import com.github.lianjiatech.retrofit.spring.boot.log.LogStrategy;
import com.github.lianjiatech.retrofit.spring.boot.log.Logging;
import com.github.lianjiatech.retrofit.spring.boot.retry.Retry;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import retrofit2.Converter;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@RetrofitClient(baseUrl = "${test.baseUrl}")
@Logging(logLevel = LogLevel.WARN)
@Retry(intervalMs = 200)
public @interface MyRetrofitClient {

   @AliasFor(annotation = RetrofitClient.class, attribute = "converterFactories")
   Class<? extends Converter.Factory>[] converterFactories() default {JacksonConverterFactory.class};

   @AliasFor(annotation = Logging.class, attribute = "logStrategy")
   LogStrategy logStrategy() default LogStrategy.BODY;
}
