package com.taotao.cloud.web.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serial;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * SuperEntity
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:46
 */
@MappedSuperclass
public class SuperEntity<T extends SuperEntity<T, I>, I extends Serializable>
	extends Model<T>
	implements Serializable {

	@Serial
	private static final long serialVersionUID = -4603650115461757622L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false, columnDefinition = "bigint comment 'id'")
	@TableId(value = "id", type = IdType.INPUT)
	private I id;

	public I getId() {
		return id;
	}

	public void setId(I id) {
		this.id = id;
	}
}
