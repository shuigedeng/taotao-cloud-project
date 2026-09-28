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

package com.taotao.cloud.data.analysis.clickhouse.controller;

import com.taotao.cloud.data.analysis.clickhouse.service.CkUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2021/03/16 17:09
 */
@RestController
public class CkUserController {

    @Autowired private CkUserService userService;

    @RequestMapping("/queryUser")
    public Object query() {
        List userList = userService.testUseMapperInterface();
        return userList.toString();
    }

    //	@RequestMapping("/insertUser")
    //	public Object insertUser() {
    //		User user = new User();
    ////		user.setAppId("SS");
    ////		user.setRegTime(new Date());
    ////		user.setUserId(777744);
    ////		user.setVersion("3.2");
    //		Integer flag = userMapper.insertUser(user);
    //		return flag;
    //	}
}
