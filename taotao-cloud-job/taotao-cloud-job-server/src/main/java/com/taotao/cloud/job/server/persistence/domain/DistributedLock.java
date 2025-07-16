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

/**
 *
 * @TableName distributed_lock
 */
@TableName(value = "distributed_lock")
public class DistributedLock implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String lockName;

    /**
     *
     */
    private String lockOwner;

    /**
     *
     */
    private Date expirationTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public Long getId() {
        return id;
    }

    /**
     *
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     */
    public String getLockName() {
        return lockName;
    }

    /**
     *
     */
    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    /**
     *
     */
    public String getLockOwner() {
        return lockOwner;
    }

    /**
     *
     */
    public void setLockOwner(String lockOwner) {
        this.lockOwner = lockOwner;
    }

    /**
     *
     */
    public Date getExpirationTime() {
        return expirationTime;
    }

    /**
     *
     */
    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
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
        DistributedLock other = (DistributedLock) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getLockName() == null
                        ? other.getLockName() == null
                        : this.getLockName().equals(other.getLockName()))
                && (this.getLockOwner() == null
                        ? other.getLockOwner() == null
                        : this.getLockOwner().equals(other.getLockOwner()))
                && (this.getExpirationTime() == null
                        ? other.getExpirationTime() == null
                        : this.getExpirationTime().equals(other.getExpirationTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLockName() == null) ? 0 : getLockName().hashCode());
        result = prime * result + ((getLockOwner() == null) ? 0 : getLockOwner().hashCode());
        result =
                prime * result
                        + ((getExpirationTime() == null) ? 0 : getExpirationTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", lockName=").append(lockName);
        sb.append(", lockOwner=").append(lockOwner);
        sb.append(", expirationTime=").append(expirationTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
