package com.taotao.cloud.bigdata.azkaban.mq.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Message<T> implements Serializable {

	private static final long serialVersionUID = 6017550519278159274L;
	private String msgId;
	private String topic;
	private String tag;
	private T data;

}
