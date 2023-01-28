package com.taotao.cloud.workflow.api.feign.flow.fallback;

import com.taotao.cloud.workflow.api.feign.flow.FlowEngineApi;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * api接口
 *
 */
//@Component
@Slf4j
public class FlowEngineApiFallback implements FlowEngineApi {

	@Override
	public TableModels tableCre(TableCreModels tableCreModels){
		return new TableModels();
	}

	@Override
	public void create(@Valid FlowEngineEntity flowEngineEntity) {
	}

	@Override
	public FlowEngineEntity getInfoByID(String id) {
		return null;
	}

	@Override
	public void updateByID (@PathVariable("id") String id, @RequestBody FlowEngineEntity flowEngineEntity){}

	@Override
	public FlowAppPageModel getAppPageList(FlowPagination pagination) {
		return new FlowAppPageModel();
	}

	@Override
	public List<FlowEngineEntity> getFlowList(List<String> id) {
		return new ArrayList<>();
	}

}
