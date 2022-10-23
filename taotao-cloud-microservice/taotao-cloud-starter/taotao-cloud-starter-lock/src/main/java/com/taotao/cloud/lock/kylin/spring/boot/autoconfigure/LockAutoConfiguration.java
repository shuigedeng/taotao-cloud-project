package com.taotao.cloud.lock.kylin.spring.boot.autoconfigure;

import com.taotao.cloud.lock.kylin.aop.LockAspect;
import com.taotao.cloud.lock.kylin.exception.LockFailureException;
import com.taotao.cloud.lock.kylin.executor.LockExecutor;
import com.taotao.cloud.lock.kylin.fail.LockFailureCallBack;
import com.taotao.cloud.lock.kylin.key.DefaultLockKeyBuilder;
import com.taotao.cloud.lock.kylin.key.LockKeyBuilder;
import com.taotao.cloud.lock.kylin.template.LockTemplate;
import java.lang.reflect.Method;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 分布式锁自动配置器
 *
 * @author wangjinkui
 */
@Configuration
@EnableConfigurationProperties(KylinLockProperties.class)
public class LockAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(LockAutoConfiguration.class);

	@Autowired
	private KylinLockProperties properties;

	/**
	 * 锁 模板
	 *
	 * @param executors 集合对象 RedissonLockExecutor和 ZookeeperLockExecutor
	 * @return 锁 模板
	 */
	@SuppressWarnings("rawtypes")
	@Bean
	@ConditionalOnMissingBean
	public LockTemplate lockTemplate(List<LockExecutor> executors) {
		LOGGER.debug("kylin-lock init lock properties:{}", properties);
		LOGGER.debug("kylin-lock init lock executors:{}", executors);

		return new LockTemplate(properties, executors);
	}

	/**
	 * 分布式锁Key生成器
	 *
	 * @return 锁key生成器
	 */
	@Bean
	@ConditionalOnMissingBean
	public LockKeyBuilder lockKeyBuilder() {
		return new DefaultLockKeyBuilder();
	}

	/**
	 * 默认的获取锁失败 回调
	 *
	 * @return 锁失败回调
	 */
	@Bean(name = "lockFailureCallBack")
	@ConditionalOnMissingBean(name = "lockFailureCallBack")
	public LockFailureCallBack lockFailureCallBack() {
		return new LockFailureCallBack() {
			@Override
			public void callBack(Method method, Object[] args) {
				LOGGER.error("lock fail call back, method:{}, args:{}", method, args);
				throw new LockFailureException(DEFAULT_MESSAGE);
			}
		};
	}

	//--------------------------------LockAspect---------------------------------------------

	/**
	 * 分布式锁aop处理器
	 *
	 * @param lockTemplate            锁模板
	 * @param lockKeyBuilder          锁key生成器
	 * @param lockFailureCallBack     默认加锁失败回调
	 * @param lockFailureCallBackList 锁失败回调集合
	 * @return 锁增强
	 */
	@Bean("lockAspect")
	@ConditionalOnMissingBean(name = "lockAspect")
	public LockAspect lockAspect(LockTemplate lockTemplate,
		LockKeyBuilder lockKeyBuilder,
		LockFailureCallBack lockFailureCallBack,
		List<LockFailureCallBack> lockFailureCallBackList) {
		return new LockAspect(lockTemplate, lockKeyBuilder, lockFailureCallBack,
			lockFailureCallBackList, properties);
	}
}
