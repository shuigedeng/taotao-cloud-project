/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.job.common;

/**
 * 系统生成的任务实例运行结果
 *
 * @author shuigedeng
 * @since 2020/4/11
 */
public class SystemInstanceResult {

    private SystemInstanceResult() {}

    /* *********** 普通instance 专用 *********** */

    /**
     * 同时运行的任务实例数过多
     */
    public static final String TOO_MANY_INSTANCES = "too many instances(%d>%d)";

    /**
     *  无可用worker
     */
    public static final String NO_WORKER_AVAILABLE = "no worker available";

    /**
     * 任务执行超时
     */
    public static final String INSTANCE_EXECUTE_TIMEOUT = "instance execute timeout";

    /**
     * 任务执行超时，成功打断任务
     */
    public static final String INSTANCE_EXECUTE_TIMEOUT_INTERRUPTED =
            "instance execute timeout,interrupted success";

    /**
     * 任务执行超时，强制终止任务
     */
    public static final String INSTANCE_EXECUTE_TIMEOUT_FORCE_STOP =
            "instance execute timeout,force stop success";

    /**
     * 用户手动停止任务，成功打断任务
     */
    public static final String USER_STOP_INSTANCE_INTERRUPTED =
            "user stop instance,interrupted success";

    /**
     * 用户手动停止任务，被系统强制终止
     */
    public static final String USER_STOP_INSTANCE_FORCE_STOP =
            "user stop instance,force stop success";

    /**
     * 创建根任务失败
     */
    public static final String TASK_INIT_FAILED = "create root task failed";

    /**
     * 未知错误
     */
    public static final String UNKNOWN_BUG = "unknown bug";

    /**
     * TaskTracker 长时间未上报
     */
    public static final String REPORT_TIMEOUT = "worker report timeout, maybe TaskTracker down";

    public static final String CAN_NOT_FIND_JOB_INFO = "can't find job info";

    /* *********** workflow 专用 *********** */

    public static final String MIDDLE_JOB_FAILED = "middle job failed";
    public static final String MIDDLE_JOB_STOPPED = "middle job stopped by user";
    public static final String CAN_NOT_FIND_JOB = "can't find some job";
    public static final String CAN_NOT_FIND_NODE = "can't find some node";
    public static final String ILLEGAL_NODE = "illegal node info";

    /**
     * 没有启用的节点
     */
    public static final String NO_ENABLED_NODES = "no enabled nodes";

    /**
     * 被用户手动停止
     */
    public static final String STOPPED_BY_USER = "stopped by user";

    public static final String CANCELED_BY_USER = "canceled by user";

    /**
     * 无效 DAG
     */
    public static final String INVALID_DAG = "invalid dag";

    /**
     * 被禁用的节点
     */
    public static final String DISABLE_NODE = "disable node";

    /**
     * 标记为成功的节点
     */
    public static final String MARK_AS_SUCCESSFUL_NODE = "mark as successful node";
}
