package com.taotao.cloud.job.core.worker.processor.task;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Task执行结果
 *
 * @author shuigedeng
 * @since 2020/4/17
 */
@Data
public class TaskResult {

    private String taskId;
    private boolean success;
    private String result;

}
