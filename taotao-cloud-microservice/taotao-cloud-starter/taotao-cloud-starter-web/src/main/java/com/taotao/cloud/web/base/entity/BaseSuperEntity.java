package com.taotao.cloud.web.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.groups.Default;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * SuperEntity
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:46
 */
@TypeDefs({
	@TypeDef(name = "json", typeClass = JsonType.class)
})
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class BaseSuperEntity<T extends SuperEntity<T, I>, I extends Serializable> extends
	SuperEntity<T, I> implements Serializable {

	@Serial
	private static final long serialVersionUID = -4603650115461757622L;

	@CreatedDate
	@Column(name = "create_time", updatable = false, columnDefinition = "DATETIME NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间'")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	@CreatedBy
	@Column(name = "create_by", columnDefinition = "bigint null comment '创建人'")
	@TableField(value = "create_by", fill = FieldFill.INSERT)
	private Long createBy;

	@LastModifiedDate
	@Column(name = "update_time", columnDefinition = "DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'")
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	@LastModifiedBy
	@Column(name = "update_by", columnDefinition = "bigint null comment '最后修改人'")
	@TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
	private Long updateBy;

	@Version
	@com.baomidou.mybatisplus.annotation.Version
	@Column(name = "version", columnDefinition = "int null default 1 comment '版本号'")
	private Integer version = 1;

	@Column(name = "del_flag", columnDefinition = "boolean null DEFAULT false comment '是否删除 0-正常 1-删除'")
	private Boolean delFlag = false;


	public BaseSuperEntity() {
	}

	public BaseSuperEntity(I id, LocalDateTime createTime, Long createBy, LocalDateTime updateTime,
		Long updateBy, Integer version, Boolean delFlag) {
		super(id);
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
		this.version = version;
		this.delFlag = delFlag;
	}

	public BaseSuperEntity(LocalDateTime createTime, Long createBy, LocalDateTime updateTime,
		Long updateBy, Integer version, Boolean delFlag) {
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
		this.version = version;
		this.delFlag = delFlag;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(boolean delFlag) {
		this.delFlag = delFlag;
	}



}
