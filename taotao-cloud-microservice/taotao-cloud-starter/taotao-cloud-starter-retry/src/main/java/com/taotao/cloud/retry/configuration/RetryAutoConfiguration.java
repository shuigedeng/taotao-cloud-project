/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.retry.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.retry.aop.GuavaRetryingAspect;
import com.taotao.cloud.retry.properties.RetryProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Config
 *
 * <pre class="code">
 *     &#064;Service
 * public class TestRetryServiceImpl implements TestRetryService {
 *
 *     &#064;Override
 *     &#064;Retryable(value = Exception.class,maxAttempts = 3,backoff = @Backoff(delay = 2000,multiplier = 1.5))
 *     public int test(int code) throws Exception{
 *         System.out.println("test被调用,时间："+LocalTime.now());
 *           if (code==0){
 *               throw new Exception("情况不对头！");
 *           }
 *         System.out.println("test被调用,情况对头了！");
 *
 *         return 200;
 *     }
 * }
 *
 * </pre>
 * 来简单解释一下注解中几个参数的含义：
 * value：抛出指定异常才会重试
 * include：和value一样，默认为空，当exclude也为空时，默认所有异常
 * exclude：指定不处理的异常
 * maxAttempts：最大重试次数，默认3次
 * backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为2000L；
 * multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为2秒，第二次为3秒，第三次为4.5秒。
 * 当重试耗尽时还是失败，会出现什么情况呢？
 * 当重试耗尽时，RetryOperations可以将控制传递给另一个回调，即RecoveryCallback。Spring-Retry还提供了@Recover注解，用于@Retryable重试失败后处理方法。如果不需要回调方法，可以直接不写回调方法，那么实现的效果是，重试次数完了后，如果还是没成功没符合业务判断，就抛出异常。
 *
 * <pre class="code">
 *     &#064;Recover
 * public int recover(Exception e, int code){
 *    System.out.println("回调方法执行！！！！");
 *    //记日志到数据库 或者调用其余的方法
 *     return 400;
 * }
 * </pre>
 * <p>
 * 可以看到传参里面写的是 Exception e，这个是作为回调的接头暗号（重试次数用完了，还是失败，我们抛出这个Exception e通知触发这个回调方法）。对于@Recover注解的方法，需要特别注意的是：
 * 方法的返回值必须与@Retryable方法一致
 * 方法的第一个参数，必须是Throwable类型的，建议是与@Retryable配置的异常一致，其他的参数，需要哪个参数，写进去就可以了（@Recover方法中有的）
 * 该回调方法与重试方法写在同一个实现类里面
 * 5. 注意事项
 * <p>
 * 由于是基于AOP实现，所以不支持类里自调用方法
 * 如果重试失败需要给@Recover注解的方法做后续处理，那这个重试的方法不能有返回值，只能是void
 * 方法内不能使用try catch，只能往外抛异常
 * &#064;Recover注解来开启重试失败后调用的方法(注意,需跟重处理方法在同一个类中)，此注解注释的方法参数一定要是@Retryable抛出的异常，否则无法识别，可以在该方法中进行日志处理。
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/06/17 17:21
 */
@EnableRetry
@AutoConfiguration
@EnableConfigurationProperties(RetryProperties.class)
@ConditionalOnProperty(prefix = RetryProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class RetryAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(RetryAutoConfiguration.class, StarterName.CORE_STARTER);
	}

	// ***********************************Guava 重试机制的实现*******************************************

	@Bean
	public GuavaRetryingAspect guavaRetryingAspect() {
		return new GuavaRetryingAspect();
	}
}

