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

import static com.taotao.cloud.common.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Maps;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.json.JacksonModule;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.SecurityUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.web.annotation.EnableUser;
import com.taotao.cloud.web.filter.TenantFilter;
import com.taotao.cloud.web.filter.TraceFilter;
import com.taotao.cloud.web.filter.VersionFilter;
import com.taotao.cloud.web.filter.WebContextFilter;
import com.taotao.cloud.web.interceptor.HeaderThreadLocalInterceptor;
import com.taotao.cloud.web.interceptor.PrometheusMetricsInterceptor;
import com.taotao.cloud.web.mvc.converter.IntegerToEnumConverterFactory;
import com.taotao.cloud.web.mvc.converter.StringToEnumConverterFactory;
import com.taotao.cloud.web.properties.FilterProperties;
import com.taotao.cloud.web.properties.XssProperties;
import com.taotao.cloud.web.xss.XssStringJsonDeserializer;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.swagger.v3.oas.annotations.Operation;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 自定义mvc配置
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/29 14:30
 */
@AutoConfigureBefore({PrometheusConfiguration.class})
public class WebMvcConfiguration implements WebMvcConfigurer {

	private final RedisRepository redisRepository;
	private final FilterProperties filterProperties;
	private final XssProperties xssProperties;

	private final Counter requestCounter;
	private final Summary requestLatency;
	private final Gauge inprogressRequests;
	private final Histogram requestLatencyHistogram;

	public WebMvcConfiguration(RedisRepository redisRepository,
		FilterProperties filterProperties,
		XssProperties xssProperties, Counter requestCounter,
		Summary requestLatency, Gauge inprogressRequests,
		Histogram requestLatencyHistogram) {
		this.redisRepository = redisRepository;
		this.filterProperties = filterProperties;
		this.xssProperties = xssProperties;

		this.requestCounter = requestCounter;
		this.requestLatency = requestLatency;
		this.inprogressRequests = inprogressRequests;
		this.requestLatencyHistogram = requestLatencyHistogram;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new LoginUserArgumentResolver());
	}

//	@Bean
//	public PrometheusMetricsInterceptor prometheusMetricsInterceptor() {
//		return new PrometheusMetricsInterceptor();
//	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HeaderThreadLocalInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(
			new PrometheusMetricsInterceptor(requestCounter, requestLatency, inprogressRequests,
				requestLatencyHistogram)).addPathPatterns("/**");
	}

	@Override
	public void configureMessageConverters(
		List<HttpMessageConverter<?>> converters) {


		WebMvcConfigurer.super.configureMessageConverters(converters);
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverterFactory(new IntegerToEnumConverterFactory());
		registry.addConverterFactory(new StringToEnumConverterFactory());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**").
			addResourceLocations("classpath:/imgs/",
				"classpath:/mystatic/",
				"classpath:/static/",
				"classpath:/public/",
				"classpath:/META-INF/resources",
				"classpath:/resources");
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		WebMvcConfigurer.super.configureAsyncSupport(configurer);
	}

