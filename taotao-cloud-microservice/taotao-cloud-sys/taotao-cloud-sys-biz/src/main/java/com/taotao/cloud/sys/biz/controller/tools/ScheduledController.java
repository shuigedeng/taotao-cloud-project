package com.taotao.cloud.sys.biz.controller.tools;


import com.taotao.cloud.sys.biz.service.IScheduledJobService;
import com.taotao.cloud.web.schedule.core.ScheduledManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ScheduledJobController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-11 15:56:29
 */
@Validated
@RestController
@Tag(name = "工具管理端-scheduled定时任务管理API", description = "工具管理端-scheduled管理API")
@RequestMapping("/sys/tools/scheduled")
public class ScheduledController {

	@Autowired(required = false)
	private ScheduledManager scheduledManager;

	@Autowired
	private IScheduledJobService IScheduledJobService;

	/**
	 * 手动执行一次任务
	 *
	 * @param name 定时任务名称
	 */
	@PostMapping("/{name}/run")
	public void runScheduled(@PathVariable("name") String name) {
		scheduledManager.runScheduled(name);
	}

	/**
	 * 结束正在执行中的任务，跳过这次运行 只有在每个前置增强器结束之后才会判断是否需要跳过此次运行
	 *
	 * @param name 定时任务名称
	 */
	@PostMapping("/{name}/callOff")
	public void callOffScheduled(@PathVariable("name") String name) {
		scheduledManager.callOffScheduled(name);
	}

	/**
	 * 终止定时任务
	 *
	 * @param name 定时任务名称
	 */
	@DeleteMapping("/{name}")
	public void cancelScheduled(@PathVariable("name") String name) {
		scheduledManager.cancelScheduled(name);
	}

	///**
	// * 获取日志文件信息
	// */
	//@GetMapping("/log/files")
	//public List<ScheduledLogFile> logFiles() {
	//    return scheduledManager.getScheduledLogFiles();
	//}
	//
	///**
	// * 获取日志信息
	// */
	//@GetMapping("/log/{fileName}")
	//public List<ScheduledLogModel> getLogs(@PathVariable("fileName") String fileName) {
	//    return scheduledManager.getScheduledLogs(fileName);
	//}

	@PostMapping("/{name}/add")
	public void addCronScheduled(@PathVariable("name") String name, @RequestBody String cron) {
		scheduledManager.addCronScheduled(name, cron);
	}

	@PostMapping("/{name}/set")
	public void setScheduledCron(@PathVariable("name") String name, @RequestBody String cron) {
		scheduledManager.setScheduledCron(name, cron);
	}


	/**
	 * 以FixedDelay模式启动定时任务
	 *
	 * @param name         定时任务名称
	 * @param fixedDelay   运行间隔时间
	 * @param initialDelay 首次运行延迟时间
	 */
	@PostMapping("/{name}/add/{fixedDelay}/{initialDelay}")
	public void addFixedDelayScheduled(@PathVariable("name") String name,
		@PathVariable("fixedDelay") Long fixedDelay,
		@PathVariable("initialDelay") Long initialDelay) {
		scheduledManager.addFixedDelayScheduled(name, fixedDelay, initialDelay);
	}

	/**
	 * 以FixedDelay模式启动定时任务
	 *
	 * @param name       定时任务名称
	 * @param fixedDelay 运行间隔时间
	 */
	@PostMapping("/{name}/add/{fixedDelay}")
	public void addFixedDelayScheduled(@PathVariable("name") String name,
		@PathVariable("fixedDelay") Long fixedDelay) {
		scheduledManager.addFixedDelayScheduled(name, fixedDelay);
	}

	/**
	 * 将定时任务转为FixedDelay模式运行，并修改执行间隔的参数值
	 *
	 * @param name       定时任务名称
	 * @param fixedDelay 运行间隔时间
	 */
	@PostMapping("/{name}/set/{fixedDelay}")
	public void setScheduledFixedDelay(@PathVariable("name") String name,
		@PathVariable("fixedDelay") Long fixedDelay) {
		scheduledManager.setScheduledFixedDelay(name, fixedDelay);
	}


	/**
	 * 以FixedRate模式启动定时任务
	 *
	 * @param name         定时任务名称
	 * @param fixedRate    运行间隔时间
	 * @param initialDelay 首次运行延迟时间
	 */
	@PostMapping("/{name}/add/{fixedRate}/{initialDelay}")
	public void addFixedRateScheduled(@PathVariable("name") String name,
		@PathVariable("fixedRate") Long fixedRate,
		@PathVariable("initialDelay") Long initialDelay) {
		scheduledManager.addFixedRateScheduled(name, fixedRate, initialDelay);
	}

	/**
	 * 以FixedRate模式启动定时任务
	 *
	 * @param name      定时任务名称
	 * @param fixedRate 运行间隔时间
	 */
	@PostMapping("/{name}/add/{fixedRate}")
	public void addFixedRateScheduled(@PathVariable("name") String name,
		@PathVariable("fixedRate") Long fixedRate) {
		scheduledManager.addFixedRateScheduled(name, fixedRate);
	}

	/**
	 * 将定时任务转为FixedRate模式运行，并修改执行间隔的参数值
	 *
	 * @param name      定时任务名称
	 * @param fixedRate 运行间隔时间
	 */
	@PostMapping("/{name}/set/{fixedRate}")
	public void setScheduledFixedRate(@PathVariable("name") String name,
		@PathVariable("fixedRate") Long fixedRate) {
		scheduledManager.setScheduledFixedRate(name, fixedRate);
	}

	@GetMapping("/all")
	public List<String> getAllSuperScheduledName() {
		return scheduledManager.getAllSuperScheduledName();
	}

	@GetMapping("/run")
	public List<String> getRunScheduledName() {
		return scheduledManager.getRunScheduledName();
	}
}
