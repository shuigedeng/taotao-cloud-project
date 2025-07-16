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

package com.taotao.cloud.xxljob.admin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.xxl.job.admin.service.impl.LoginService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class JobInfoControllerTest extends AbstractSpringMvcTest {
    private static Logger logger = LoggerFactory.getLogger(JobInfoControllerTest.class);

    private Cookie cookie;

    @BeforeEach
    public void login() throws Exception {
        MvcResult ret =
                mockMvc.perform(
                                post("/login")
                                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                        .param("userName", "admin")
                                        .param("password", "123456"))
                        .andReturn();
        cookie = ret.getResponse().getCookie(LoginService.LOGIN_IDENTITY_KEY);
    }

    @Test
    public void testAdd() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("jobGroup", "1");
        parameters.add("triggerStatus", "-1");

        MvcResult ret =
                mockMvc.perform(
                                post("/jobinfo/pageList")
                                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                        // .content(paramsJson)
                                        .params(parameters)
                                        .cookie(cookie))
                        .andReturn();

        logger.info(ret.getResponse().getContentAsString());
    }
}
