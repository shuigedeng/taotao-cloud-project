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

package com.taotao.cloud.job.server.persistence.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @TableName instance_info
 */
@Getter
@TableName(value = "instance_info")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceInfo implements Serializable {
    /**
     *
     * -- GETTER --
     *
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long actualTriggerTime;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private String appName;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long expectedTriggerTime;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long finishedTime;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Date gmtCreate;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Date gmtModified;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long instanceId;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private String instanceParams;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long jobId;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private String jobParams;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long lastReportTime;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private String result;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long runningTimes;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Integer status;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private String taskTrackerAddress;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Integer type;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long wfInstanceId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     */
    public void setActualTriggerTime(Long actualTriggerTime) {
        this.actualTriggerTime = actualTriggerTime;
    }

    /**
     *
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     *
     */
    public void setExpectedTriggerTime(Long expectedTriggerTime) {
        this.expectedTriggerTime = expectedTriggerTime;
    }

    /**
     *
     */
    public void setFinishedTime(Long finishedTime) {
        this.finishedTime = finishedTime;
    }

    /**
     *
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     *
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     *
     */
    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    /**
     *
     */
    public void setInstanceParams(String instanceParams) {
        this.instanceParams = instanceParams;
    }

    /**
     *
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     *
     */
    public void setJobParams(String jobParams) {
        this.jobParams = jobParams;
    }

    /**
     *
     */
    public void setLastReportTime(Long lastReportTime) {
        this.lastReportTime = lastReportTime;
    }

    /**
     *
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     *
     */
    public void setRunningTimes(Long runningTimes) {
        this.runningTimes = runningTimes;
    }

    /**
     *
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     *
     */
    public void setTaskTrackerAddress(String taskTrackerAddress) {
        this.taskTrackerAddress = taskTrackerAddress;
    }

    /**
     *
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     *
     */
    public void setWfInstanceId(Long wfInstanceId) {
        this.wfInstanceId = wfInstanceId;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        InstanceInfo other = (InstanceInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getActualTriggerTime() == null
                        ? other.getActualTriggerTime() == null
                        : this.getActualTriggerTime().equals(other.getActualTriggerTime()))
                && (this.getAppName() == null
                        ? other.getAppName() == null
                        : this.getAppName().equals(other.getAppName()))
                && (this.getExpectedTriggerTime() == null
                        ? other.getExpectedTriggerTime() == null
                        : this.getExpectedTriggerTime().equals(other.getExpectedTriggerTime()))
                && (this.getFinishedTime() == null
                        ? other.getFinishedTime() == null
                        : this.getFinishedTime().equals(other.getFinishedTime()))
                && (this.getGmtCreate() == null
                        ? other.getGmtCreate() == null
                        : this.getGmtCreate().equals(other.getGmtCreate()))
                && (this.getGmtModified() == null
                        ? other.getGmtModified() == null
                        : this.getGmtModified().equals(other.getGmtModified()))
                && (this.getInstanceId() == null
                        ? other.getInstanceId() == null
                        : this.getInstanceId().equals(other.getInstanceId()))
                && (this.getInstanceParams() == null
                        ? other.getInstanceParams() == null
                        : this.getInstanceParams().equals(other.getInstanceParams()))
                && (this.getJobId() == null
                        ? other.getJobId() == null
                        : this.getJobId().equals(other.getJobId()))
                && (this.getJobParams() == null
                        ? other.getJobParams() == null
                        : this.getJobParams().equals(other.getJobParams()))
                && (this.getLastReportTime() == null
                        ? other.getLastReportTime() == null
                        : this.getLastReportTime().equals(other.getLastReportTime()))
                && (this.getResult() == null
                        ? other.getResult() == null
                        : this.getResult().equals(other.getResult()))
                && (this.getRunningTimes() == null
                        ? other.getRunningTimes() == null
                        : this.getRunningTimes().equals(other.getRunningTimes()))
                && (this.getStatus() == null
                        ? other.getStatus() == null
                        : this.getStatus().equals(other.getStatus()))
                && (this.getTaskTrackerAddress() == null
                        ? other.getTaskTrackerAddress() == null
                        : this.getTaskTrackerAddress().equals(other.getTaskTrackerAddress()))
                && (this.getType() == null
                        ? other.getType() == null
                        : this.getType().equals(other.getType()))
                && (this.getWfInstanceId() == null
                        ? other.getWfInstanceId() == null
                        : this.getWfInstanceId().equals(other.getWfInstanceId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result =
                prime * result
                        + ((getActualTriggerTime() == null)
                                ? 0
                                : getActualTriggerTime().hashCode());
        result = prime * result + ((getAppName() == null) ? 0 : getAppName().hashCode());
        result =
                prime * result
                        + ((getExpectedTriggerTime() == null)
                                ? 0
                                : getExpectedTriggerTime().hashCode());
        result = prime * result + ((getFinishedTime() == null) ? 0 : getFinishedTime().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        result = prime * result + ((getInstanceId() == null) ? 0 : getInstanceId().hashCode());
        result =
                prime * result
                        + ((getInstanceParams() == null) ? 0 : getInstanceParams().hashCode());
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        result = prime * result + ((getJobParams() == null) ? 0 : getJobParams().hashCode());
        result =
                prime * result
                        + ((getLastReportTime() == null) ? 0 : getLastReportTime().hashCode());
        result = prime * result + ((getResult() == null) ? 0 : getResult().hashCode());
        result = prime * result + ((getRunningTimes() == null) ? 0 : getRunningTimes().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result =
                prime * result
                        + ((getTaskTrackerAddress() == null)
                                ? 0
                                : getTaskTrackerAddress().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getWfInstanceId() == null) ? 0 : getWfInstanceId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", actualTriggerTime=").append(actualTriggerTime);
        sb.append(", appId=").append(appName);
        sb.append(", expectedTriggerTime=").append(expectedTriggerTime);
        sb.append(", finishedTime=").append(finishedTime);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", instanceId=").append(instanceId);
        sb.append(", instanceParams=").append(instanceParams);
        sb.append(", jobId=").append(jobId);
        sb.append(", jobParams=").append(jobParams);
        sb.append(", lastReportTime=").append(lastReportTime);
        sb.append(", result=").append(result);
        sb.append(", runningTimes=").append(runningTimes);
        sb.append(", status=").append(status);
        sb.append(", taskTrackerAddress=").append(taskTrackerAddress);
        sb.append(", type=").append(type);
        sb.append(", wfInstanceId=").append(wfInstanceId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
