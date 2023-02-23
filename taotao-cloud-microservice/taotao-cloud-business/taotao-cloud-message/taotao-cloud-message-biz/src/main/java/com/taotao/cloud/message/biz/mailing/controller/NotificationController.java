
package com.taotao.cloud.message.biz.mailing.controller;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.message.biz.mailing.entity.Notification;
import com.taotao.cloud.message.biz.mailing.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *  系统通知
 */
@RestController
@RequestMapping("/notification")
@Tags({
	@Tag(name = "消息管理接口"),
	@Tag(name = "通知管理接口")
})
public class NotificationController extends BaseWriteableRestController<Notification, String> {

	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Override
	public WriteableService<Notification, String> getWriteableService() {
		return notificationService;
	}

	@Operation(summary = "条件查询通知信息分页数据", description = "根据输入的字段条件查询通知信息",
		responses = {
			@ApiResponse(description = "详情列表", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))})
	@Parameters({
		@Parameter(name = "pageNumber", required = true, description = "当前页码", schema = @Schema(type = "integer")),
		@Parameter(name = "pageSize", required = true, description = "每页显示数量", schema = @Schema(type = "integer")),
		@Parameter(name = "userId", required = true, description = "用户ID"),
		@Parameter(name = "category", description = "通知类别", schema = @Schema(type = "integer")),
		@Parameter(name = "read", description = "是否已读", schema = @Schema(type = "boolean")),
	})
	@GetMapping("/condition")
	public Result<Map<String, Object>> findByCondition(
		@NotNull @RequestParam(value = "pageNumber") Integer pageNumber,
		@NotNull @RequestParam(value = "pageSize") Integer pageSize,
		@NotNull @RequestParam(value = "userId") String userId,
		@RequestParam(value = "category", required = false) Integer category,
		@RequestParam(value = "read", required = false) Boolean read) {
		NotificationCategory notificationCategory = NotificationCategory.get(category);
		if (ObjectUtils.isNotEmpty(notificationCategory)) {
			if (notificationCategory == NotificationCategory.ANNOUNCEMENT) {
				notificationService.pullAnnouncements(userId);
			}
		}

		Page<Notification> pages = notificationService.findByCondition(pageNumber, pageSize, userId,
			notificationCategory, read);
		return result(pages);
	}

	@Operation(summary = "全部通知已读", description = "根据用户ID设置该用户的全部通知为已读",
		responses = {
			@ApiResponse(description = "影响数据条目数", content = @Content(mediaType = "application/json"))})
	@Parameters({
		@Parameter(name = "userId", description = "用户ID")
	})
	@PutMapping("/all-read")
	public Result<Integer> setAllRead(@RequestParam("userId") String userId) {
		Integer result = notificationService.setAllRead(userId);
		return Result.success("操作成功", result);
	}
}
