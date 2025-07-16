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

package com.taotao.cloud.mq.client.consumer.dto;

import java.util.Objects;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqTopicTagDto {

    /**
     * 消费者分组名称
     */
    private String groupName;

    /**
     * 标题名称
     */
    private String topicName;

    /**
     * 标签名称
     */
    private String tagRegex;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTagRegex() {
        return tagRegex;
    }

    public void setTagRegex(String tagRegex) {
        this.tagRegex = tagRegex;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MqTopicTagDto tagDto = (MqTopicTagDto) object;
        return Objects.equals(groupName, tagDto.groupName)
                && Objects.equals(topicName, tagDto.topicName)
                && Objects.equals(tagRegex, tagDto.tagRegex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, topicName, tagRegex);
    }
}
