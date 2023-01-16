package com.taotao.cloud.order.biz.service.business.order.check;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 处理器配置类
 */
@AllArgsConstructor
@Data
public class CheckHandlerConfig {
    /**
     * 处理器Bean名称
     */
    private String handler;
    /**
     * 下一个处理器
     */
    private CheckHandlerConfig next;
    /**
     * 是否降级
     */
    private Boolean down = Boolean.FALSE;
}
