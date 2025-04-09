package com.taotao.cloud.job.worker.processor;

import lombok.*;

/**
 * processor执行结果
 *
 * @author shuigedeng
 * @since 2020/3/18
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResult {

    private boolean success = false;

    private String msg;

    public ProcessResult(boolean success) {
        this.success = success;
    }
}
