package com.taotao.cloud.flink.doe.beans;



/**
 * @Date: 2023/12/27
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class OrdersBean {
    private  int  oid ;
    private  String  name ;
    private  String  city  ;
    private  double money ;
    private  Long  ts ;
	public OrdersBean(){}
	public OrdersBean(int oid, String name, String city, double money, Long ts) {
		this.oid = oid;
		this.name = name;
		this.city = city;
		this.money = money;
		this.ts = ts;
	}

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}
}