//	@Bean
//	@ConditionalOnBean(value = {RedisRepository.class})
//	public RequestMappingScanListener resourceAnnotationScan() {
//		RequestMappingScanListener scan = new RequestMappingScanListener(redisRepository);
//		LogUtil.info("资源扫描类.[{}]", scan);
//		return scan;
//	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return customizer -> {
			ObjectMapper objectMapper = customizer.createXmlMapper(true).build();
			objectMapper
				.setLocale(Locale.CHINA)
				//去掉默认的时间戳格式
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				// 时区
				.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
				//Date参数日期格式
				.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT, Locale.CHINA))
				// 包含null
				.setSerializationInclusion(Include.ALWAYS)
				//该特性决定parser是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。 如果该属性关闭，则如果遇到这些字符，则会抛出异常。JSON标准说明书要求所有控制符必须使用引号，因此这是一个非标准的特性
				.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
				// 忽略不能转义的字符
				.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(),
					true)
				//在使用spring boot + jpa/hibernate，如果实体字段上加有FetchType.LAZY，并使用jackson序列化为json串时，会遇到SerializationFeature.FAIL_ON_EMPTY_BEANS异常
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
				//忽略未知字段
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				//DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES相当于配置，JSON串含有未知字段时，反序列化依旧可以成功
				//单引号处理
				.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

			// 注册自定义模块
			objectMapper.registerModule(new JacksonModule()).findAndRegisterModules();

			customizer.configure(objectMapper);

			/**
			 * 配置跨站攻击 反序列化处理器
			 */
			if (xssProperties.getRequestBodyEnabled()) {
				customizer.deserializerByType(String.class, new XssStringJsonDeserializer());
			}
		};
	}

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
	public FilterRegistrationBean<VersionFilter> lbIsolationFilterFilterRegistrationBean() {
		FilterRegistrationBean<VersionFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new VersionFilter(filterProperties));
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName(VersionFilter.class.getName());
		registrationBean.setOrder(1);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<TenantFilter> tenantFilterFilterRegistrationBean() {
		FilterRegistrationBean<TenantFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new TenantFilter(filterProperties));
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName(TenantFilter.class.getName());
		registrationBean.setOrder(2);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<TraceFilter> traceFilterFilterRegistrationBean() {
		FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new TraceFilter(filterProperties));
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName(TraceFilter.class.getName());
		registrationBean.setOrder(3);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<WebContextFilter> webContextFilterFilterRegistrationBean() {
		FilterRegistrationBean<WebContextFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new WebContextFilter(filterProperties));
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName(WebContextFilter.class.getName());
		registrationBean.setOrder(4);
		return registrationBean;
	}

//	/**
//	 * 配置跨站攻击过滤器
//	 */
//	@Bean
//	@ConditionalOnProperty(prefix = XssProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
//	public FilterRegistrationBean<XssFilter> filterRegistrationBean() {
//		FilterRegistrationBean<XssFilter> filterRegistration = new FilterRegistrationBean<>();
//		filterRegistration.setFilter(new XssFilter());
//		filterRegistration.setEnabled(xssProperties.getEnabled());
//		filterRegistration.addUrlPatterns(xssProperties.getPatterns().toArray(new String[0]));
//		filterRegistration.setOrder(xssProperties.getOrder());
//
//		Map<String, String> initParameters = new HashMap<>(4);
//		initParameters.put(IGNORE_PATH, CollUtil.join(xssProperties.getIgnorePaths(), ","));
//		initParameters.put(IGNORE_PARAM_VALUE,
//			CollUtil.join(xssProperties.getIgnoreParamValues(), ","));
//		filterRegistration.setInitParameters(initParameters);
//		return filterRegistration;
//	}


	/**
	 * 请求资源扫描监听器
	 *
	 * @author shuigedeng
	 */
	public static class RequestMappingScanListener implements
		ApplicationListener<ApplicationReadyEvent> {

		private static final AntPathMatcher PATH_MATCH = new AntPathMatcher();
		private final Set<String> ignoreApi = new HashSet<>();
		private final RedisRepository redisRepository;


		public RequestMappingScanListener(RedisRepository redisRepository) {
			this.redisRepository = redisRepository;
			this.ignoreApi.add("/error");
			this.ignoreApi.add("/swagger-resources/**");
			this.ignoreApi.add("/v2/api-docs-ext/**");
		}

		/**
		 * 默认事件
		 *
		 * @param event ApplicationReadyEvent
		 */
		@Override
		public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
			try {
				ConfigurableApplicationContext applicationContext = event.getApplicationContext();
				Environment env = applicationContext.getEnvironment();
				// 获取微服务模块名称
				String microService = env.getProperty("spring.application.name", "application");
				if (redisRepository == null || applicationContext
					.containsBean("resourceServerConfiguration")) {
					LogUtil.warn("[{0}]忽略接口资源扫描", microService);
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
							mediaTypeSet.add(MediaType.APPLICATION_JSON_UTF8);
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
//				PreAuth preAuth = method.getMethodAnnotation(PreAuth.class);
//				if (preAuth != null) {
//					auth = "1";
//				}

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
		 * @return String
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
		 * @return String
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
	 * @link https://my.oschina.net/u/4149877/blog/3143391/print
	 * @link https://blog.csdn.net/aiyaya_/article/details/79221733
	 */
	public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

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

			/**
			 * 根据value状态获取更多用户信息，待实现
			 */
			return loginUser;
		}
	}

}
