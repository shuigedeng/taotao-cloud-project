package com.taotao.cloud.message.biz.austin.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 钉钉 自定义机器人 + 工作通知
 * <p>
 * <a href="https://open.dingtalk.com/document/group/custom-robot-access">https://open.dingtalk.com/document/group/custom-robot-access</a>
 * <p>
 * <a href="https://open.dingtalk.com/document/orgapp-server/asynchronous-sending-of-enterprise-session-messages">https://open.dingtalk.com/document/orgapp-server/asynchronous-sending-of-enterprise-session-messages</a>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingDingContentModel extends ContentModel {

    /**
     * 发送类型
     */
    private String sendType;

    /**
     * 【文本消息】需要发送的内容
     */
    private String content;

    /**
     * 图片、文件、语音消息 需要发送使用的素材ID字段
     */
    private String mediaId;

    // ...
}
