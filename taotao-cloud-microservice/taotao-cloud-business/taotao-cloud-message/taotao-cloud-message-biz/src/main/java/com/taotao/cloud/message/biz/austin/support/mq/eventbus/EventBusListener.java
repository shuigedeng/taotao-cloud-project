package com.taotao.cloud.message.biz.austin.support.mq.eventbus;


import com.taotao.cloud.message.biz.austin.common.domain.RecallTaskInfo;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;

import java.util.List;

/**
 * @author shuigedeng
 * 监听器
 */
public interface EventBusListener {


    /**
     * 消费消息
     *
     * @param lists
     */
    void consume(List<TaskInfo> lists);

    /**
     * 撤回消息
     *
     * @param recallTaskInfo
     */
    void recall(RecallTaskInfo recallTaskInfo);
}
