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
	private Long createdBy;

	@CreatedDate
	@Column(name = "update_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'")
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	@LastModifiedBy
	@Column(name = "update_by", columnDefinition = "bigint comment '最后修改人'")
	@TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
	private Long updateBy;

	@Version
	@com.baomidou.mybatisplus.annotation.Version
	@Column(name = "version", nullable = false, columnDefinition = "int not null default 1 comment '版本号'")
	private int version = 1;

	@Column(name = "del_flag", nullable = false, columnDefinition = "boolean NOT NULL DEFAULT false comment '是否删除 0-正常 1-删除'")
	private Boolean delFlag;

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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(boolean delFlag) {
		this.delFlag = delFlag;
	}
}
