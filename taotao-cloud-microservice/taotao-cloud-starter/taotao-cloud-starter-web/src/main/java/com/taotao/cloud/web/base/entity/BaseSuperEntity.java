package com.taotao.cloud.web.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.groups.Default;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * SuperEntity
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:46
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseSuperEntity<T extends SuperEntity<T, I>, I extends Serializable> extends
	SuperEntity<T, I> implements Serializable {

	@Serial
	private static final long serialVersionUID = -4603650115461757622L;

	@CreatedDate
	@Column(name = "create_time", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	@CreatedBy
	@Column(name = "create_by", columnDefinition = "bigint comment '创建人'")
	@TableField(value = "create_by", fill = FieldFill.INSERT)
	private I createdBy;

	@CreatedDate
	@Column(name = "last_modified_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'")
	@TableField(value = "last_modified_time", fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime lastModifiedTime;

	@LastModifiedBy
	@Column(name = "last_modified_by", columnDefinition = "bigint comment '最后修改人'")
	@TableField(value = "last_modified_by", fill = FieldFill.INSERT_UPDATE)
	private I lastModifiedBy;

	@Version
	@com.baomidou.mybatisplus.annotation.Version
	@Column(name = "version", nullable = false, columnDefinition = "int not null default 1 comment '版本号'")
	private int version = 1;

	@Column(name = "del_flag", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否删除 0-正常 1-删除'")
	private boolean delFlag;

	/**
	 * 保存和缺省验证组
	 */
	public interface Save extends Default {

	}

	/**
	 * 更新和缺省验证组
	 */
	public interface Update extends Default {

	}

	public BaseSuperEntity() {
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public I getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(I createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public I getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(I lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isDelFlag() {
		return delFlag;
	}

	public void setDelFlag(boolean delFlag) {
		this.delFlag = delFlag;
	}
}
