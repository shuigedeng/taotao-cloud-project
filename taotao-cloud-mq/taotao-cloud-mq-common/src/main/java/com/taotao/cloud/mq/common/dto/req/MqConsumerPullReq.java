package com.taotao.cloud.mq.common.dto.req;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqConsumerPullReq extends MqCommonReq {

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 拉取大小
     */
    private int size;

    /**
     * 标题名称
     */
    private String topicName;

    /**
     * 标签正则
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "MqConsumerPullReq{" +
                "groupName='" + groupName + '\'' +
                ", size=" + size +
                ", topicName='" + topicName + '\'' +
                ", tagRegex='" + tagRegex + '\'' +
                "} " + super.toString();
    }

}
