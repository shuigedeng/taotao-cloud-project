package com.taotao.cloud.open.api.openapi;

import com.taotao.cloud.open.openapi.annotation.OpenApi;
import com.taotao.cloud.open.openapi.annotation.OpenApiMethod;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@OpenApi("userApi")
public class UserOpenApi {

    @OpenApiMethod("getUserById")
    public String getUserById(Long id) {
        // log.info("getUserById：id=" + id);
        // User user = new User();
        // user.setId(1L);
        // user.setName("张三");
        // return user;
		return "sldfjalsdfjk";
    }
}
