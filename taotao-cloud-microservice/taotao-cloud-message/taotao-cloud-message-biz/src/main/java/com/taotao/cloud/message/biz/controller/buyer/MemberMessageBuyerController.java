package com.taotao.cloud.message.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.message.api.enums.MessageStatusEnum;
import com.taotao.cloud.message.api.vo.MemberMessageQueryVO;
import com.taotao.cloud.message.biz.entity.MemberMessage;
import com.taotao.cloud.message.biz.service.MemberMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * 买家端,会员站内消息接口
 */
@Validated
@RestController
@Tag(name = "平台管理端-会员站内消息API", description = "平台管理端-会员站内消息API")
@RequestMapping("/message/buyer/member")
public class MemberMessageBuyerController {

	/**
	 * 会员站内消息
	 */
	@Autowired
	private MemberMessageService memberMessageService;

	@Operation(summary = "分页获取会员站内消息", description = "分页获取会员站内消息")
	@RequestLogger("分页获取会员站内消息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<IPage<MemberMessage>> page(MemberMessageQueryVO memberMessageQueryVO,
		PageVO page) {
		memberMessageQueryVO.setMemberId(UserContext.getCurrentUser().getId());
		return Result.success(memberMessageService.getPage(memberMessageQueryVO, page));
	}

	@Operation(summary = "消息已读", description = "消息已读")
	@RequestLogger("消息已读")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{message_id}")
	public Result<Boolean> read(@PathVariable("message_id") String messageId) {
		return Result.success(
			memberMessageService.editStatus(MessageStatusEnum.ALREADY_READY.name(), messageId));
	}

	@Operation(summary = "消息放入回收站", description = "消息放入回收站")
	@RequestLogger("消息放入回收站")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping("/{message_id}")
	public Result<Boolean> deleteMessage(@PathVariable("message_id") String messageId) {
		return Result.success(
			memberMessageService.editStatus(MessageStatusEnum.ALREADY_REMOVE.name(), messageId));
	}

}
