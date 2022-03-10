/*
 * Copyright 2002-2021 the original author or authors.
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


import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.web.annotation.EnableUser;
import com.taotao.cloud.web.filter.TenantFilter;
import com.taotao.cloud.web.filter.TraceFilter;
import com.taotao.cloud.web.filter.VersionFilter;
import com.taotao.cloud.web.filter.WebContextFilter;
import com.taotao.cloud.web.interceptor.DoubtApiInterceptor;
import com.taotao.cloud.web.interceptor.HeaderThreadLocalInterceptor;
import com.taotao.cloud.web.interceptor.PrometheusMetricsInterceptor;
import com.taotao.cloud.web.properties.FilterProperties;
import com.taotao.cloud.web.properties.InterceptorProperties;
import com.taotao.cloud.web.properties.XssProperties;
import com.taotao.cloud.web.sensitive.desensitize.DesensitizeProperties;
import com.taotao.cloud.web.validation.converter.IntegerToEnumConverterFactory;
import com.taotao.cloud.web.validation.converter.String2DateConverter;
import com.taotao.cloud.web.validation.converter.String2LocalDateConverter;
import com.taotao.cloud.web.validation.converter.String2LocalDateTimeConverter;
import com.taotao.cloud.web.validation.converter.String2LocalTimeConverter;
import com.taotao.cloud.web.validation.converter.StringToEnumConverterFactory;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import okhttp3.OkHttpClient;
import org.hibernate.validator.HibernateValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 自定义mvc配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:30:20
 */
