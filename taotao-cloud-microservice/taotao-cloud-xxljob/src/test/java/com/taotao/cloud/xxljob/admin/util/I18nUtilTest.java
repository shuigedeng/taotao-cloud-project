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

package com.taotao.cloud.xxljob.admin.util;

import com.xxl.job.admin.core.util.I18nUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * email util test
 *
 * @author xuxueli 2017-12-22 17:16:23
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class I18nUtilTest {
    private static Logger logger = LoggerFactory.getLogger(I18nUtilTest.class);

    @Test
    public void test() {
        logger.info(I18nUtil.getString("admin_name"));
        logger.info(I18nUtil.getMultString("admin_name", "admin_name_full"));
        logger.info(I18nUtil.getMultString());
    }
}
