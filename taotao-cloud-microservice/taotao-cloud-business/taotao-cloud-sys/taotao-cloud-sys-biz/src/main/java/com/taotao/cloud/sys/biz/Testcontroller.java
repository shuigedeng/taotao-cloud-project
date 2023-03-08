package com.taotao.cloud.sys.biz;

@RestController
@Api(value = "Testcontroller", description = "测试swagger")
public class Testcontroller {


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
