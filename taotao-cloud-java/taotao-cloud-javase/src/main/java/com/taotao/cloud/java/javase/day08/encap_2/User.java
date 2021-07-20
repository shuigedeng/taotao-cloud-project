package com.taotao.cloud.java.javase.day08.encap_2;
/**
 * 用户类
 * @author shuigedeng
 *
 */
public class User {
	//卡号
	private String cardNo;
	//身份证号
	private String identity;
	//用户名
	private String username;
	//密码
	private String password;
	//电话
	private String phone;
	//余额
	private double balance;
	
	//alt+/ 默认构造方法快捷键
	public User() {
		// TODO Auto-generated constructor stub
	}
	//带参构造方法 alt+shift+s --》generate Constructor
	public User(String cardNo, String identity, String username, String password, String phone, double balance) {
		super();
		this.cardNo = cardNo;
		this.identity = identity;
		this.username = username;
		this.password = password;
		this.phone = phone;
		this.balance = balance;
	}
	//get和set
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
	
}
