package com.taotao.cloud.message.biz.austin.common.domain;

import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/**
 * 简单的埋点信息
 *
 * @author shuigedeng
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class SimpleAnchorInfo {


    /**
     * 具体点位
     */
    private int state;

    /**
     * 业务Id(数据追踪使用)
     * 生成逻辑参考 TaskInfoUtils
     */
    private Long businessId;

    /**
     * 生成时间
     */
    private long timestamp;
}
