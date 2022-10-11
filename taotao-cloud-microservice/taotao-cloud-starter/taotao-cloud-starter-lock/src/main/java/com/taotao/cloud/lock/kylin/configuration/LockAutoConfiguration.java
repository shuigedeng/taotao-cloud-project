package com.taotao.cloud.lock.kylin.configuration;

import com.taotao.cloud.lock.kylin.annotation.KylinLock;
import com.taotao.cloud.lock.kylin.annotation.KylinLocks;
import com.taotao.cloud.lock.kylin.aop.DefaultLockInterceptor;
import com.taotao.cloud.lock.kylin.aop.LockAnnotationAdvisor;
import com.taotao.cloud.lock.kylin.aop.LockInterceptor;
import com.taotao.cloud.lock.kylin.aop.MultiLockInterceptor;
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
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;


/**
 * 分布式锁自动配置器
 *
 * @author wangjinkui
 */
@AutoConfiguration
@EnableConfigurationProperties(KylinLockProperties.class)
public class LockAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(LockAutoConfiguration.class);

	private final KylinLockProperties properties;

	public LockAutoConfiguration(KylinLockProperties properties) {
		this.properties = properties;
	}

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

	/**
	 * 分布式锁aop处理器
	 *
	 * @param lockTemplate            锁模板
	 * @param lockKeyBuilder          锁key生成器
	 * @param lockFailureCallBack     默认加锁失败回调
	 * @param lockFailureCallBackList 锁失败回调集合
	 * @return 锁增强
	 */
	@Bean("lockInterceptor")
	@ConditionalOnMissingBean(name = "lockInterceptor")
	public DefaultLockInterceptor lockInterceptor(LockTemplate lockTemplate,
		LockKeyBuilder lockKeyBuilder,
		LockFailureCallBack lockFailureCallBack,
		List<LockFailureCallBack> lockFailureCallBackList) {
		return new DefaultLockInterceptor(lockTemplate, lockKeyBuilder, lockFailureCallBack,
			lockFailureCallBackList, properties);
	}

	/**
	 * 分布式锁aop通知
	 *
	 * @param lockInterceptor 增强
	 * @return 顾问
	 */
	@Bean("lockAnnotationAdvisor")
	@ConditionalOnMissingBean(name = "lockAnnotationAdvisor")
	public LockAnnotationAdvisor lockAnnotationAdvisor(LockInterceptor lockInterceptor) {
		//顺序 @KylinLock @Transactional 加锁 ->事务 ->业务 ->提交事务 ->释放锁
		return new LockAnnotationAdvisor(lockInterceptor,
			AnnotationMatchingPointcut.forMethodAnnotation(
				KylinLock.class), Ordered.HIGHEST_PRECEDENCE);
	}

	//-----------------------------------重复注解---------------------------------------------

	/**
	 * 重复注解 分布式锁aop处理器
	 *
	 * @param lockTemplate            锁模板
	 * @param lockKeyBuilder          key生成器
	 * @param lockFailureCallBack     默认失败回调
	 * @param lockFailureCallBackList 失败回调集合
	 * @return 分布式锁aop处理器
	 */
	@Bean("multiLockInterceptor")
	@ConditionalOnMissingBean(name = "multiLockInterceptor")
	public LockInterceptor multiLockInterceptor(LockTemplate lockTemplate,
		LockKeyBuilder lockKeyBuilder,
		LockFailureCallBack lockFailureCallBack,
		List<LockFailureCallBack> lockFailureCallBackList) {
		return new MultiLockInterceptor(lockTemplate, lockKeyBuilder, lockFailureCallBack,
			lockFailureCallBackList, properties);
	}

	/**
	 * 重复注解 分布式锁aop通知
	 *
	 * @param multiLockInterceptor 锁增强
	 * @return 顾问
	 */
	@Bean("multiLockAnnotationAdvisor")
	@ConditionalOnMissingBean(name = "multiLockAnnotationAdvisor")
	public LockAnnotationAdvisor multiLockAnnotationAdvisor(LockInterceptor multiLockInterceptor) {
		return new LockAnnotationAdvisor(multiLockInterceptor,
			AnnotationMatchingPointcut.forMethodAnnotation(
				KylinLocks.class), Ordered.HIGHEST_PRECEDENCE);
	}

	//-----------------------------------重复注解---------------------------------------------

}
