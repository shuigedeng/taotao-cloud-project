package com.taotao.cloud.workflow.biz.common.model.task;

import java.io.Serializable;
import lombok.Data;

/**
 * 本地任务
 *
 */
@Data
public class LocalTaskModel implements Serializable {

    // 展示使用-------------
    /**
     * id
     */
    private String id;

    /**
     * 展示的方法名
     */
    private String fullName;

    /**
     * 方法说明
     */
    private String description;


    // 补充参数-------------
    /**
     * 任务组名
     *
     * @return
     */
    private String jobGroupName;

    /**
     * 表达式
     *
     * @return
     */
    private String cron;

    /**
     * 开始时间
     *
     * @return
     */
    private String startDate;

    /**
     * 结束时间
     *
     * @return
     */
    private String endDate;


    // 反射调用时使用------------------------------------
    /**
     * 类
     */
    private Object clz;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class[] parameterType;

    /**
     * 值
     */
    private Object[] parameterValue;

}
