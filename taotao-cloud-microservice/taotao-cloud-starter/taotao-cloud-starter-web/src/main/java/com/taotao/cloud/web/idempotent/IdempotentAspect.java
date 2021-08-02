package com.taotao.cloud.web.idempotent;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.common.aspect.BaseAspect;
import com.taotao.cloud.common.lock.ZLock;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.redis.lock.RedissonDistributedLock;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 注解执行器 处理重复请求 和串行指定条件的请求
 * <p>
 * 两种模式的拦截 1.rid 是针对每一次请求的 2.key+val 是针对相同参数请求
 * </p>
 *
 * @author shuigedeng
 */
@Aspect
@ConditionalOnClass(RedisRepository.class)
public class IdempotentAspect extends BaseAspect {

	private final ThreadLocal<String> PER_FIX_KEY = new ThreadLocal<String>();

	/**
	 * 配置注解后 默认开启
	 */
	private final boolean enable = true;

	/**
	 * request请求头中的key
	 */
	private final static String HEADER_RID_KEY = "RID";

	/**
	 * redis中锁的key前缀
	 */
	private static final String REDIS_KEY_PREFIX = "RID:";

	/**
	 * 锁等待时长
	 */
	private static final int LOCK_WAIT_TIME = 10;

	private final RedissonDistributedLock redissonDistributedLock;


	public IdempotentAspect(RedissonDistributedLock redissonDistributedLock) {
		this.redissonDistributedLock = redissonDistributedLock;
	}

	@Pointcut("@annotation(vip.mate.core.ide.annotation.Ide)")
	public void watchIde() {

	}

	@Before("watchIde()")
	public void doBefore(JoinPoint joinPoint) throws Exception {
		Idempotent idempotent = getAnnotation(joinPoint, Idempotent.class);

		if (enable && null != idempotent) {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
			if (null == attributes) {
				throw new IdempotentException("请求数据为空");
			}
			HttpServletRequest request = attributes.getRequest();

			//1.判断模式
			if (idempotent.ideTypeEnum() == IdempotentTypeEnum.ALL
				|| idempotent.ideTypeEnum() == IdempotentTypeEnum.RID) {
				//2.1.通过rid模式判断是否属于重复提交
				String rid = request.getHeader(HEADER_RID_KEY);

				try {
					if (StringUtils.isNotBlank(rid)) {
						ZLock result = redissonDistributedLock.tryLock(REDIS_KEY_PREFIX + rid,
							LOCK_WAIT_TIME,
							TimeUnit.MILLISECONDS);
						if (Objects.isNull(result)) {
							throw new IdempotentException("命中RID重复请求");
						}
						LogUtil.debug("msg1=当前请求已成功记录,且标记为0未处理,,{0}={1}", HEADER_RID_KEY, rid);
					} else {
						LogUtil.warn(
							"msg1=header没有rid,防重复提交功能失效,,remoteHost={0}" + request.getRemoteHost());
					}
				} catch (Exception e) {
					LogUtil.error("获取redis锁发生异常", e);
					throw e;
				}
			}

			if (idempotent.ideTypeEnum() == IdempotentTypeEnum.ALL
				|| idempotent.ideTypeEnum() == IdempotentTypeEnum.KEY) {
				//2.2.通过自定义key模式判断是否属于重复提交
				String key = idempotent.key();
				if (StringUtils.isNotBlank(key)) {
					String val = "";
					Object[] paramValues = joinPoint.getArgs();
					String[] paramNames = ((CodeSignature) joinPoint.getSignature())
						.getParameterNames();
					//获取自定义key的value
					for (int i = 0; i < paramNames.length; i++) {
						String params = JSON.toJSONString(paramValues[i]);
						if (params.startsWith("{")) {
							//如果是对象
							//通过key获取value
							JSONObject jsonObject = JSON.parseObject(params);
							val = jsonObject.getString(key);
						} else if (key.equals(paramNames[i])) {
							//如果是单个k=v
							val = params;
						} else {
							//如果自定义的key,在请求参数中没有此参数,说明非法请求
							LogUtil.warn("自定义的key,在请求参数中没有此参数,防重复提交功能失效");
						}
					}

					//判断重复提交的条件
					String perFix = idempotent.perFix();
					if (StringUtils.isNotBlank(val)) {
						perFix = perFix + ":" + val;

						try {
							ZLock result = redissonDistributedLock.tryLock(perFix, LOCK_WAIT_TIME,
								TimeUnit.MILLISECONDS);
							if (!Objects.nonNull(result)) {
								String targetName = joinPoint.getTarget().getClass().getName();
								String methodName = joinPoint.getSignature().getName();
								LogUtil.error("msg1=不允许重复执行,,key={0},,targetName={1},,methodName={2}",
									perFix, targetName, methodName);
								throw new IdempotentException("不允许重复提交");
							}
							//存储在当前线程
							PER_FIX_KEY.set(perFix);
							LogUtil.info("msg1=当前请求已成功锁定:{0}", perFix);
						} catch (Exception e) {
							LogUtil.error("获取redis锁发生异常", e);
							throw e;
						}
					} else {
						LogUtil.warn("自定义的key,在请求参数中value为空,防重复提交功能失效");
					}
				}
			}
		}
	}

	@After("watchIde()")
	public void doAfter(JoinPoint joinPoint) throws Throwable {
		try {
			Idempotent idempotent = getAnnotation(joinPoint, Idempotent.class);
			if (enable && null != idempotent) {

				if (idempotent.ideTypeEnum() == IdempotentTypeEnum.ALL
					|| idempotent.ideTypeEnum() == IdempotentTypeEnum.RID) {
					ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
					HttpServletRequest request = attributes.getRequest();
					String rid = request.getHeader(HEADER_RID_KEY);
					if (StringUtils.isNotBlank(rid)) {
						try {
//							redisService.unLock(REDIS_KEY_PREFIX + rid);
							LogUtil.info("msg1=当前请求已成功处理,,rid={0}", rid);
						} catch (Exception e) {
							LogUtil.error("释放redis锁异常", e);
						}
					}
					PER_FIX_KEY.remove();
				}

				if (idempotent.ideTypeEnum() == IdempotentTypeEnum.ALL
					|| idempotent.ideTypeEnum() == IdempotentTypeEnum.KEY) {
					// 自定义key
					String key = idempotent.key();
					if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(PER_FIX_KEY.get())) {
						try {
//							redisService.unLock(PER_FIX_KEY.get());
							LogUtil.info("msg1=当前请求已成功释放,,key={0}", PER_FIX_KEY.get());
							PER_FIX_KEY.set(null);
							PER_FIX_KEY.remove();
						} catch (Exception e) {
							LogUtil.error("释放redis锁异常", e);
						}
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
	}
}
