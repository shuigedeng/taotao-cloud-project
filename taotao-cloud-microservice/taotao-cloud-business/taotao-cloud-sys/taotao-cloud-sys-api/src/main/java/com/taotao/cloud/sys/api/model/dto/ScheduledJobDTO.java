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

package com.taotao.cloud.sys.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

@Schema(title = "scheduled定时任务")
public class ScheduledJobDTO {

    // 任务id
    @Schema(description = "主键 创建时不传，更新时传", example = "1")
    private String id;

    // 任务名
    @Schema(description = "任务名", example = "1")
    private String name;

    /*
    目标字符串
    格式bean.method(params)
    String字符串类型，包含'、boolean布尔类型，等于true或者false
    long长整形，包含L、double浮点类型，包含D、其他类型归类为整形
    */
    @Schema(description = "目标字符串", example = "demoJob.handleMessage('aaa', true, 500L, 1.23D)")
    private String invokeTarget;

    // 周期(month、week、day、hour、minute、secods)
    @Schema(description = "周期(month、week、day、hour、minute、secods)", example = "month")
    private String cycle;

    // 执行策略(1手动，2-自动）
    @Schema(description = "执行策略(1手动，2-自动）", example = "1")
    private Integer policy;

    @Schema(description = "周", example = "1")
    private String week;

    @Schema(description = "月", example = "1")
    private String month;

    @Schema(description = "天", example = "1")
    private String day;

    @Schema(description = "销售", example = "1")
    private String hour;

    @Schema(description = "分钟", example = "1")
    private String minute;

    @Schema(description = "秒", example = "1")
    private String secods;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // 备注
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvokeTarget() {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget) {
        this.invokeTarget = invokeTarget;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public Integer getPolicy() {
        return policy;
    }

    public void setPolicy(Integer policy) {
        this.policy = policy;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = StringUtils.isBlank(minute) ? "0" : minute;
    }

    public String getSecods() {
        return secods;
    }

    public void setSecods(String secods) {
        this.secods = StringUtils.isBlank(secods) ? "0" : secods;
    }
}
