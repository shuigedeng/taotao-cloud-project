package com.taotao.cloud.job.worker.processor.task;

import lombok.Data;

/**
 * Task执行结果
 *
 * @author tjq
 * @since 2020/4/17
 */
@Data
public class TaskResult {

    private String taskId;
    private boolean success;
    private String result;

}
