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

package com.taotao.cloud.message.biz.austin.handler.deduplication.service;

import org.dromara.hutoolcrypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author 3y
 * @date 2021/12/11 内容去重服务（默认5分钟相同的文案发给相同的用户去重）
 */
@Service
public class ContentDeduplicationService extends AbstractDeduplicationService {

    @Autowired
    public ContentDeduplicationService(@Qualifier("SlideWindowLimitService") LimitService limitService) {
        this.limitService = limitService;
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    /**
     * 内容去重 构建key
     *
     * <p>key: md5(templateId + receiver + content)
     *
     * <p>相同的内容相同的模板短时间内发给同一个人
     *
     * @param taskInfo
     * @return
     */
    @Override
    public String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return DigestUtil.md5Hex(
                taskInfo.getMessageTemplateId() + receiver + JSON.toJSONString(taskInfo.getContentModel()));
    }
}
