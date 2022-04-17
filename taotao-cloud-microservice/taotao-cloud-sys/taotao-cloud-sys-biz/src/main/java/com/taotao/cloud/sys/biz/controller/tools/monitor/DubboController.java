package com.taotao.cloud.sys.biz.controller.tools.monitor;

import com.taotao.cloud.sys.biz.service.IDubboService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DubboController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 15:49:28
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-dubbo管理API", description = "工具管理端-dubbo管理API")
@RequestMapping("/sys/tools/monitor/dubbo")
public class DubboController {

	private final IDubboService dubboService;

	//@GetMapping("/connects")
	//public List<String> connects() {
	//	return dubboService.connects();
	//}
	//
	///**
	// * 所有的 dubbo 服务,在某个连接上
	// */
	//@GetMapping("/services")
	//public List<String> services(@NotNull String connName) throws IOException {
	//	return dubboService.services(connName);
	//}
	//
	///**
	// * 某个服务的提供者列表
	// */
	//@GetMapping("/providers")
	//public List<DubboProviderDto> providers(@NotNull String connName, @NotNull String serviceName)
	//	throws IOException {
	//	return dubboService.providers(connName, serviceName);
	//}
	//
	///**
	// * 调用 dubbo 服务
	// */
	//@PostMapping("/invoke")
	//public Object invoke(@RequestBody @Valid DubboInvokeParam dubboInvokeParam)
	//	throws ClassNotFoundException, NoSuchMethodException, RemotingException, InterruptedException, ExecutionException {
	//	return dubboService.invoke(dubboInvokeParam);
	//}
}
