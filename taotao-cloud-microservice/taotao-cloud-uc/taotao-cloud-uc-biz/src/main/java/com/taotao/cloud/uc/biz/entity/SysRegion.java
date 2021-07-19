package com.taotao.cloud.uc.biz.entity;// package com.taotao.cloud.uc.biz.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * 地区表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_uc_region")
@org.hibernate.annotations.Table(appliesTo = "sys_uc_region", comment = "地区表")
public class SysRegion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigint not null comment 'id'")
	private Long id;

	/**
	 * 地区编码
	 */
	@Column(name = "code", unique = true, nullable = false, columnDefinition = "varchar(255) not null comment '地区编码'")
	private String code;

	/**
	 * 地区名称
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(255) not null default '' comment '地区名称'")
	private String name;

	/**
	 * 地区级别（1:省份province,2:市city,3:区县district,4:街道street）
	 */
	@Column(name = "level", nullable = false, columnDefinition = "int not null comment '地区级别（1:省份province,2:市city,3:区县district,4:街道street）'")
	private Integer level;

	/**
	 * 城市编码
	 */
	@Column(name = "city_code", columnDefinition = "varchar(255) null comment '城市编码'")
	private String cityCode;

	/**
	 * 城市中心经度
	 */
	@Column(name = "lng", columnDefinition = "varchar(255) null comment '城市中心经度'")
	private String lng;

	/**
	 * 城市中心纬度
	 */
	@Column(name = "lat", columnDefinition = "varchar(255) null comment '城市中心经度'")
	private String lat;

	/**
	 * 地区父节点
	 */
	@Column(name = "parent_id", nullable = false, columnDefinition = "int not null comment '地区父节点'")
	private Integer parentId;

	@CreatedDate
	@Column(name = "create_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'")
	private LocalDateTime createTime;

	@LastModifiedDate
	@Column(name = "last_modified_time", columnDefinition = "TIMESTAMP comment '最后修改时间'")
	private LocalDateTime lastModifiedTime;
}
