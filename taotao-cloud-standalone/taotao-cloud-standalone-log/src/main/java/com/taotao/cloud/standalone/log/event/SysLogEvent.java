package com.taotao.cloud.standalone.log.event;

import com.taotao.cloud.standalone.log.domain.SysLog;
import org.springframework.context.ApplicationEvent;

/**
 * @Classname SysLogEvent
 * @Description 系统日志事件
 * @Author 李号东 lihaodongmail@163.com
 * @since 2019-04-28 11:34
 * @Version 1.0
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(SysLog sysLog) {
        super(sysLog);
    }
}
