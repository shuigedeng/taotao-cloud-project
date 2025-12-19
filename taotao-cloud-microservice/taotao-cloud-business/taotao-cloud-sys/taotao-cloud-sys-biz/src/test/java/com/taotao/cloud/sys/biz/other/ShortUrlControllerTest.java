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

package com.taotao.cloud.sys.biz.other;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.alibaba.fastjson2.JSON;
import com.taotao.boot.common.utils.log.LogUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shorturl.server.server.application.dto.UrlRequest;

/**
 * ShortUrlControllerTest
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ShortUrlServerApplication.class)
@AutoConfigureMockMvc
public class ShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    public ShortUrlControllerTest() {
    }

    private String shortUrl = null;

    @Test
    @DisplayName("测试长链接接口")
    public void test() throws Exception {
        UrlRequest url = new UrlRequest();
        url.setLongUrl("https://blog.csdn.net/linsongbin1/article/details/83574619");
        LogUtils.info(url.getLongUrl().length());

        MvcResult mvcResult = mockMvc.perform(post("/shortUrlServer/getShortUrl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(url)))
                .andReturn();

        // shortUrl = mvcResult.getResponse().getContentType().
        LogUtils.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("测试长链接接口")
    public void testShort() throws Exception {
        UrlRequest url = new UrlRequest();
        url.setShortUrl("http://t.cn/eRKFcl9M");
        LogUtils.info(url.getShortUrl().length());

        MvcResult mvcResult = mockMvc.perform(get("/shortUrlServer/getLongUrl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(url)))
                .andReturn();
        LogUtils.info(mvcResult.getResponse().getContentAsString());
    }
}
