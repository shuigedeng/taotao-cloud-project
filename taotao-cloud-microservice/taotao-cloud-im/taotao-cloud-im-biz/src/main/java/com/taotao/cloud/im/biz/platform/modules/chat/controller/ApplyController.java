package com.taotao.cloud.im.biz.platform.modules.chat.controller;

import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.service.ChatApplyService;
import com.platform.modules.chat.service.ChatFriendService;
import com.platform.modules.chat.vo.ApplyVo01;
import com.platform.modules.chat.vo.FriendVo02;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 申请
 */
@RestController
@Slf4j
@RequestMapping("/apply")
public class ApplyController extends BaseController {

	@Resource
	private ChatFriendService chatFriendService;

	@Resource
	private ChatApplyService chatApplyService;

	/**
	 * 申请添加
	 */
	@ApiVersion(VersionEnum.V1_0_0)
	@PostMapping("/add")
	public AjaxResult add(@Validated @RequestBody FriendVo02 friendVo) {
		chatFriendService.applyFriend(friendVo);
		return AjaxResult.success();
	}

	/**
	 * 申请记录
	 */
	@ApiVersion(VersionEnum.V1_0_0)
	@GetMapping("/list")
	public TableDataInfo list() {
		startPage("create_time desc");
		return getDataTable(chatApplyService.list());
	}

	/**
	 * 申请详情
	 */
	@ApiVersion(VersionEnum.V1_0_0)
	@GetMapping("/info/{applyId}")
	public AjaxResult getInfo(@PathVariable Long applyId) {
		return AjaxResult.success(chatApplyService.getInfo(applyId));
	}

	/**
	 * 同意申请
	 */
	@ApiVersion(VersionEnum.V1_0_0)
	@PostMapping("/agree")
	public AjaxResult agree(@Validated @RequestBody ApplyVo01 applyVo) {
		chatFriendService.agree(applyVo.getApplyId());
		return AjaxResult.success();
	}

	/**
	 * 拒绝申请
	 */
	@ApiVersion(VersionEnum.V1_0_0)
	@PostMapping("/refused")
	public AjaxResult refused(@Validated @RequestBody ApplyVo01 applyVo) {
		chatFriendService.refused(applyVo.getApplyId());
		return AjaxResult.success();
	}

	/**
	 * 忽略申请
	 */
	@ApiVersion(VersionEnum.V1_0_0)
	@PostMapping("/ignore")
	public AjaxResult ignore(@Validated @RequestBody ApplyVo01 applyVo) {
		chatFriendService.ignore(applyVo.getApplyId());
		return AjaxResult.success();
	}

}
