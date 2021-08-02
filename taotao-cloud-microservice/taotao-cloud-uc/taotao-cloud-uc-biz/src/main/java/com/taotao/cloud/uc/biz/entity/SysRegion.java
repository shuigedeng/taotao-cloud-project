package com.taotao.cloud.uc.biz.entity;// package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.uc.biz.entity.SysJob.SysJobBuilder;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * 地区表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
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


	@Override
	public String toString() {
		return "SysRegion{" +
			"id=" + id +
			", code='" + code + '\'' +
			", name='" + name + '\'' +
			", level=" + level +
			", cityCode='" + cityCode + '\'' +
			", lng='" + lng + '\'' +
			", lat='" + lat + '\'' +
			", parentId=" + parentId +
			", createTime=" + createTime +
			", lastModifiedTime=" + lastModifiedTime +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysRegion sysRegion = (SysRegion) o;
		return Objects.equals(id, sysRegion.id) && Objects.equals(code,
			sysRegion.code) && Objects.equals(name, sysRegion.name)
			&& Objects.equals(level, sysRegion.level) && Objects.equals(
			cityCode, sysRegion.cityCode) && Objects.equals(lng, sysRegion.lng)
			&& Objects.equals(lat, sysRegion.lat) && Objects.equals(parentId,
			sysRegion.parentId) && Objects.equals(createTime, sysRegion.createTime)
			&& Objects.equals(lastModifiedTime, sysRegion.lastModifiedTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, code, name, level, cityCode, lng, lat, parentId, createTime,
			lastModifiedTime);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public SysRegion() {
	}

	public SysRegion(Long id, String code, String name, Integer level, String cityCode,
		String lng, String lat, Integer parentId, LocalDateTime createTime,
		LocalDateTime lastModifiedTime) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.level = level;
		this.cityCode = cityCode;
		this.lng = lng;
		this.lat = lat;
		this.parentId = parentId;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}

	public static SysRegionBuilder builder() {
		return new SysRegionBuilder();
	}
	public static final class SysRegionBuilder {

		private Long id;
		private String code;
		private String name;
		private Integer level;
		private String cityCode;
		private String lng;
		private String lat;
		private Integer parentId;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;

		private SysRegionBuilder() {
		}

		public static SysRegionBuilder aSysRegion() {
			return new SysRegionBuilder();
		}

		public SysRegionBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysRegionBuilder code(String code) {
			this.code = code;
			return this;
		}

		public SysRegionBuilder name(String name) {
			this.name = name;
			return this;
		}

		public SysRegionBuilder level(Integer level) {
			this.level = level;
			return this;
		}

		public SysRegionBuilder cityCode(String cityCode) {
			this.cityCode = cityCode;
			return this;
		}

		public SysRegionBuilder lng(String lng) {
			this.lng = lng;
			return this;
		}

		public SysRegionBuilder lat(String lat) {
			this.lat = lat;
			return this;
		}

		public SysRegionBuilder parentId(Integer parentId) {
			this.parentId = parentId;
			return this;
		}

		public SysRegionBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public SysRegionBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public SysRegion build() {
			SysRegion sysRegion = new SysRegion();
			sysRegion.setId(id);
			sysRegion.setCode(code);
			sysRegion.setName(name);
			sysRegion.setLevel(level);
			sysRegion.setCityCode(cityCode);
			sysRegion.setLng(lng);
			sysRegion.setLat(lat);
			sysRegion.setParentId(parentId);
			sysRegion.setCreateTime(createTime);
			sysRegion.setLastModifiedTime(lastModifiedTime);
			return sysRegion;
		}
	}
}
