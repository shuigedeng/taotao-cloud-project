package com.taotao.cloud.sys.api.vo.region;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 地区VO
 *
 */
public class RegionVO {

	@Schema(description = "id")
	private Long id;

	@Schema(description = "地区父节点")
	private Long parentId;

	@Schema(description = "地区编码")
	private String code;

	@Schema(description = "地区名称")
	private String name;

	/**
	 * 地区级别（1:省份province,2:市city,3:区县district,4:街道street） "行政区划级别" + "country:国家" +
	 * "province:省份（直辖市会在province和city显示）" + "city:市（直辖市会在province和city显示）" + "district:区县" +
	 * "street:街道"
	 */
	@Schema(description = "地区级别")
	private String level;

	@Schema(description = "城市编码")
	private String cityCode;

	@Schema(description = "城市中心经度")
	private String lng;

	@Schema(description = "城市中心纬度")
	private String lat;

	@Schema(description = "行政地区路径,类似：1，2，3")
	private String path;

	@Schema(description = "排序")
	private Integer orderNum = 1;

	@Schema(description = "子信息")
	private List<RegionVO> children;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public List<RegionVO> getChildren() {
		return children;
	}

	public void setChildren(List<RegionVO> children) {
		this.children = children;
	}
}
