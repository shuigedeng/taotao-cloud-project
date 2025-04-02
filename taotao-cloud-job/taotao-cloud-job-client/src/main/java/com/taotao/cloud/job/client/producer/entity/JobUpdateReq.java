package com.taotao.cloud.job.client.producer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kjob.common.enums.TimeExpressionType;
import org.kjob.common.module.LifeCycle;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobUpdateReq {
    /**
     * 以appName为分组，被该app下的某个subApp所调度
     */
    private String appName;
    private Long jobId;

    private String jobName;
    private String jobDescription;
    private String jobParams;

    private TimeExpressionType timeExpressionType;
    private String timeExpression;
    private LifeCycle lifeCycle;

    private String processorInfo;
    private int maxInstanceNum;


}
