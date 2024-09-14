package com.taotao.boot.data.sync.partitioner;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
	private Long id;
	private String name;
	private int age;
}
