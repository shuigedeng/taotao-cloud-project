package com.taotao.cloud.java.concurrent.callback;

public class Data {
	private int n;
	private int m;

	public Data(int n, int m) {
		System.out.println("调用data的构造函数");
		this.n = n;
		this.m = m;
	}

	@Override
	public String toString() {
		int r = n / m;
		return n + "/" + m + " = " + r;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

}
