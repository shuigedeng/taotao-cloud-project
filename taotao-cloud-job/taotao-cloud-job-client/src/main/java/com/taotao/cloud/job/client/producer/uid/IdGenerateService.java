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

package com.taotao.cloud.job.client.producer.uid;

import com.taotao.cloud.job.common.utils.net.MyNetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 唯一ID生成服务，使用 Twitter snowflake 算法
 * 机房ID：固定为0，占用2位
 * 机器ID：由 ServerIdProvider 提供
 *
 * @author shuigedeng
 * @since 2020/4/6
 */
@Slf4j
public class IdGenerateService {

    private final SnowFlakeIdGenerator snowFlakeIdGenerator;

    private static final int DATA_CENTER_ID = 0;

    public IdGenerateService() {
        String ip = MyNetUtil.address;
        snowFlakeIdGenerator = new SnowFlakeIdGenerator(DATA_CENTER_ID, ip);
        log.info("[IdGenerateService] initialize IdGenerateService successfully, IP:{}", ip);
    }

    /**
     * 分配分布式唯一ID
     * @return 分布式唯一ID
     */
    public long allocate() {
        return snowFlakeIdGenerator.nextId();
    }
}
