/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.api.dto.quartz;


import java.io.Serializable;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuartzLogDto implements Serializable {

    /** 定时任务名称 */
    private String beanName;

    /** Bean名称  */
    private Timestamp createTime;

    /** cron表达式 */
    private String cronExpression;

    /** 异常详细  */
    private String exceptionDetail;

    /** 状态 */
    private Boolean isSuccess;

    /** 任务名称 */
    private String jobName;

    /** 方法名称 */
    private String methodName;

    /** 参数 */
    private String params;

    /** 耗时（毫秒） */
    private Long time;

}
