package com.taotao.cloud.third.client.support.retrofit.config;

import com.github.lianjiatech.retrofit.spring.boot.interceptor.GlobalInterceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SourceGlobalInterceptor implements GlobalInterceptor {

   //@Autowired
   //private TestService testService;

   @Override
   public Response intercept(Chain chain) throws IOException {
      Request request = chain.request();
      Request newReq = request.newBuilder()
              .addHeader("source", "test")
              .build();
      //testService.test();
      return chain.proceed(newReq);
   }
}
