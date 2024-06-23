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

package com.taotao.cloud.sys.biz.log.src.test.java.com.taotao.cloud.log.biz;

import com.taotao.cloud.log.biz.log.param.LoginLogParam;
import com.taotao.cloud.log.biz.log.service.LoginLogService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaoTaoCloudLogApplicationTests {

    @Autowired
    private LoginLogService loginLogService;

    @Test
    public void contextLoads() {}

    @Test
    public void save() {
        LoginLogParam param = new LoginLogParam();
        param.setUserId(0L);
        param.setAccount("aa");
        param.setLogin(false);
        param.setClient("aaa");
        param.setLoginType("bbb");
        param.setIp("ccc");
        param.setLoginLocation("cc");
        param.setBrowser("vv");
        param.setOs("aa");
        param.setMsg("");
        param.setLoginTime(LocalDateTime.now());
        param.setCurrentPage(0);
        param.setPageSize(0);
        param.setSort("");
        param.setOrder("");
        loginLogService.add(param);
    }
}
