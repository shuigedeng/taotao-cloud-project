package com.taotao.cloud.job.client.producer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.taotao.cloud.common.enums.TimeExpressionType;
import com.taotao.cloud.common.module.LifeCycle;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobCreateReq {
    /**
     * 以appName为分组，被该app下的某个subApp所调度
     */
    private String appName;

    private String jobName;
    private String jobDescription;
    private String jobParams;

    private TimeExpressionType timeExpressionType;
    private String timeExpression;
    private LifeCycle lifeCycle;

    private String processorInfo;
    private int maxInstanceNum;


}
