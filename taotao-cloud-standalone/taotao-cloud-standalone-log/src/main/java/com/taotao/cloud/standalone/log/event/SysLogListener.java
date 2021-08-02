package com.taotao.cloud.standalone.log.event;

import com.taotao.cloud.standalone.log.domain.SysLog;
import com.taotao.cloud.standalone.log.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Classname SysLogListener
 * @Description 注解形式的监听 异步监听日志事件
 * @Author shuigedeng
 * @since 2019-04-28 11:34
 * @Version 1.0
 */
@Component
public class SysLogListener {

    @Autowired
    private ISysLogService sysLogService;

    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        SysLog sysLog = (SysLog) event.getSource();
        // 保存日志
        sysLogService.save(sysLog);
    }
}
