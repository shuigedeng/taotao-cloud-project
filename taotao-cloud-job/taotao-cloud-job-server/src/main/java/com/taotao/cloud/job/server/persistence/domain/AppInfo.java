package com.taotao.cloud.job.server.persistence.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * 
 * @TableName app_info
 */
@Getter
@TableName(value ="app_info")
@Builder
public class AppInfo implements Serializable {
    /**
     *
     * -- GETTER --
     *

     */
    @TableId
    private Long id;

    /**
     *
     * -- GETTER --
     *

     */
    private String currentServer;

    /**
     *
     * -- GETTER --
     *

     */
    private String appName;

    private String subAppName;

    /**
     *
     * -- GETTER --
     *

     */
    private String password;

    public void setSubAppName(String subAppName) {
        this.subAppName = subAppName;
    }

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
    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
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
    public void setPassword(String password) {
        this.password = password;
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
        AppInfo other = (AppInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCurrentServer() == null ? other.getCurrentServer() == null : this.getCurrentServer().equals(other.getCurrentServer()))
            && (this.getAppName() == null ? other.getAppName() == null : this.getAppName().equals(other.getAppName()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCurrentServer() == null) ? 0 : getCurrentServer().hashCode());
        result = prime * result + ((getAppName() == null) ? 0 : getAppName().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", currentServer=").append(currentServer);
        sb.append(", appName=").append(appName);
        sb.append(", password=").append(password);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
