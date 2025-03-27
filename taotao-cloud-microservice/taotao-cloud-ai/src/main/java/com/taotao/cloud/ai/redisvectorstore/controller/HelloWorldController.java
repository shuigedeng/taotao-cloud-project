package com.taotao.cloud.ai.redisvectorstore.controller;

import com.taotao.cloud.ai.redisvectorstore.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloWorldController {
	@Autowired
	SearchService searchService;

	@RequestMapping("/hello")
	public Map<String, Object> showHelloWorld(String message) {
		Map<String, Object> map = new HashMap<>();
		map.put("msg", searchService.retrieve(message));
		return map;
	}
}
