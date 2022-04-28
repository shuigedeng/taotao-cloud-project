package com.taotao.cloud.ip2region.model;

/**
 * data block class
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:27:43
 */
public class DataBlock {

	/**
	 * 城市标识
	 * city id
	 */
	private int cityId;

	/**
	 * 地区
	 * region address
	 */
	private String region;

	/**
	 * 数据ptr
	 * region ptr in the db file
	 */
	private int dataPtr;

	/**
	 * construct method
	 *
	 * @param cityId  city id
	 * @param region  region string
	 * @param dataPtr data ptr
	 * @return
	 * @since 2022-04-27 17:27:43
	 */
	public DataBlock(int cityId, String region, int dataPtr) {
		this.cityId = cityId;
		this.region = region;
		this.dataPtr = dataPtr;
	}

	/**
	 * 数据块
	 *
	 * @param cityId 城市标识
	 * @param region 地区
	 * @return
	 * @since 2022-04-27 17:27:43
	 */
	public DataBlock(int cityId, String region) {
		this(cityId, region, 0);
	}

	/**
	 * 得到城市id
	 *
	 * @return int
	 * @since 2022-04-27 17:27:43
	 */
	public int getCityId() {
		return cityId;
	}

	/**
	 * 设置城市id
	 *
	 * @param cityId 城市标识
	 * @return {@link DataBlock }
	 * @since 2022-04-27 17:27:43
	 */
	public DataBlock setCityId(int cityId) {
		this.cityId = cityId;
		return this;
	}

	/**
	 * 得到区域
	 *
	 * @return {@link String }
	 * @since 2022-04-27 17:27:43
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * 设置区域
	 *
	 * @param region 地区
	 * @return {@link DataBlock }
	 * @since 2022-04-27 17:27:43
	 */
	public DataBlock setRegion(String region) {
		this.region = region;
		return this;
	}

	/**
	 * 获取数据ptr
	 *
	 * @return int
	 * @since 2022-04-27 17:27:43
	 */
	public int getDataPtr() {
		return dataPtr;
	}

	/**
	 * 集数据ptr
	 *
	 * @param dataPtr 数据ptr
	 * @return {@link DataBlock }
	 * @since 2022-04-27 17:27:44
	 */
	public DataBlock setDataPtr(int dataPtr) {
		this.dataPtr = dataPtr;
		return this;
	}

	/**
	 * 字符串
	 *
	 * @return {@link String }
	 * @since 2022-04-27 17:27:44
	 */
	@Override
	public String toString() {
		return String.valueOf(cityId) + '|' + region + '|' + dataPtr;
	}

}
