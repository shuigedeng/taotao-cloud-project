package com.taotao.cloud.wechat.biz.mp;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * jsapi 测试.
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 * @date 2020-04-25
 */
@Test
public class WxJsapiControllerTest extends BaseControllerTest {
    @Test(invocationCount = 1000, threadPoolSize = 5)
    public void testGetJsapiTicket() {
        given()
            .when().get("/wx/jsapi/xxxx/getJsapiTicket")
            .then()
            .log().everything();
    }
}
