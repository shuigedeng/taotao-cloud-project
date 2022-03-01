package com.taotao.cloud.sys.biz.tools.core.service.connect.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.http.nio.reactor.ConnectingIOReactor;

import java.util.Date;

public class ConnectOutput{
    /**
     * id
     */
    private Long id;
    /**
     * 连接数据
     */
    private ConnectInput connectInput;
    /**
     * 上次修改人
     */
    private String lastUpdateUser;
    /**
     * 上次更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;
    /**
     * 上次访问时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastAccessTime;
    /**
     * 当前连接失败次数
     */
    private int linkErrorCount;
    /**
     * 相对于配置路径的路径
     */
    private String path;

    public ConnectOutput() {
    }

    public ConnectOutput(Long id, ConnectInput connectInput, String lastUpdateUser, Date lastUpdateTime, Date lastAccessTime, int linkErrorCount,String path) {
        this.id = id;
        this.connectInput = connectInput;
        this.lastUpdateUser = lastUpdateUser;
        this.lastUpdateTime = lastUpdateTime;
        this.lastAccessTime = lastAccessTime;
        this.linkErrorCount = linkErrorCount;
        this.path = path;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ConnectInput getConnectInput() {
		return connectInput;
	}

	public void setConnectInput(
		ConnectInput connectInput) {
		this.connectInput = connectInput;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public int getLinkErrorCount() {
		return linkErrorCount;
	}

	public void setLinkErrorCount(int linkErrorCount) {
		this.linkErrorCount = linkErrorCount;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
