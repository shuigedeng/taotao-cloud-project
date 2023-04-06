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

package com.taotao.cloud.message.biz.austin.handler.deduplication.limit;

import cn.hutool.core.util.IdUtil;
import com.taotao.cloud.message.biz.austin.support.utils.RedisUtils;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

/**
 * 滑动窗口去重器（内容去重采用基于redis中zset的滑动窗口去重，可以做到严格控制单位时间内的频次。）
 *
 * @author cao
 * @date 2022-04-20 11:34
 */
@Service(value = "SlideWindowLimitService")
public class SlideWindowLimitService extends AbstractLimitService {

    private static final String LIMIT_TAG = "SW_";

    @Autowired
    private RedisUtils redisUtils;

    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("limit.lua")));
    }

    /**
     * @param service 去重器对象
     * @param taskInfo
     * @param param 去重参数
     * @return 返回不符合条件的手机号码
     */
    @Override
    public Set<String> limitFilter(AbstractDeduplicationService service, TaskInfo taskInfo, DeduplicationParam param) {

        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
        long nowTime = System.currentTimeMillis();
        for (String receiver : taskInfo.getReceiver()) {
            String key = LIMIT_TAG + deduplicationSingleKey(service, taskInfo, receiver);
            String scoreValue = String.valueOf(IdUtil.getSnowflake().nextId());
            String score = String.valueOf(nowTime);
            if (redisUtils.execLimitLua(
                    redisScript,
                    Collections.singletonList(key),
                    String.valueOf(param.getDeduplicationTime() * 1000),
                    score,
                    String.valueOf(param.getCountNum()),
                    scoreValue)) {
                filterReceiver.add(receiver);
            }
        }
        return filterReceiver;
    }
}
