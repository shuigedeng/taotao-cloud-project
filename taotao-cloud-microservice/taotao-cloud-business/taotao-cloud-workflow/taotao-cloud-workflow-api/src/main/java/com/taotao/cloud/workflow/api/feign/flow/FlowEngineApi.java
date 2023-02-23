package com.taotao.cloud.workflow.api.feign.flow;

import java.util.List;
import javax.validation.Valid;
import jnpf.engine.entity.FlowEngineEntity;
import jnpf.engine.model.flowengine.FlowAppPageModel;
import jnpf.engine.model.flowengine.FlowPagination;
import jnpf.exception.WorkFlowException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * api接口
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司（https://www.jnpfsoft.com）
 * @date 2021/3/15 11:55
 */
//@FeignClient(name = FeignName.WORKFLOW_SERVER_NAME , fallback = FlowEngineApiFallback.class, path = "/Engine/FlowEngine")
public interface FlowEngineApi {

	@PostMapping(value = "/tableCre")
	TableModels tableCre(@RequestBody TableCreModels tableCreModels) throws WorkFlowException;

	@PostMapping(value = "/create")
	void create(@RequestBody @Valid FlowEngineEntity flowEngineEntity) throws WorkFlowException;

	@GetMapping(value = "/getInfoByID/{id}")
	FlowEngineEntity getInfoByID (@PathVariable("id") String id);

	@PostMapping(value = "/updateByID/{id}")
	void updateByID (@PathVariable("id") String id, @RequestBody FlowEngineEntity flowEngineEntity) throws WorkFlowException;

	@PostMapping(value = "/getAppPageList")
	FlowAppPageModel getAppPageList(@RequestBody FlowPagination pagination);

	@PostMapping(value = "/getFlowList")
	List<FlowEngineEntity> getFlowList(@RequestBody List<String> id);
}
