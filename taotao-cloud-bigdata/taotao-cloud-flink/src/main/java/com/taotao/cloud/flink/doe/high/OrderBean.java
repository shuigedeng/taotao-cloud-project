package com.taotao.cloud.flink.doe.high;



/**
 * @Date: 2024/1/3
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class OrderBean {
    private  int oid  ;
    private  double money ;
    private  int  uid ;
    private  long  ts ;
	public OrderBean(){}
	public OrderBean(int oid, double money, int uid, long ts) {
		this.oid = oid;
		this.money = money;
		this.uid = uid;
		this.ts = ts;
	}

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}
}
