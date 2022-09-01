package com.taotao.cloud.common.utils.location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 经纬度GeoHash转换工具类
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:32:40
 */
public class GeoHash {

	//最小纬度值
	public static final double MINLAT = -90;
	//最大纬度值
	public static final double MAXLAT = 90;
	//最小经度值
	public static final double MINLNG = -180;
	//最大经度值
	public static final double MAXLNG = 180;

	//base32编码的所有字符
	private static final char[] BASE32CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v',
		'w', 'x', 'y', 'z'};

	/**
	 * 1 2500km;2 630km;3 78km;4 30km 5 2.4km; 6 610m; 7 76m; 8 19m 9 4.8m; 10 59.5cm; 11 14.9cm; 12
	 * 1.9cm
	 */
	//经纬度转化为geohash长度
	private int hashLength = 8;
	//纬度转化为二进制长度
	private int latLength = 20;
	//经度转化为二进制长度
	private int lngLength = 20;

	//每格纬度的单位大小
	private double minLat;
	//每格经度的单位大小
	private double minLng;

	//纬度
	private double lat;
	//经度
	private double lng;

	/**
	 * 地理散列
	 *
	 * @param lat 纬度
	 * @param lng 液化天然气
	 * @since 2022-05-11 10:33:53
	 */
	public GeoHash(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
		setMinLatLng();
	}

	/**
	 * 初始化经纬度最小单位
	 *
	 * @since 2022-05-11 10:33:53
	 */
	private void setMinLatLng() {
		minLat = MAXLAT - MINLAT;
		for (int i = 0; i < latLength; i++) {
			minLat /= 2.0;
		}
		minLng = MAXLNG - MINLNG;
		for (int i = 0; i < lngLength; i++) {
			minLng /= 2.0;
		}
	}

	/**
	 * 设置经纬度转化为hash编码长度
	 *
	 * @param length 长度
	 * @since 2022-05-11 10:33:53
	 */
	public void setHashLength(int length) {
		if (length > 1) {
			hashLength = length;
			latLength = (length * 5) / 2;
			if (length % 2 == 0) {
				lngLength = latLength;
			} else {
				lngLength = latLength + 1;
			}
			setMinLatLng();
		}
	}

	/**
	 * 将值转换为二进制编码
	 *
	 * @param value  价值
	 * @param min    最小值
	 * @param max    马克斯
	 * @param length 长度
	 * @return {@link boolean[] }
	 * @since 2022-05-11 10:33:54
	 */
	private boolean[] getHashArray(double value, double min, double max, int length) {
		if (value < min || value > max) {
			return null;
		}
		if (length < 1) {
			return null;
		}
		boolean[] result = new boolean[length];
		for (int i = 0; i < length; i++) {
			double mid = (min + max) / 2.0;
			if (value > mid) {
				result[i] = true;
				min = mid;
			} else {
				result[i] = false;
				max = mid;
			}
		}
		return result;
	}

	/**
	 * 获得地理二元
	 *
	 * @param lat 纬度
	 * @param lng 液化天然气
	 * @return {@link boolean[] }
	 * @since 2022-05-11 10:33:54
	 */
	private boolean[] getGeoBinary(double lat, double lng) {
		boolean[] latArray = getHashArray(lat, MINLAT, MAXLAT, latLength);
		boolean[] lngArray = getHashArray(lng, MINLNG, MAXLNG, lngLength);
		return merge(latArray, lngArray);
	}

	/**
	 * 合并两个二进制字符串
	 *
	 * @param latArray lat数组
	 * @param lngArray 液化天然气数组
	 * @return {@link boolean[] }
	 * @since 2022-05-11 10:33:54
	 */
	private boolean[] merge(boolean[] latArray, boolean[] lngArray) {
		if (latArray == null || lngArray == null) {
			return null;
		}
		boolean[] result = new boolean[lngArray.length + latArray.length];
		Arrays.fill(result, false);
		for (int i = 0; i < lngArray.length; i++) {
			result[2 * i] = lngArray[i];
		}
		for (int i = 0; i < latArray.length; i++) {
			result[2 * i + 1] = latArray[i];
		}
		return result;
	}

	/**
	 * 获取经纬度的base32字符串
	 *
	 * @param lat 纬度
	 * @param lng 液化天然气
	 * @return {@link String }
	 * @since 2022-05-11 10:33:54
	 */
	private String getGeoHashBase32(double lat, double lng) {
		boolean[] bools = getGeoBinary(lat, lng);
		if (bools == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bools.length; i = i + 5) {
			boolean[] base32 = new boolean[5];
			for (int j = 0; j < 5; j++) {
				base32[j] = bools[i + j];
			}
			char cha = getBase32Char(base32);
			if (' ' == cha) {
				return null;
			}
			sb.append(cha);
		}
		return sb.toString();
	}

	/**
	 * 将五位二进制转化为base32
	 *
	 * @param base32 base32
	 * @return char
	 * @since 2022-05-11 10:33:54
	 */
	private char getBase32Char(boolean[] base32) {
		if (base32 == null || base32.length != 5) {
			return ' ';
		}
		int num = 0;
		for (boolean bool : base32) {
			num <<= 1;
			if (bool) {
				num += 1;
			}
		}
		return BASE32CHARS[num % BASE32CHARS.length];
	}

	/**
	 * 获取周围九宫格的编码
	 *
	 * @return {@link List }<{@link String }>
	 * @since 2022-05-11 10:33:54
	 */
	public List<String> getGeoHashBase32ForAround() {
		double leftLat = lat - minLat;
		double rightLat = lat + minLat;
		double upLng = lng - minLng;
		double downLng = lng + minLng;
		List<String> base32ForAround = new ArrayList<String>();
		//左侧从上到下 3个
		String leftUp = getGeoHashBase32(leftLat, upLng);
		if (!(leftUp == null || "".equals(leftUp))) {
			base32ForAround.add(leftUp);
		}
		String leftMid = getGeoHashBase32(leftLat, lng);
		if (!(leftMid == null || "".equals(leftMid))) {
			base32ForAround.add(leftMid);
		}
		String leftDown = getGeoHashBase32(leftLat, downLng);
		if (!(leftDown == null || "".equals(leftDown))) {
			base32ForAround.add(leftDown);
		}
		//中间从上到下 3个
		String midUp = getGeoHashBase32(lat, upLng);
		if (!(midUp == null || "".equals(midUp))) {
			base32ForAround.add(midUp);
		}
		String midMid = getGeoHashBase32(lat, lng);
		if (!(midMid == null || "".equals(midMid))) {
			base32ForAround.add(midMid);
		}
		String midDown = getGeoHashBase32(lat, downLng);
		if (!(midDown == null || "".equals(midDown))) {
			base32ForAround.add(midDown);
		}
		//右侧从上到下 3个
		String rightUp = getGeoHashBase32(rightLat, upLng);
		if (!(rightUp == null || "".equals(rightUp))) {
			base32ForAround.add(rightUp);
		}
		String rightMid = getGeoHashBase32(rightLat, lng);
		if (!(rightMid == null || "".equals(rightMid))) {
			base32ForAround.add(rightMid);
		}
		String rightDown = getGeoHashBase32(rightLat, downLng);
		if (!(rightDown == null || "".equals(rightDown))) {
			base32ForAround.add(rightDown);
		}
		return base32ForAround;
	}

	/**
	 * 获得地理散列base32
	 *
	 * @return {@link String }
	 * @since 2022-05-11 10:33:54
	 */
	public String getGeoHashBase32() {
		return getGeoHashBase32(lat, lng);
	}

}
