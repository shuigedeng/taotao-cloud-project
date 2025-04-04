package com.taotao.cloud.message.biz.austin.api.domain;

import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;
import lombok.experimental.*;

import java.util.List;

/**
 * 发送接口的参数
 * batch
 *
 * @author shuigedeng
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
public class BatchSendRequest {


    /**
     * 执行业务类型
     * 必传,参考 BusinessCode枚举
     */
    private String code;


    /**
     * 消息模板Id
     * 必传
     */
    private Long messageTemplateId;


    /**
     * 消息相关的参数
     * 必传
     */
    private List<MessageParam> messageParamList;


}
