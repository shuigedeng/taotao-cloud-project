package com.taotao.cloud.store.biz.api.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.message.api.feign.IFeignStoreMessageService;
import com.taotao.cloud.message.api.vo.StoreMessageQueryVO;
import com.taotao.cloud.message.api.vo.StoreMessageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 管理端,店铺消息消息管理接口
 */
@Validated
@RestController
@Tag(name = "管理端-店铺消息消息管理接口", description = "平台管理端-店铺消息消息管理接口")
@RequestMapping("/manager/message/store")
public class StoreMessageManagerController {

	@Autowired
	private IFeignStoreMessageService storeMessageService;

	@Operation(summary = "多条件分页获取", description = "多条件分页获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<PageResult<StoreMessageVO>> getByCondition(StoreMessageQueryVO storeMessageQueryVO,
                                                             PageParam pageParam) {
		IPage<StoreMessageVO> page = storeMessageService.getPage(storeMessageQueryVO, pageParam);
		return Result.success(PageResult.convertMybatisPage(page, StoreMessageVO.class));
	}

}