@Configuration
@EnableConfigurationProperties({
	FilterProperties.class,
	InterceptorProperties.class,
	DesensitizeProperties.class
})
public class WebMvcConfiguration implements WebMvcConfigurer, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(WebMvcConfiguration.class, StarterName.WEB_STARTER);
	}

	/**
	 * redisRepository
	 */
	@Autowired
	private RedisRepository redisRepository;
	/**
	 * filterProperties
	 */
	@Autowired
	private FilterProperties filterProperties;
	@Autowired
	private InterceptorProperties interceptorProperties;
	/**
	 * xssProperties
	 */
	@Autowired
	private XssProperties xssProperties;
	/**
	 * requestCounter
	 */
	@Autowired
	private Counter requestCounter;
	/**
	 * requestLatency
	 */
	@Autowired
	private Summary requestLatency;
	/**
	 * inprogressRequests
	 */
	@Autowired
	private Gauge inprogressRequests;
	/**
	 * requestLatencyHistogram
	 */
	@Autowired
	private Histogram requestLatencyHistogram;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new LoginUserArgumentResolver());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (interceptorProperties.getHeader()) {
			registry.addInterceptor(new HeaderThreadLocalInterceptor())
				.addPathPatterns("/**");
		}

		if (interceptorProperties.getPrometheus()) {
			registry.addInterceptor(new PrometheusMetricsInterceptor(
					requestCounter,
					requestLatency,
					inprogressRequests,
					requestLatencyHistogram))
				.addPathPatterns("/**");
		}

		if (interceptorProperties.getDoubtApi()) {
			registry.addInterceptor(new DoubtApiInterceptor(interceptorProperties))
				.addPathPatterns("/**")
				.excludePathPatterns("/actuator/**");
		}

	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverterFactory(new IntegerToEnumConverterFactory());
		registry.addConverterFactory(new StringToEnumConverterFactory());

		registry.addConverter(new String2DateConverter());
		registry.addConverter(new String2LocalDateConverter());
		registry.addConverter(new String2LocalDateTimeConverter());
		registry.addConverter(new String2LocalTimeConverter());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/**")
			.addResourceLocations("classpath:/static/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry
			.addViewController("/index")
			.setViewName("index");
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {

	}

	@Bean
	@LoadBalanced
	public OkHttpClient.Builder builder() {
		return new OkHttpClient.Builder();
	}

//	@Bean
//	@ConditionalOnBean(value = {RedisRepository.class})
//	public RequestMappingScanListener resourceAnnotationScan() {
//		RequestMappingScanListener scan = new RequestMappingScanListener(redisRepository);
//		LogUtil.info("资源扫描类.[{}]", scan);
//		return scan;
//	}

	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
			.configure()
			// 快速失败模式
			.failFast(true)
			.buildValidatorFactory();
		return validatorFactory.getValidator();
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Bean
	@ConditionalOnProperty(prefix = FilterProperties.PREFIX, name = "version", havingValue = "true")
	public FilterRegistrationBean<VersionFilter> lbIsolationFilter() {
		FilterRegistrationBean<VersionFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new VersionFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName(VersionFilter.class.getName());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 6);
		return registrationBean;
	}

	@Bean
	@ConditionalOnProperty(prefix = FilterProperties.PREFIX, name = "tenant", havingValue = "true")
	public FilterRegistrationBean<TenantFilter> tenantFilter() {
		FilterRegistrationBean<TenantFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new TenantFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName(TenantFilter.class.getName());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 5);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<TraceFilter> traceFilter() {
		FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new TraceFilter(filterProperties));
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName(TraceFilter.class.getName());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 4);
		return registrationBean;
	}

	@Bean
	@ConditionalOnProperty(prefix = FilterProperties.PREFIX, name = "webContext", havingValue = "true")
	public FilterRegistrationBean<WebContextFilter> webContextFilter() {
		FilterRegistrationBean<WebContextFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new WebContextFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName(WebContextFilter.class.getName());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 3);
		return registrationBean;
	}


	///**
	// * 配置跨站攻击过滤器
	// */
	//@Bean
	//@ConditionalOnProperty(prefix = XssProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
	//public FilterRegistrationBean<XssFilter> filterRegistrationBean() {
	//	FilterRegistrationBean<XssFilter> filterRegistration = new FilterRegistrationBean<>();
	//	filterRegistration.setFilter(new XssFilter());
	//	filterRegistration.setEnabled(xssProperties.getEnabled());
	//	filterRegistration.addUrlPatterns(xssProperties.getPatterns().toArray(new String[0]));
	//	filterRegistration.setOrder(xssProperties.getOrder());
	//
	//	Map<String, String> initParameters = new HashMap<>(4);
	//	initParameters.put(IGNORE_PATH, CollUtil.join(xssProperties.getIgnorePaths(), ","));
	//	initParameters.put(IGNORE_PARAM_VALUE,
	//		CollUtil.join(xssProperties.getIgnoreParamValues(), ","));
	//	filterRegistration.setInitParameters(initParameters);
	//	return filterRegistration;
	//}

	/**
	 * 请求资源扫描监听器
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 21:31:34
	 */
	public static class RequestMappingScanListener implements
		ApplicationListener<ApplicationReadyEvent> {

		/**
		 * PATH_MATCH
		 */
		private static final AntPathMatcher PATH_MATCH = new AntPathMatcher();
		/**
		 * ignoreApi
		 */
		private final Set<String> ignoreApi = new HashSet<>();
		/**
		 * redisRepository
		 */
		private final RedisRepository redisRepository;

		public RequestMappingScanListener(RedisRepository redisRepository) {
			this.redisRepository = redisRepository;
			this.ignoreApi.add("/error");
			this.ignoreApi.add("/swagger-resources/**");
			this.ignoreApi.add("/v2/api-docs-ext/**");
		}

		@Override
		public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
			try {
				ConfigurableApplicationContext applicationContext = event.getApplicationContext();
				Environment env = applicationContext.getEnvironment();

				// 获取微服务模块名称
				String microService = env.getProperty("spring.application.name", "application");
				if (redisRepository == null || applicationContext
					.containsBean("resourceServerConfiguration")) {
					LogUtil.warn("[{}]忽略接口资源扫描", microService);
					return;
				}

				// 所有接口映射
				RequestMappingHandlerMapping mapping = applicationContext
					.getBean(RequestMappingHandlerMapping.class);

				// 获取url与类和方法的对应信息
				Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
				List<Map<String, String>> list = new ArrayList<>();
				for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
					RequestMappingInfo info = m.getKey();
					HandlerMethod method = m.getValue();

					Operation methodAnnotation = method.getMethodAnnotation(Operation.class);
					if (methodAnnotation != null) {
						if (methodAnnotation.hidden()) {
							// 忽略的接口不扫描
							continue;
						}
					}

					// 请求路径
					PatternsRequestCondition p = info.getPatternsCondition();
					String urls = "";
					if (Objects.nonNull(p)) {
						urls = getUrls(p.getPatterns());
						if (isIgnore(urls)) {
							continue;
						}
					}

					Set<MediaType> mediaTypeSet = info.getProducesCondition()
						.getProducibleMediaTypes();
					for (MethodParameter params : method.getMethodParameters()) {
						if (params.hasParameterAnnotation(RequestBody.class)) {
							mediaTypeSet.add(MediaType.APPLICATION_JSON);
							break;
						}
					}

					String mediaTypes = getMediaTypes(mediaTypeSet);
					// 请求类型
					RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
					String methods = getMethods(methodsCondition.getMethods());
					Map<String, String> api = Maps.newHashMap();

					// 类名
					String className = method.getMethod().getDeclaringClass().getName();

					// 方法名
					String methodName = method.getMethod().getName();
					String fullName = className + "." + methodName;

					// md5码
					String md5 = DigestUtils.md5DigestAsHex((microService + urls).getBytes());
					String summary = "";
					String description = "";
					String auth = "0";

					if (methodAnnotation != null) {
						summary = methodAnnotation.summary();
						description = methodAnnotation.description();
					}

					// 判断是否需要权限校验
					//PreAuth preAuth = method.getMethodAnnotation(PreAuth.class);
					//if (preAuth != null) {
					//	auth = "1";
					//}

					summary = StrUtil.isBlank(summary) ? methodName : summary;
					api.put("summary", summary);
					api.put("description", description);
					api.put("path", urls);
					api.put("code", md5);
					api.put("className", className);
					api.put("methodName", methodName);
					api.put("method", methods);
					api.put("serviceId", microService);
					api.put("contentType", mediaTypes);
					api.put("auth", auth);
					list.add(api);
				}

				// 放入redis缓存
				Map<String, Object> res = Maps.newHashMap();
				res.put("serviceId", microService);
				res.put("size", list.size());
				res.put("list", list);

				redisRepository.setExpire(
					CommonConstant.TAOTAO_CLOUD_API_RESOURCE,
					res,
					CommonConstant.TAOTAO_CLOUD_RESOURCE_EXPIRE);
				redisRepository.setExpire(
					CommonConstant.TAOTAO_CLOUD_SERVICE_RESOURCE,
					microService,
					CommonConstant.TAOTAO_CLOUD_RESOURCE_EXPIRE);

				LogUtil.info("资源扫描结果:serviceId=[{}] size=[{}] redis缓存key=[{}]",
					microService,
					list.size(),
					CommonConstant.TAOTAO_CLOUD_API_RESOURCE);
			} catch (Exception e) {
				LogUtil.error("error: {}", e.getMessage());
			}
		}

		/**
		 * getUrls
		 *
		 * @param urls urls
		 * @return {@link java.lang.String }
		 * @author shuigedeng
		 * @since 2021-09-02 21:32:06
		 */
		private String getUrls(Set<String> urls) {
			StringBuilder stringBuilder = new StringBuilder();
			for (String url : urls) {
				stringBuilder.append(url).append(",");
			}
			if (urls.size() > 0) {
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			}
			return stringBuilder.toString();
		}

		/**
		 * 是否是忽略的Api
		 *
		 * @param requestPath 请求地址
		 * @return boolean
		 * @author shuigedeng
		 * @since 2021-09-02 21:32:13
		 */
		private boolean isIgnore(String requestPath) {
			for (String path : ignoreApi) {
				if (PATH_MATCH.match(path, requestPath)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 获取媒体类型
		 *
		 * @param mediaTypes 类型SET集
		 * @return {@link java.lang.String }
		 * @author shuigedeng
		 * @since 2021-09-02 21:32:23
		 */
		private String getMediaTypes(Set<MediaType> mediaTypes) {
			StringBuilder stringBuilder = new StringBuilder();
			for (MediaType mediaType : mediaTypes) {
				stringBuilder.append(mediaType.toString()).append(",");
			}
			if (mediaTypes.size() > 0) {
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			}
			return stringBuilder.toString();
		}

		/**
		 * 获取方法
		 *
		 * @param requestMethods 请求方法
		 * @return {@link java.lang.String }
		 * @author shuigedeng
		 * @since 2021-09-02 21:32:30
		 */
		private String getMethods(Set<RequestMethod> requestMethods) {
			StringBuilder stringBuilder = new StringBuilder();
			for (RequestMethod requestMethod : requestMethods) {
				stringBuilder.append(requestMethod.toString()).append(",");
			}
			if (requestMethods.size() > 0) {
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			}
			return stringBuilder.toString();
		}
	}


	/**
	 * 通过header里的token获取用户信息
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @see <a href="https://my.oschina.net/u/4149877/blog/3143391/print">https://my.oschina.net/u/4149877/blog/3143391/print</a>
	 * @see <a href="https://blog.csdn.net/aiyaya_/article/details/79221733">https://blog.csdn.net/aiyaya_/article/details/79221733</a>
	 * @since 2021-09-02 21:32:45
	 */
	public static class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

		public LoginUserArgumentResolver() {
		}

		@Override
		public boolean supportsParameter(MethodParameter parameter) {
			boolean isHasEnableUserAnn = parameter.hasParameterAnnotation(EnableUser.class);
			boolean isHasLoginUserParameter = parameter.getParameterType()
				.isAssignableFrom(SecurityUser.class);
			return isHasEnableUserAnn && isHasLoginUserParameter;
		}

		@Override
		public Object resolveArgument(MethodParameter methodParameter,
			ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest,
			WebDataBinderFactory webDataBinderFactory) throws Exception {
			EnableUser user = methodParameter.getParameterAnnotation(EnableUser.class);
			boolean value = user.value();
			HttpServletRequest request = nativeWebRequest.getNativeRequest(
				HttpServletRequest.class);
			SecurityUser loginUser = SecurityUtil.getUser();

			//根据value状态获取更多用户信息，待实现
			return loginUser;
		}
	}
}
