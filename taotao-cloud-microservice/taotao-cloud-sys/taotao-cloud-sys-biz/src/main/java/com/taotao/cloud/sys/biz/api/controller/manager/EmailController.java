package com.taotao.cloud.sys.biz.api.controller.manager;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.redis.delay.config.RedissonTemplate;
import com.taotao.cloud.redis.redisson.RedisDelayQueue;
import com.taotao.cloud.security.annotation.NotAuth;
import com.taotao.cloud.sys.api.dto.EmailDTO;
import com.taotao.cloud.sys.api.vo.alipay.EmailVO;
import com.taotao.cloud.sys.biz.model.entity.config.EmailConfig;
import com.taotao.cloud.sys.biz.mapstruct.IEmailMapStruct;
import com.taotao.cloud.sys.biz.service.IEmailConfigService;
import com.taotao.cloud.web.quartz.QuartzManager;
import com.taotao.cloud.web.schedule.core.ScheduledManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * EmailController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-21 17:11:55
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-邮件管理API", description = "工具管理端-邮件管理API")
@RequestMapping("/sys/tools/email")
public class EmailController {

	private final RedisDelayQueue redisDelayQueue;
	private final RedissonTemplate redissonTemplate;
	private final QuartzManager quartzManager;
	private final ScheduledManager scheduledManager;
	private final IEmailConfigService emailService;

	@Operation(summary = "查询邮件配置信息", description = "查询邮件配置信息")
	@RequestLogger("查询邮件配置信息")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<EmailConfig> get() {
		return Result.success(emailService.find());
	}

	@Operation(summary = "配置邮件", description = "配置邮件")
	@RequestLogger("配置邮件")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping
	public Result<Boolean> update(@Validated @RequestBody EmailConfig emailConfig) {
		emailService.update(emailConfig, emailService.find());
		return Result.success(true);
	}

	@Operation(summary = "添加配置邮件", description = "添加配置邮件")
	@RequestLogger("添加配置邮件")
	@NotAuth
	@PostMapping
	public Result<Boolean> add(@Validated @RequestBody EmailDTO emailDTO) {
		EmailConfig emailConfig = IEmailMapStruct.INSTANCE.emailDTOToEmailConfigt(emailDTO);
		emailService.save(emailConfig);

		//for (int i = 0; i < 10; i++) {
		//	Integer random = new Random().nextInt(300) + 1;
		//	Map<String, String> map1 = new HashMap<>();
		//	map1.put("orderId", String.valueOf(2));
		//	map1.put("remark", "订单支付超时，自动取消订单");
		//	map1.put("random", String.valueOf(random));
		//	map1.put("timestamp", String.valueOf(System.currentTimeMillis()));
		//	redisDelayQueue.addDelayQueue(map1, random, TimeUnit.SECONDS,
		//		RedisDelayQueueEnum.ORDER_PAYMENT_TIMEOUT.getCode());
		//}

		//CarLbsDto carLbsDto = new CarLbsDto();
		//carLbsDto.setCid("1");
		//carLbsDto.setBusinessType("0");
		//carLbsDto.setCity("北京市");
		//carLbsDto.setCityId("265");
		//carLbsDto.setName("fsfds");
		//carLbsDto.setCarNum("156156");
		//redissonTemplate.sendWithDelay("riven", carLbsDto, 8000);

		//QuartzJobModel jobModel = new QuartzJobModel();
		//jobModel.setId(123L);
		//jobModel.setBeanName("quartzJobTest");
		//jobModel.setCronExpression("0/30 * * * * ?");
		//jobModel.setJobName("test");
		//jobModel.setMethodName("test");
		//jobModel.setParams("sdfsdf");
		//jobModel.setCreateTime(LocalDateTime.now());
		//quartzManager.addJob(jobModel);

		List<String> runScheduledName = scheduledManager.getRunScheduledName();
		LogUtil.info("===============: ", runScheduledName);

		return Result.success(true);
	}

	@Operation(summary = "发送邮件", description = "发送邮件")
	@RequestLogger("发送邮件")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping("/send")
	public Result<Boolean> send(@Validated @RequestBody EmailVO emailVo) throws Exception {
		emailService.send(emailVo, emailService.find());
		return Result.success(true);
	}
}
