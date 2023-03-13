package com.taotao.cloud.sys.biz.controller;

import com.taotao.cloud.sys.biz.service.grpc.DeviceGrpcService;
import org.apache.pulsar.shade.io.swagger.annotations.Api;
import org.apache.pulsar.shade.io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Testcontroller", description = "测试grpc")
public class TestGrpcController {


	@Autowired
	private DeviceGrpcService deviceGrpcService;


	@RequestMapping("/testInsertDeviceFix")
	@ApiOperation(value = "test", httpMethod = "GET", notes = "测试grpc插入")
	public String printMessage3(@RequestParam(defaultValue = "Hmemb") String name) {
		return deviceGrpcService.insertDeviceFix();
	}

	@RequestMapping("/TEST1")
	@ApiOperation(value = "test", httpMethod = "GET", notes = "测试1")
	public String printMessage(@RequestParam(defaultValue = "Michael") String name) {
		return name;
	}
}
