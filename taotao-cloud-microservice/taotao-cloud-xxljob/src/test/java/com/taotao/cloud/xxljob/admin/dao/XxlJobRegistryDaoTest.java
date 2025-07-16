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

package com.taotao.cloud.xxljob.admin.dao;

import com.xxl.job.admin.core.model.XxlJobRegistry;
import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class XxlJobRegistryDaoTest {

    @Resource private XxlJobRegistryDao xxlJobRegistryDao;

    @Test
    public void test() {
        int ret = xxlJobRegistryDao.registrySaveOrUpdate("g1", "k1", "v1", new Date());
        /*int ret = xxlJobRegistryDao.registryUpdate("g1", "k1", "v1", new Date());
        if (ret < 1) {
            ret = xxlJobRegistryDao.registrySave("g1", "k1", "v1", new Date());
        }*/

        List<XxlJobRegistry> list = xxlJobRegistryDao.findAll(1, new Date());

        int ret2 = xxlJobRegistryDao.removeDead(Arrays.asList(1));
    }

    @Test
    public void test2() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            new Thread(
                            () -> {
                                int ret =
                                        xxlJobRegistryDao.registrySaveOrUpdate(
                                                "g1", "k1", "v1", new Date());
                                System.out.println(ret);

                                /*int ret = xxlJobRegistryDao.registryUpdate("g1", "k1", "v1", new Date());
                                if (ret < 1) {
                                    ret = xxlJobRegistryDao.registrySave("g1", "k1", "v1", new Date());
                                }*/
                            })
                    .start();
        }

        TimeUnit.SECONDS.sleep(10);
    }
}
