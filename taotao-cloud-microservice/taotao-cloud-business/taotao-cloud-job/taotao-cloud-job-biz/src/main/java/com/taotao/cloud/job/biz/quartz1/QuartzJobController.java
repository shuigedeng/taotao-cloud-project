package com.taotao.cloud.job.biz.quartz1;

import cn.hutool.core.bean.BeanUtil;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.job.biz.quartz1.entity.QuartzJobEntity;
import com.taotao.cloud.job.biz.quartz1.param.QuartzJobDTO;
import com.taotao.cloud.job.biz.quartz1.param.QuartzJobQuery;
import com.taotao.cloud.job.biz.quartz1.service.QuartzJobService;
import com.taotao.cloud.job.biz.quartz1.vo.QuartzJobVO;
import com.taotao.cloud.job.quartz.quartz1.utils.CronUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时任务
 */
@Tag(name = "定时任务")
@RestController
@RequestMapping("/job/quartz1/api/quartz")
public class QuartzJobController {

	private final QuartzJobService quartzJobService;

	public QuartzJobController(QuartzJobService quartzJobService) {
		this.quartzJobService = quartzJobService;
	}

	@Operation(summary = "添加")
	@PostMapping("/add")
	public Result<Boolean> add(@RequestBody QuartzJobDTO quartzJobDTO) {
		if (!CronUtils.isValid(quartzJobDTO.getCronExpression())) {
			return Result.fail("操作失败，Cron表达式不正确");
		}
		quartzJobService.addJob(quartzJobDTO);
		return Result.success(true);
	}

	@Operation(summary = "更新")
	@PostMapping("/update")
	public Result<Boolean> update(@RequestBody QuartzJobDTO quartzJobDTO) {
		if (!CronUtils.isValid(quartzJobDTO.getCronExpression())) {
			return Result.fail("操作失败，Cron表达式不正确");
		}
		quartzJobService.updateJob(quartzJobDTO);
		return Result.success(true);
	}

	@Operation(summary = "分页")
	@GetMapping("/page")
	public Result<PageResult<QuartzJobVO>> page(QuartzJobQuery quartzJobQuery) {
		return Result.success(quartzJobService.page(quartzJobQuery));
	}

	@Operation(summary = "单条")
	@GetMapping("/findById")
	public Result<QuartzJobVO> findById(Long id) {
		QuartzJobEntity quartzJob = quartzJobService.findById(id);

		QuartzJobVO quartzJobVO = BeanUtil.copyProperties(quartzJob, QuartzJobVO.class);
		return Result.success(quartzJobVO);
	}

	@Operation(summary = "启动")
	@PostMapping("/start")
	public Result<Boolean> start(Long id) {
		quartzJobService.start(id);
		return Result.success(true);
	}

	@Operation(summary = "停止")
	@PostMapping("/stop")
	public Result<Boolean> stop(Long id) {
		quartzJobService.stopJob(id);
		return Result.success(true);
	}

	@Operation(summary = "立即执行")
	@PostMapping("/execute")
	public Result<Boolean> execute(Long id) {
		quartzJobService.runOnce(id);
		return Result.success(true);
	}

	@Operation(summary = "删除")
	@DeleteMapping("/delete")
	public Result<Boolean> delete(Long id) {
		quartzJobService.deleteJob(id);
		return Result.success(true);
	}

	@Operation(summary = "判断是否是定时任务类")
	@GetMapping("/judgeJobClass")
	public Result<String> judgeJobClass(String jobClassName) {
		return Result.success(quartzJobService.judgeJobClass(jobClassName));
	}

	@Operation(summary = "同步定时任务状态")
	@PostMapping("/syncJobStatus")
	public Result<Boolean> syncJobStatus() {
		quartzJobService.syncJobStatus();
		return Result.success(true);
	}
}
