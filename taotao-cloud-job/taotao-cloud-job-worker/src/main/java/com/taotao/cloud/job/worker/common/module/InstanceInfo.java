package com.taotao.cloud.job.worker.common.module;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName instance_info
 */
@Getter
@Setter
public class InstanceInfo implements Serializable {
    /**
     *
     * -- GETTER --
     *

     */
    private Long id;

    /**
     *
     * -- GETTER --
     *

     */
    private Long actualTriggerTime;
    // 处理器类型（JavaBean、Jar、脚本等）
    private String processorType;
    // 处理器信息
    private String processorInfo;

    /**
     *
     * -- GETTER --
     *

     */
    private Long appId;

    /**
     *
     * -- GETTER --
     *

     */
    private Long expectedTriggerTime;

    /**
     *
     * -- GETTER --
     *

     */
    private Long finishedTime;

    /**
     *
     * -- GETTER --
     *

     */
    private Date gmtCreate;

    /**
     *
     * -- GETTER --
     *

     */
    private Date gmtModified;

    /**
     *
     * -- GETTER --
     *

     */
    private Long instanceId;

    /**
     *
     * -- GETTER --
     *

     */
    private String instanceParams;

    /**
     *
     * -- GETTER --
     *

     */
    private Long jobId;

    /**
     *
     * -- GETTER --
     *

     */
    private String jobParams;

    /**
     *
     * -- GETTER --
     *

     */
    private Long lastReportTime;

    /**
     *
     * -- GETTER --
     *

     */
    private String result;

    /**
     *
     * -- GETTER --
     *

     */
    private Long runningTimes;

    /**
     *
     * -- GETTER --
     *

     */
    private Integer status;

    /**
     *
     * -- GETTER --
     *

     */
    private String taskTrackerAddress;

    /**
     *
     * -- GETTER --
     *

     */
    private Integer type;

    /**
     *
     * -- GETTER --
     *

     */
   }
