package com.taotao.cloud.message.biz.austin.handler.pending;

import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import com.taotao.cloud.message.biz.austin.handler.config.HandlerThreadPoolConfig;
import com.taotao.cloud.message.biz.austin.handler.utils.GroupIdMappingUtils;
import com.taotao.cloud.message.biz.austin.support.utils.ThreadPoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;


/**
 * 存储 每种消息类型 与 TaskPending 的关系
 *
 * @author shuigedeng
 */
@Component
public class TaskPendingHolder {
    /**
     * 获取得到所有的groupId
     */
    private static List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();
    @Autowired
    private ThreadPoolUtils threadPoolUtils;

    /**
     * 给每个渠道，每种消息类型初始化一个线程池
     */
    @PostConstruct
    public void init() {
        /**
         * example ThreadPoolName:austin.im.notice
         *
         * 可以通过apollo配置：dynamic-tp-apollo-dtp.yml  动态修改线程池的信息
         */
        for (String groupId : groupIds) {
            DtpExecutor executor = HandlerThreadPoolConfig.getExecutor(groupId);
            threadPoolUtils.register(executor);
        }
    }

    /**
     * 得到对应的线程池
     *
     * @param groupId
     * @return
     */
    public ExecutorService route(String groupId) {
        return DtpRegistry.getExecutor(HandlerThreadPoolConfig.PRE_FIX + groupId);
    }


}
