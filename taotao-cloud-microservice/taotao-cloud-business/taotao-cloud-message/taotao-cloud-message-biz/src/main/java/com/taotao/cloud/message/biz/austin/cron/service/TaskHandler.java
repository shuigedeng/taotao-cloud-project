package com.taotao.cloud.message.biz.austin.cron.service;

/**
 * @author 3y
 * @since 2022/2/9
 * 具体处理定时任务逻辑的Handler
 */
public interface TaskHandler {

    /**
     * 处理具体的逻辑
     *
     * @param messageTemplateId
     */
    void handle(Long messageTemplateId);

}
