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

package com.taotao.cloud.wechat.biz.mp;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;

/**
 * 公共测试方法和参数.
 *
 * @author Binary Wang
 * @since 2019-06-14
 */
public abstract class BaseControllerTest {
    private static final String ROOT_URL = "http://127.0.0.1:8080/";

    @BeforeTest
    public void setup() {
        RestAssured.baseURI = ROOT_URL;
    }
}
