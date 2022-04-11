package com.taotao.cloud.mongodb.helper.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;

/**
 * BaseModel
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-10 11:55:04
 */
public class BaseModel implements Serializable {

	@Id
	private String id;
	@CreateTime
	private LocalDateTime createTime;
	@UpdateTime
	private LocalDateTime updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
}
