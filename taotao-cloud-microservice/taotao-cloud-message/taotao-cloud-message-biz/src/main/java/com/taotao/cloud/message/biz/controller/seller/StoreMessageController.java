package com.taotao.cloud.message.biz.controller.seller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.message.api.enums.MessageStatusEnum;
import com.taotao.cloud.message.api.vo.StoreMessageQueryVO;
import com.taotao.cloud.message.biz.entity.StoreMessage;
import com.taotao.cloud.message.biz.service.StoreMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,消息接口
 */
@Validated
@RestController
@Tag(name = "店铺端-消息API", description = "店铺端-消息API")
@RequestMapping("/message/seller/storeMessage")
public class StoreMessageController {

	/**
	 * 商家消息
	 */
	@Autowired
	private StoreMessageService storeMessageService;

	@Operation(summary = "获取商家消息", description = "获取商家消息", method = CommonConstant.GET)
	@RequestLogger("获取商家消息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<IPage<StoreMessage>> getPage(String status, PageVO pageVo) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		StoreMessageQueryVO storeMessageQueryVO = new StoreMessageQueryVO();
		storeMessageQueryVO.setStatus(status);
		storeMessageQueryVO.setStoreId(storeId);
		IPage<StoreMessage> page = storeMessageService.getPage(storeMessageQueryVO, pageVo);
		return Result.success(page);
	}

	@Operation(summary = "获取商家消息总汇", description = "获取商家消息总汇", method = CommonConstant.GET)
	@RequestLogger("获取商家消息总汇")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/all")
	public Result<Map<String, Object>> getPage(PageVO pageVo) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		//返回值定义
		Map<String, Object> map = new HashMap<>(4);
		StoreMessageQueryVO storeMessageQueryVO = new StoreMessageQueryVO();
		storeMessageQueryVO.setStoreId(storeId);
		//未读消息
		storeMessageQueryVO.setStatus(MessageStatusEnum.UN_READY.name());
		IPage<StoreMessage> page = storeMessageService.getPage(storeMessageQueryVO, pageVo);
		map.put("UN_READY", page);
		//已读消息
		storeMessageQueryVO.setStatus(MessageStatusEnum.ALREADY_READY.name());
		page = storeMessageService.getPage(storeMessageQueryVO, pageVo);
		map.put("ALREADY_READY", page);
		//回收站
		storeMessageQueryVO.setStatus(MessageStatusEnum.ALREADY_REMOVE.name());
		page = storeMessageService.getPage(storeMessageQueryVO, pageVo);
		map.put("ALREADY_REMOVE", page);
		return Result.success(map);
	}

	@Operation(summary = "已读操作", description = "已读操作", method = CommonConstant.PUT)
	@RequestLogger("已读操作")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}/read")
	public Result<Boolean> readMessage(@PathVariable String id) {
		OperationalJudgment.judgment(storeMessageService.getById(id));
		Boolean result = storeMessageService.editStatus(MessageStatusEnum.ALREADY_READY.name(), id);
		return Result.success(result);
	}

	@Operation(summary = "回收站还原消息", description = "回收站还原消息", method = CommonConstant.PUT)
	@RequestLogger("回收站还原消息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}/reduction")
	public Result<Boolean> reductionMessage(@PathVariable String id) {
		OperationalJudgment.judgment(storeMessageService.getById(id));
		Boolean result = storeMessageService.editStatus(MessageStatusEnum.ALREADY_READY.name(), id);
		return Result.success(result);
	}

	@Operation(summary = "删除操作", description = "删除操作", method = CommonConstant.DELETE)
	@RequestLogger("删除操作")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping("/{id}")
	public Result<Boolean> deleteMessage(@PathVariable String id) {
		OperationalJudgment.judgment(storeMessageService.getById(id));
		Boolean result = storeMessageService.editStatus(MessageStatusEnum.ALREADY_REMOVE.name(),
			id);
		return Result.success(result);
	}

	@Operation(summary = "彻底删除操作", description = "彻底删除操作", method = CommonConstant.DELETE)
	@RequestLogger("彻底删除操作")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping("/{id}/thorough")
	public Result<Boolean> disabled(@PathVariable String id) {
		OperationalJudgment.judgment(storeMessageService.getById(id));
		Boolean result = storeMessageService.deleteByMessageId(id);
		return Result.success(result);
	}


}
