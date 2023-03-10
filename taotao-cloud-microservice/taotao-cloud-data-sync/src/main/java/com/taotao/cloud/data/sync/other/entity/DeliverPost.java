package com.taotao.cloud.data.sync.other.entity;

import lombok.Data;

@Data
public class DeliverPost {

	private String orderId ;
	private String postId;
	private boolean isArrived ;
	
	
}
