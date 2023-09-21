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

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import org.testng.annotations.Test;

/**
 * 菜单测试.
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 * @since 2020-08-19
 */
public class WxMenuControllerTest extends BaseControllerTest {

    @Test
    public void testMenuCreate() {
        given().when()
                .body(new WxMenu())
                .contentType(ContentType.JSON)
                .post("/wx/menu/wxappid/create")
                .then()
                .log()
                .everything();
    }
}
