package com.taotao.cloud.common.utils.location;


import com.alibaba.fastjson2.JSONObject;

/**
 * 位置上市
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:23:37
 */
public class LocationIPO {

	/**
	 * 经度
	 */
	private double lng;
	/**
	 * 纬度
	 */
	private double lat;

	/**
	 * 将经纬度参数转换为位置对象
	 * <p>
	 * {@linkplain JSONObject} 转 {@linkplain LocationIPO}
	 *
	 * @param location 标准的经纬度JSON对象，包含的key有("lng", "lat")
	 * @return 经纬度对象
	 */
	public static LocationIPO toLocationIPO(JSONObject location) {
		double lng = location.getDouble("lng");
		double lat = location.getDouble("lat");
		return LocationIPO.builder().lng(lng).lat(lat).build();
	}

	public LocationIPO() {
	}

	public LocationIPO(double lng, double lat) {
		this.lng = lng;
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public static LocationIPOBuilder builder() {
		return new LocationIPOBuilder();
	}

	public static final class LocationIPOBuilder {

		private double lng;
		private double lat;

		private LocationIPOBuilder() {
		}

		public LocationIPOBuilder lng(double lng) {
			this.lng = lng;
			return this;
		}

		public LocationIPOBuilder lat(double lat) {
			this.lat = lat;
			return this;
		}

		public LocationIPO build() {
			LocationIPO locationIPO = new LocationIPO();
			locationIPO.setLng(lng);
			locationIPO.setLat(lat);
			return locationIPO;
		}
	}
}
