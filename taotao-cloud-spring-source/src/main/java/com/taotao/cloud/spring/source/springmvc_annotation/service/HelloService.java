package com.taotao.cloud.spring.source.springmvc_annotation.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

	public String sayHello(String name) {

		return "Index " + name;
	}

}
