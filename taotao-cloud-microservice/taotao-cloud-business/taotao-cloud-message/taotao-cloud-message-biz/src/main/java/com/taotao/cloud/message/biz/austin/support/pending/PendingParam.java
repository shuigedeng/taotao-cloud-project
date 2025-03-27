package com.taotao.cloud.message.biz.austin.support.pending;


import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;
import lombok.experimental.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;


/**
 * @author shuigedeng
 * pending初始化参数类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@Accessors(chain = true)
public class PendingParam<T> {

    /**
     * 消费线程池实例【必填】
     */
    protected ExecutorService executorService;
    /**
     * 阻塞队列实现类【必填】
     */
    private BlockingQueue<T> queue;
    /**
     * batch 触发执行的数量阈值【必填】
     */
    private Integer numThreshold;
    /**
     * batch 触发执行的时间阈值，单位毫秒【必填】
     */
    private Long timeThreshold;

}
