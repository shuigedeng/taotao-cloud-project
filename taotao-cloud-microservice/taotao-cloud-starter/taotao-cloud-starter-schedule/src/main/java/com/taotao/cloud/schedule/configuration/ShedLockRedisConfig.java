package com.taotao.cloud.schedule.configuration;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.Date;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description SchedulerLock 基于 Redis 的配置
 **/
@Configuration
//defaultLockAtMostFor 指定在执行节点结束时应保留锁的默认时间使用ISO8601 Duration格式
//作用就是在被加锁的节点挂了时，无法释放锁，造成其他节点无法进行下一任务
//这里默认55s
//关于ISO8601 Duration格式用的不到，具体可上网查询下相关资料，应该就是一套规范，规定一些时间表达方式
@EnableSchedulerLock(defaultLockAtMostFor = "PT55S")
public class ShedLockRedisConfig {

    @Value("${spring.profiles.active}")
    private String env;

    @Bean
    public LockProvider lockProvider(RedisConnectionFactory connectionFactory) {
        //环境变量 -需要区分不同环境避免冲突，如dev环境和test环境，两者都部署时，只有一个实例进行，此时会造成相关环境未启动情况
        return new RedisLockProvider(connectionFactory, env);
    }


	//区分服务
	@Value("${server.port}")
	private String port;

	@Scheduled(cron = "0 */1 * * * ?")
	/**
	 * lockAtLeastForString的作用是为了防止在任务开始之初由于各个服务器同名任务的服务器时间差，启动时间差等这些造成的一些问题，有了这个时间设置后，
	 *     就可以避免因为上面这些小的时间差造成的一些意外，保证一个线程在抢到锁后，即便很快执行完，也不要立即释放，留下一个缓冲时间。
	 *     这样等多个线程都启动后，由于任务已经被锁定，其他没有获得锁的任务也不会再去抢锁。注意这里的时间不要设置几秒几分钟，尽量大些
	 *lockAtMostForString 这个设置的作用是为了防止抢到锁的那个线程，因为一些意外死掉了，而锁又始终不被释放。
	 *     这样的话，虽然当前执行周期虽然失败了，但以后的执行周期如果这里一直不释放的话，后面就永远执行不到了。
	 *     它的目的不在于隐藏任务，更重要的是，释放锁，并且查找解决问题。
	 *至于是否带有string后缀，只是2种表达方式，数字类型的就是毫秒数，字符串类型的就有自己固定的格式 ，例如：PT30S  30s时间设置，单位可以是S,M,H
	 *
	 * @SchedulerLock注解一共支持五个参数，分别是
	 *
	 * name：用来标注一个定时服务的名字，被用于写入数据库作为区分不同服务的标识，如果有多个同名定时任务则同一时间点只有一个执行成功
	 * lockAtMostFor：成功执行任务的节点所能拥有独占锁的最长时间，单位是毫秒ms
	 * lockAtMostForString：成功执行任务的节点所能拥有的独占锁的最长时间的字符串表达，例如“PT14M”表示为14分钟，单位可以是S,M,H
	 * lockAtLeastFor：成功执行任务的节点所能拥有独占所的最短时间，单位是毫秒ms
	 * lockAtLeastForString：成功执行任务的节点所能拥有的独占锁的最短时间的字符串表达，例如“PT14M”表示为14分钟,单位可以是S,M,H
	 */
	@SchedulerLock(name = "scheduledController_notice", lockAtLeastFor = "PT15M", lockAtMostFor = "PT14M")
	public void notice() {
		try {
			LogUtils.info(port + "- 执行定时器 scheduledController_notice");
		} catch (Exception e) {
			LogUtils.error("异常信息:", e);
		}
	}

	@Component
	public class TaskSchedule {

		/**

		 * 每分钟执行一次

		 * [秒] [分] [小时] [日] [月] [周] [年]

		 */
		@Scheduled(cron = "1 * * * * ?")
		@SchedulerLock(name = "synchronousSchedule")
		public void SynchronousSchedule() {
			System.out.println("Start run schedule to synchronous data:" + new Date());
		}

	}

}
