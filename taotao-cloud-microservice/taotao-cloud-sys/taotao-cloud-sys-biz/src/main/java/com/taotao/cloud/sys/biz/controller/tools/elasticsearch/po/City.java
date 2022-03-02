package com.taotao.cloud.sys.biz.controller.tools.elasticsearch.po;


public class City {
	private short cityid;
	private String city;
	private String lastupdate;
	private Country country;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public short getCityid() {
		return cityid;
	}
	public void setCityid(short cityid) {
		this.cityid = cityid;
	}
	public String getLastupdate() {
		return lastupdate;
	}
	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}
}
