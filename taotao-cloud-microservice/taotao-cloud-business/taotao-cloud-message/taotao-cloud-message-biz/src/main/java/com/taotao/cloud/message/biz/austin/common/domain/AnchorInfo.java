package com.taotao.cloud.message.biz.austin.common.domain;

import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 埋点信息
 *
 * @author shuigedeng
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class AnchorInfo {
    /**
     * 消息唯一Id(数据追踪使用)
     * 生成逻辑参考 TaskInfoUtils
     */
    private String bizId;

    /**
     * 消息唯一Id(数据追踪使用)
     * 生成逻辑参考 TaskInfoUtils
     */
    private String messageId;
    /**
     * 发送用户
     */
    private Set<String> ids;

    /**
     * 具体点位
     *
     * @see com.taotao.cloud.message.biz.austin.common.enums.AnchorState
     */
    private int state;

    /**
     * 业务Id(数据追踪使用)
     * 生成逻辑参考 TaskInfoUtils
     */
    private Long businessId;


    /**
     * 日志生成时间
     */
    private long logTimestamp;

}
