/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.ip2region.model;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.taotao.cloud.common.utils.StringUtil;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * ip 信息详情
 *
 * @author L.cm
 */
public class IpInfo implements Serializable {

	/**
	 * 城市id
	 */
	private Integer cityId;
	/**
	 * 国家
	 */
	private String country;
	/**
	 * 区域
	 */
	private String region;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 运营商
	 */
	private String isp;
	/**
	 * region ptr in the db file
	 */
	private int dataPtr;

	/**
	 * 拼接完整的地址
	 *
	 * @return address
	 */
	public String getAddress() {
		Set<String> regionSet = new LinkedHashSet<>();
		regionSet.add(country);
		regionSet.add(region);
		regionSet.add(province);
		regionSet.add(city);
		regionSet.removeIf(Objects::isNull);
		return StringUtil.join(regionSet, StringPool.EMPTY);
	}

	/**
	 * 拼接完整的地址
	 *
	 * @return address
	 */
	public String getAddressAndIsp() {
		Set<String> regionSet = new LinkedHashSet<>();
		regionSet.add(country);
		regionSet.add(region);
		regionSet.add(province);
		regionSet.add(city);
		regionSet.add(isp);
		regionSet.removeIf(Objects::isNull);
		return StringUtil.join(regionSet, StringPool.SPACE);
	}

	public IpInfo() {
	}

	public IpInfo(Integer cityId, String country, String region, String province, String city,
		String isp, int dataPtr) {
		this.cityId = cityId;
		this.country = country;
		this.region = region;
		this.province = province;
		this.city = city;
		this.isp = isp;
		this.dataPtr = dataPtr;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public int getDataPtr() {
		return dataPtr;
	}

	public void setDataPtr(int dataPtr) {
		this.dataPtr = dataPtr;
	}
}
