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
package com.taotao.cloud.web.configuration;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.web.annotation.LoginUser;
import com.taotao.cloud.web.interceptor.DoubtApiInterceptor;
import com.taotao.cloud.web.interceptor.HeaderThreadLocalInterceptor;
import com.taotao.cloud.web.listener.RequestMappingScanListener;
import com.taotao.cloud.web.properties.FilterProperties;
import com.taotao.cloud.web.properties.InterceptorProperties;
import com.taotao.cloud.web.validation.converter.IntegerToEnumConverterFactory;
import com.taotao.cloud.web.validation.converter.String2DateConverter;
import com.taotao.cloud.web.validation.converter.String2LocalDateConverter;
import com.taotao.cloud.web.validation.converter.String2LocalDateTimeConverter;
import com.taotao.cloud.web.validation.converter.String2LocalTimeConverter;
import com.taotao.cloud.web.validation.converter.StringToEnumConverterFactory;
import okhttp3.OkHttpClient;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutDeferredResultProcessingInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

/**
 * 自定义mvc配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:30:20
 */
@AutoConfiguration
@EnableConfigurationProperties({
	FilterProperties.class,
	InterceptorProperties.class,
})
public class WebMvcAutoConfiguration implements WebMvcConfigurer, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(WebMvcAutoConfiguration.class, StarterName.WEB_STARTER);
	}

	/**
	 * redisRepository
	 */
	@Autowired
	private RedisRepository redisRepository;
	/**
	 * 异步线程池任务执行人
	 */
	@Autowired
	private AsyncThreadPoolTaskExecutor asyncThreadPoolTaskExecutor;
	/**
	 * 拦截器属性
	 */
	@Autowired
	private InterceptorProperties interceptorProperties;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
	}

	//路径匹配规则
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		//// 设置是否模糊匹配，默认真。例如/user是否匹配/user.*。如果真，也就是说"/user.html"的请求会被"/user"的Controller所拦截。
		//configurer.setUseSuffixPatternMatch(false);
		//// 设置是否自动后缀模式匹配，默认真。如/user是否匹配/user/。如果真，也就是说, "/user"和"/user/"都会匹配到"/user"的Controller。
		//configurer.setUseTrailingSlashMatch(true);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	}

	//内容协商策略 配置内容裁决的一些参数
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		//// 自定义策略
		//configurer.favorPathExtension(true)// 是否通过请求Url的扩展名来决定mediaType，默认true
		//	.ignoreAcceptHeader(true)// 不检查Accept请求头
		//	.parameterName("mediaType")
		//	.defaultContentType(MediaType.TEXT_HTML)// 设置默认的MediaType
		//	.mediaType("html", MediaType.TEXT_HTML)// 请求以.html结尾的会被当成MediaType.TEXT_HTML
		//	.mediaType("json", MediaType.APPLICATION_JSON)// 请求以.json结尾的会被当成MediaType.APPLICATION_JSON
		//	.mediaType("xml", MediaType.APPLICATION_ATOM_XML);// 请求以.xml结尾的会被当成MediaType.APPLICATION_ATOM_XML
		//
		//// 或者下面这种写法
		//Map<String, MediaType> map = new HashMap<>();
		//map.put("html", MediaType.TEXT_HTML);
		//map.put("json", MediaType.APPLICATION_JSON);
		//map.put("xml", MediaType.APPLICATION_ATOM_XML);
		//// 指定基于参数的解析类型
		//ParameterContentNegotiationStrategy negotiationStrategy = new ParameterContentNegotiationStrategy(map);
		//// 指定基于请求头的解析
		//configurer.strategies(Arrays.asList(negotiationStrategy));
	}

	//异步调用支持
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		// 注册callable拦截器
		configurer.registerCallableInterceptors(new TimeoutCallableProcessingInterceptor());
		// 注册deferredResult拦截器
		configurer.registerDeferredResultInterceptors(
			new TimeoutDeferredResultProcessingInterceptor());
		// 异步请求超时时间
		configurer.setDefaultTimeout(1000);
		// 设定异步请求线程池callable等, spring默认线程不可重用
		configurer.setTaskExecutor(asyncThreadPoolTaskExecutor);
	}

	//参数解析器
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new LoginUserArgumentResolver());
	}

	//返回值处理器
	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
	}

	//拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (interceptorProperties.getHeader()) {
			registry.addInterceptor(new HeaderThreadLocalInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/actuator/**");
		}

		if (interceptorProperties.getDoubtApi()) {
			registry.addInterceptor(new DoubtApiInterceptor(interceptorProperties))
				.addPathPatterns("/**")
				.excludePathPatterns("/actuator/**");
		}
	}

	//信息转化器
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		//创建fastJson消息转换器
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		//创建配置类
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		//修改配置返回内容的过滤
		fastJsonConfig.setSerializerFeatures(
			SerializerFeature.DisableCircularReferenceDetect,
			SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteNullStringAsEmpty
		);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		//将fastjson添加到视图消息转换器列表内
		converters.add(fastConverter);

		//把自定义的序列化规则设置进入转换器里
		for (HttpMessageConverter<?> converter : converters) {
			if (converter instanceof MappingJackson2HttpMessageConverter jackson2Converter) {
				jackson2Converter.setObjectMapper(JsonUtil.MAPPER);
			}
		}
	}

	//信息转化器扩展
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

	}

	//异常处理器扩展
	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

	}

	//格式化器和转换器
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverterFactory(new IntegerToEnumConverterFactory());
		registry.addConverterFactory(new StringToEnumConverterFactory());

		registry.addConverter(new String2DateConverter());
		registry.addConverter(new String2LocalDateConverter());
		registry.addConverter(new String2LocalDateTimeConverter());
		registry.addConverter(new String2LocalTimeConverter());
	}

	//静态资源处理器
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/**")
			.addResourceLocations("classpath:/static/");
	}

	//视图控制器
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry
			.addViewController("/index")
			.setViewName("index");
	}

	//视图解析器
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		//请求视图文件的前缀地址
		internalResourceViewResolver.setPrefix("/WEB-INF/jsp/");
		//请求视图文件的后缀
		internalResourceViewResolver.setSuffix(".jsp");
		registry.viewResolver(internalResourceViewResolver);
	}

	@Bean
	@LoadBalanced
	public OkHttpClient.Builder builder() {
		return new OkHttpClient.Builder();
	}

	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
			.configure()
			//快速失败模式
			.failFast(true)
			.buildValidatorFactory();
		return validatorFactory.getValidator();
	}

	//@Bean
	//public RequestContextListener requestContextListener() {
	//	return new RequestContextListener();
	//}

	@Bean
	public RequestMappingScanListener requestMappingScanListener() {
		return new RequestMappingScanListener(redisRepository);
	}

	/**
	 * 通过header里的token获取用户信息
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @see <a
	 * href="https://my.oschina.net/u/4149877/blog/3143391/print">https://my.oschina.net/u/4149877/blog/3143391/print</a>
	 * @see <a
	 * href="https://blog.csdn.net/aiyaya_/article/details/79221733">https://blog.csdn.net/aiyaya_/article/details/79221733</a>
	 * @since 2021-09-02 21:32:45
	 */
	public static class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

		public LoginUserArgumentResolver() {
		}

		@Override
		public boolean supportsParameter(MethodParameter parameter) {
			boolean isHasEnableUserAnn = parameter.hasParameterAnnotation(LoginUser.class);
			boolean isHasLoginUserParameter = parameter.getParameterType().isAssignableFrom(SecurityUser.class);
			return isHasEnableUserAnn && isHasLoginUserParameter;
		}

		@Override
		public Object resolveArgument(MethodParameter methodParameter,
									  ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest,
									  WebDataBinderFactory webDataBinderFactory) throws Exception {
			LoginUser user = methodParameter.getParameterAnnotation(LoginUser.class);
			boolean value = user.value();
			HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
			SecurityUser loginUser = SecurityUtil.getCurrentUser();

			//根据value状态获取更多用户信息，待实现
			return loginUser;
		}
	}
}
