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

package com.taotao.cloud.open.platform.openapiclient;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.openapi.client.OpenApiClient;
import com.taotao.cloud.openapi.client.OpenApiClientBuilder;
import com.taotao.cloud.openapi.client.model.Gender;
import com.taotao.cloud.openapi.client.model.User;
import com.taotao.cloud.openapi.common.enums.AsymmetricCryEnum;
import com.taotao.cloud.openapi.common.enums.CryModeEnum;
import com.taotao.cloud.openapi.common.enums.SymmetricCryEnum;
import com.taotao.cloud.openapi.common.model.OutParams;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class UserApiTest {

    @Value("${keys.local.rsa.privateKey}")
    private String privateKey;

    @Value("${keys.remote.rsa.publicKey}")
    private String remotePublicKey;

    @Value("${openapi.client.config.baseUrl}")
    private String baseUrl;

    /**
     * 定义OpenApiClient
     */
    OpenApiClient apiClient = null;

    @PostConstruct
    public void init() {
        apiClient = new OpenApiClientBuilder(baseUrl, privateKey, remotePublicKey, "001", "userApi")
                .asymmetricCry(AsymmetricCryEnum.RSA)
                .retDecrypt(true)
                .cryModeEnum(CryModeEnum.SYMMETRIC_CRY)
                .symmetricCry(SymmetricCryEnum.AES)
                .enableCompress(false)
                .build();
    }

    public void getUserById() {
        OutParams outParams = apiClient.callOpenApi("getUserById", 10001);
        LogUtils.info("返回值：" + outParams);
    }

    public void saveUser() {
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        OutParams outParams = apiClient.callOpenApi("saveUser", user);
        LogUtils.info("返回值：" + outParams);
    }

    public void batchSaveUser() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        users.add(user);
        OutParams outParams = apiClient.callOpenApi("batchSaveUser", users);
        LogUtils.info("返回值：" + outParams);
    }

    public void batchSaveUser2() {
        User[] users = new User[1];
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        users[0] = user;
        // 仅一个参数且是数组类型，必须转成Object类型，否则会被识别为多个参数
        OutParams outParams = apiClient.callOpenApi("batchSaveUser2", (Object) users);
        LogUtils.info("返回值：" + outParams);
    }

    public void listUsers() {
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        ids.add(3L);
        OutParams outParams = apiClient.callOpenApi("listUsers", ids);
        LogUtils.info("返回值：" + outParams);
    }

    public void listUsers2() {
        Long[] ids = new Long[] {2L, 3L};
        // 仅一个参数且是数组类型，必须转成Object类型，否则会被识别为多个参数
        OutParams outParams = apiClient.callOpenApi("listUsers2", (Object) ids);
        LogUtils.info("返回值：" + outParams);
    }

    public void listUsers3() {
        long[] ids = new long[] {2L, 3L};
        // 仅一个参数且是数组类型,long这种基本类型非包装类型数组不需要强转Object
        OutParams outParams = apiClient.callOpenApi("listUsers3", ids);
        LogUtils.info("返回值：" + outParams);
    }

    public void getAllUsers() {
        // 存在方法级别配置，需构建新的client
        OpenApiClient client = new OpenApiClientBuilder(baseUrl, privateKey, remotePublicKey, "001", "userApi")
                .asymmetricCry(AsymmetricCryEnum.RSA)
                .retDecrypt(false)
                .cryModeEnum(CryModeEnum.ASYMMETRIC_CRY)
                .symmetricCry(SymmetricCryEnum.AES)
                .httpReadTimeout(10)
                .enableCompress(true)
                .build();
        OutParams outParams = client.callOpenApi("getAllUsers");
        LogUtils.info("返回值：" + outParams);
    }

    public void addUser() {
        // 为了精确调用到想要的重载方法，这里将第一个参数转成了Object对象
        OutParams outParams =
                apiClient.callOpenApi("addUser", (Object) "展昭", "13312341234", "1331234@qq.com", Gender.MALE);
        LogUtils.info("返回值：" + outParams);
    }

    public void addUsers() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        users.add(user);
        OutParams outParams = apiClient.callOpenApi("addUsers", 5L, "李寻欢", users);
        LogUtils.info("返回值：" + outParams);
    }
}
