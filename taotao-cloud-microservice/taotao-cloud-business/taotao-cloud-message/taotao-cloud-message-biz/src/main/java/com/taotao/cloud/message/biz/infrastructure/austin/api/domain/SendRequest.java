package com.taotao.cloud.message.biz.infrastructure.austin.api.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送/撤回接口的参数
 *
 * @author shuigedeng
 */
@Data

@AllArgsConstructor
@NoArgsConstructor

public class SendRequest {

    /**
     * 执行业务类型
     *
     * @see com.taotao.cloud.message.biz.austin.service.api.enums.BusinessCode
     * send:发送消息
     * recall:撤回消息
     */
    private String code;

    /**
     * 消息模板Id
     * [必填]
     */
    private Long messageTemplateId;


    /**
     * 消息相关的参数
     * 当业务类型为"send"，必传
     */
    private MessageParam messageParam;

    /**
     * 需要撤回的消息messageIds (可根据发送接口返回的消息messageId进行撤回)
     * [可选]
     */
    private List<String> recallMessageIds;

}
