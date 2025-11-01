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

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.openapi.client.model.Gender;
import com.taotao.boot.openapi.client.model.Role;
import com.taotao.boot.openapi.client.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class UserApiTest2 {

    @Autowired
    UserApiClient userApiClient;

    @Autowired
    RoleApiClient roleApiClient;

    public void getRoleById() {
        Role role = roleApiClient.getRoleById(1000L);
        LogUtils.info("返回值：" + role);
    }

    public void getUserById() {
        User user = userApiClient.getUserById(10001L);
        LogUtils.info("返回值：" + user);
    }

    public void saveUser() {
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        Boolean aBoolean = userApiClient.saveUser(user);
        LogUtils.info("返回值：" + aBoolean);
    }

    public void batchSaveUser() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        users.add(user);
        userApiClient.batchSaveUser(users);
        LogUtils.info("返回值：void");
    }

    public void batchSaveUser2() {
        User[] users = new User[1];
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        users[0] = user;
        userApiClient.batchSaveUser(users);
        LogUtils.info("返回值：void");
    }

    public void listUsers() {
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        ids.add(3L);
        List<User> users = userApiClient.listUsers(ids);
        LogUtils.info("返回值：" + users);
    }

    public void listUsers2() {
        Long[] ids = new Long[] {2L, 3L};
        List<User> users = userApiClient.listUsers2(ids);
        LogUtils.info("返回值：" + users);
    }

    public void listUsers3() {
        long[] ids = new long[] {2L, 3L};
        List<User> users = userApiClient.listUsers3(ids);
        LogUtils.info("返回值：" + users);
    }

    public void getAllUsers() {
        List<User> users = userApiClient.getAllUsers();
        LogUtils.info("返回值：" + users);
    }

    public void getAllUsersMap() {
        Map<Long, User> userMap = userApiClient.getAllUsersMap();
        LogUtils.info("返回值：" + JSONUtil.toJsonStr(userMap));
    }

    public void addUser() {
        // 为了精确调用到想要的重载方法，这里将第一个参数转成了Object对象
        User user = userApiClient.addUser("展昭", "13312341234", "1331234@qq.com", Gender.MALE);
        LogUtils.info("返回值：" + user);
    }

    public void addUsers() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        users.add(user);
        User userRet = userApiClient.addUser(5L, "李寻欢", users);
        LogUtils.info("返回值：" + userRet);
    }
}
