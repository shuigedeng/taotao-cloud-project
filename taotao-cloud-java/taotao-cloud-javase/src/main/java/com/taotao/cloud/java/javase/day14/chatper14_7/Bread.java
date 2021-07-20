package com.taotao.cloud.java.javase.day14.chatper14_7;
/**
 * 面包
 * @author shuigedeng
 *
 */
public class Bread {
	private int id;
	private String productName;
	public Bread() {
		// TODO Auto-generated constructor stub
	}
	public Bread(int id, String productName) {
		super();
		this.id = id;
		this.productName = productName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	@Override
	public String toString() {
		return "Bread [id=" + id + ", productName=" + productName + "]";
	}
	
	
	
}
