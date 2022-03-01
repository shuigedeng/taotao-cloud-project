package com.taotao.cloud.sys.biz.elasticsearch.po;

import java.util.List;

public class Country {
	private String country_id;
	private String country;
	private String last_update;
	private List<City> citys;
	
	String joinkey;
	
	public String getCountry_id() {
		return country_id;
	}
	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getLast_update() {
		return last_update;
	}
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}
	public List<City> getCitys() {
		return citys;
	}
	public void setCitys(List<City> citys) {
		this.citys = citys;
	}
	public String getJoinkey() {
		return joinkey;
	}
	public void setJoinkey(String joinkey) {
		this.joinkey = joinkey;
	}
	
}
