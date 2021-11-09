package com.taotao.cloud.order.biz.pulsar;

public class Product {

	public static final String PRODUCT_TOPIC = "product-topic";

	private String data;

	public Product(String data) {
		this.data = data;
	}

	public Product() {
	}

	public String getData() {
		return data;
	}
}
