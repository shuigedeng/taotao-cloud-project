package com.taotao.cloud.web.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serial;
import java.io.Serializable;
import javax.annotation.PreDestroy;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.hibernate.annotations.GenericGenerator;

/**
 * SuperEntity
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:46
 */
@MappedSuperclass
public class SuperEntity<T extends SuperEntity<T, I>, I extends Serializable>
	extends Model<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = -4603650115461757622L;

	@Id
	@GenericGenerator(name="snowFlakeIdGenerator", strategy="com.taotao.cloud.data.jpa.bean.SnowFlakeIdGenerator")
	@GeneratedValue(generator="snowFlakeIdGenerator")
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, columnDefinition = "bigint not null comment 'id'")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private I id;

	public I getId() {
		return id;
	}

	public void setId(I id) {
		this.id = id;
	}

}
