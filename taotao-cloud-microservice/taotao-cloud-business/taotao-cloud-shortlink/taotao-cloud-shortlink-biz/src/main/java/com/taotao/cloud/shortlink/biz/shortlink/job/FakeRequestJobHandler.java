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

package com.taotao.cloud.shortlink.biz.shortlink.job;

import com.taotao.cloud.log.api.api.enums.ShortLinkDomainTypeEnum;
import com.taotao.cloud.log.api.api.request.ShortLinkCreateRequest;
import com.taotao.cloud.shortlink.biz.shortlink.rpc.ShortLinkServiceImpl;
import com.xxl.tool.response.Response;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class FakeRequestJobHandler extends IJobHandler {

    //    @Resource
    //    private ShortLinkService shortLinkService;

    @Resource
    private ShortLinkServiceImpl shortLinkService;

    @XxlJob(value = "fakeRequest")
    @Override
    public Response<String> execute(String param) throws Exception {
        XxlJobLogger.log("XXL-JOB, fakeRequest.");
        Random random = new Random();

        List<ShortLinkCreateRequest> requests = new ArrayList<>(5000);
        for (int i = 0; i < 5000; i++) {
            String url = "https://estate.zc.net/details/"
                    + i
                    + "/estateid/"
                    + random.nextInt(2000000000)
                    + "?time="
                    + System.currentTimeMillis();
            ShortLinkCreateRequest createRequest = ShortLinkCreateRequest.builder()
                    .title("房源-" + random.nextInt(100000000))
                    .originalUrl(url)
                    .domainType(ShortLinkDomainTypeEnum.ORIGIN.getCode())
                    .groupId((long) (random.nextInt(4) + 1))
                    .domainId(1L)
                    .accountId(1L)
                    .expired(LocalDate.now().plusDays(random.nextInt(10000)))
                    .build();

            requests.add(createRequest);
        }

        try {
            shortLinkService.tempBatchCreateCode(requests);
        } catch (Exception e) {
            log.warn("FakeRequestJobHandler: e -> {}", e.toString());
        }

        return SUCCESS;
    }
}
