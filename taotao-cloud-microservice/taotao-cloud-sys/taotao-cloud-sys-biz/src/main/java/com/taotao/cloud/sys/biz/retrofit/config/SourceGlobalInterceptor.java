package com.taotao.cloud.sys.biz.retrofit.config;

import com.github.lianjiatech.retrofit.spring.boot.interceptor.GlobalInterceptor;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
