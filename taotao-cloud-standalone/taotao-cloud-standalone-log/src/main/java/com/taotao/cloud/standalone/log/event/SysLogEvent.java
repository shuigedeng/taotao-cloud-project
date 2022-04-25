package com.taotao.cloud.standalone.log.event;

import com.taotao.cloud.standalone.log.domain.SysLog;
import org.springframework.context.ApplicationEvent;

/**
 * @Classname SysLogEvent
 * @Description 系统日志事件
 * @Author shuigedeng
 * @since 2019-04-28 11:34
 * 
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(SysLog sysLog) {
        super(sysLog);
    }
}
