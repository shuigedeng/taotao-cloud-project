package com.taotao.cloud.common.utils.location;

import java.util.ArrayList;
import java.util.List;

/**
 * 位置工具类，用于处理经纬度等问题
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:24:08
 */
public class LocationUtils {

	private static final double EARTH_RADIUS = 6378.137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 通过经纬度获取距离(单位：米)
	 * <p>
	 * 说明（如：高德地图，重庆市政府坐标）<br>
	 * <code>106.550464,29.563761</code><br>
	 * 106.550464 经度<br> 29.563761 纬度<br> 注：lng 经度<br> 注：lat 纬度
	 *
	 * @param locationIPO1 位置1
	 * @param locationIPO2 位置1
	 * @return 距离
	 */
	public static double getDistance(LocationIPO locationIPO1, LocationIPO locationIPO2) {
		double lng1 = locationIPO1.getLng();
		double lat1 = locationIPO1.getLat();
		double lng2 = locationIPO2.getLng();
		double lat2 = locationIPO2.getLat();

		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(
			Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(
				Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000d) / 10000d;
		s = s * 1000;
		return s;
	}

	/**
	 * 通过经纬度获取距离(单位：米)
	 *
	 * @param locationIPO     位置
	 * @param locationIPOList 位置数组
	 * @return 距离数组
	 */
	public static List<Double> getDistance(LocationIPO locationIPO,
		List<LocationIPO> locationIPOList) {
		List<Double> list = new ArrayList<>();
		for (LocationIPO location : locationIPOList) {
			list.add(getDistance(locationIPO, location));
		}

		return list;
	}

	/**
	 * 获得距离当前位置最近的经纬度
	 * <p>
	 * 返回locations数组中最小值的下标
	 *
	 * @param locationIPO     位置
	 * @param locationIPOList 位置数组
	 * @return minIndex
	 */
	public static int getNearestLngAndLat(LocationIPO locationIPO,
		List<LocationIPO> locationIPOList) {
		int minIndex = 0;

		var list = getDistance(locationIPO, locationIPOList);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) < list.get(minIndex)) {
				minIndex = i;
			}
		}

		return minIndex;
	}

}
