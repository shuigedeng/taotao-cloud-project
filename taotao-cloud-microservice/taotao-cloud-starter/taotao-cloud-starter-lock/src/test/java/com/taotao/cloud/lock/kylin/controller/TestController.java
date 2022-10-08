package com.taotao.cloud.lock.kylin.controller;


import com.taotao.cloud.lock.kylin.service.IndexService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Autowired
	private IndexService indexService;

	@GetMapping("/test")
	public String test() {
		return new Date().toString();
	}
}
