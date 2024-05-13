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
        return Objects.equals(groupName, tagDto.groupName) &&
                Objects.equals(topicName, tagDto.topicName) &&
                Objects.equals(tagRegex, tagDto.tagRegex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, topicName, tagRegex);
    }
}
