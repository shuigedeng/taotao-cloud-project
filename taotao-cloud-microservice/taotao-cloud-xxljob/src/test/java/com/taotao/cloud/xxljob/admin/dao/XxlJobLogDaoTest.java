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

import com.xxl.job.admin.core.model.XxlJobLog;
import jakarta.annotation.Resource;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class XxlJobLogDaoTest {

    @Resource private XxlJobLogDao xxlJobLogDao;

    @Test
    public void test() {
        List<XxlJobLog> list = xxlJobLogDao.pageList(0, 10, 1, 1, null, null, 1);
        int list_count = xxlJobLogDao.pageListCount(0, 10, 1, 1, null, null, 1);

        XxlJobLog log = new XxlJobLog();
        log.setJobGroup(1);
        log.setJobId(1);

        long ret1 = xxlJobLogDao.save(log);
        XxlJobLog dto = xxlJobLogDao.load(log.getId());

        log.setTriggerTime(new Date());
        log.setTriggerCode(1);
        log.setTriggerMsg("1");
        log.setExecutorAddress("1");
        log.setExecutorHandler("1");
        log.setExecutorParam("1");
        ret1 = xxlJobLogDao.updateTriggerInfo(log);
        dto = xxlJobLogDao.load(log.getId());

        log.setHandleTime(new Date());
        log.setHandleCode(2);
        log.setHandleMsg("2");
        ret1 = xxlJobLogDao.updateHandleInfo(log);
        dto = xxlJobLogDao.load(log.getId());

        List<Long> ret4 = xxlJobLogDao.findClearLogIds(1, 1, new Date(), 100, 100);

        int ret2 = xxlJobLogDao.delete(log.getJobId());
    }
}
