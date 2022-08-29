package com.taotao.cloud.encrypt.handler;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.encrypt.annotation.SeparateEncrypt;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterConfig;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 初始化处理器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:10:07
 */
public class InitHandler {

	/**
	 * 处理程序
	 *
	 * @param filterConfig        过滤器配置
	 * @param encryptCacheUri     加密缓存uri
	 * @param isEncryptAnnotation 是加密注释
	 * @since 2022-07-06 15:10:08
	 */
	public static void handler(FilterConfig filterConfig, Set<String> encryptCacheUri, AtomicBoolean isEncryptAnnotation) {
		WebApplicationContext servletContext = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
		Map<String, Object> restControllers = new HashMap<>();
		Map<String, Object> controllers = new HashMap<>();
		try {
			controllers = servletContext.getBeansWithAnnotation(Controller.class);
		} catch (BeanCreationException e) {
			LogUtils.error(e.getMessage());
		}
		try {
			restControllers = servletContext.getBeansWithAnnotation(RestController.class);
		} catch (BeanCreationException e) {
			LogUtils.error(e.getMessage());
		}

		if (restControllers.size() > 0) {
			List<Object> types = restControllers.values().stream().filter(v -> AnnotationUtils.findAnnotation(v.getClass(), SeparateEncrypt.class ) != null).collect(Collectors.toList());
			List<Object> notTypes = restControllers.values().stream().filter(v -> AnnotationUtils.findAnnotation(v.getClass(), SeparateEncrypt.class ) == null).collect(Collectors.toList());
			restcontrollerTypesHandler(types, encryptCacheUri);
			restcontrollerNotTypesHandler(notTypes, encryptCacheUri);
		}
		if (controllers.size() > 0) {
			List<Object> types = controllers.values().stream().filter(v -> AnnotationUtils.findAnnotation(v.getClass(), SeparateEncrypt.class ) != null).collect(Collectors.toList());
			List<Object> notTypes = controllers.values().stream().filter(v -> AnnotationUtils.findAnnotation(v.getClass(), SeparateEncrypt.class ) == null).collect(Collectors.toList());
			controllerTypesHandler(types, encryptCacheUri);
			controllerNotTypesHandler(notTypes, encryptCacheUri);
		}

		if (encryptCacheUri.size() > 0) {
			isEncryptAnnotation.set(true);
		}
	}

	/**
	 * 控制器不类型处理程序
	 *
	 * @param types    类型
	 * @param cacheUrl url缓存
	 * @since 2022-07-06 15:10:08
	 */
	private static void controllerNotTypesHandler(List<Object> types, Set<String> cacheUrl) {
		if (types.size() > 0) {
			types.stream().forEach(t -> {
				Class<?> aClass = t.getClass();
				Method[] declaredMethods = aClass.getDeclaredMethods();
				String[] finalTypeUrl = typeUrl(aClass);
				List<Method> methods = Arrays.stream(declaredMethods).filter(d -> AnnotationUtils.findAnnotation(d, SeparateEncrypt.class) != null).collect(Collectors.toList());
				if (methods.size() == 0) {
					return;
				}
				MethodHandler(methods, finalTypeUrl, cacheUrl);
			});
		}
	}

	/**
	 * 控制器类型处理程序
	 *
	 * @param types    类型
	 * @param cacheUrl url缓存
	 * @since 2022-07-06 15:10:08
	 */
	private static void controllerTypesHandler(List<Object> types, Set<String> cacheUrl) {
		if (types.size() > 0) {
			types.stream().forEach(t -> {
				Class<?> aClass = t.getClass();
				Method[] declaredMethods = aClass.getDeclaredMethods();
				String[] finalTypeUrl = typeUrl(aClass);
				if (declaredMethods.length == 0) {
					return;
				}
				List<Method> methods = Arrays.asList(declaredMethods);
				MethodHandler(methods, finalTypeUrl, cacheUrl);
			});
		}
	}


	/**
	 * restcontroller不类型处理程序
	 *
	 * @param types    类型
	 * @param cacheUrl url缓存
	 * @since 2022-07-06 15:10:08
	 */
	private static void restcontrollerNotTypesHandler(List<Object> types, Set<String> cacheUrl) {
		if (types.size() > 0) {
			types.stream().forEach(t -> {
				Class<?> aClass = t.getClass();
				Method[] declaredMethods = aClass.getDeclaredMethods();
				String[] finalTypeUrl = typeUrl(aClass);
				List<Method> methods = Arrays.stream(declaredMethods).filter(d -> AnnotationUtils.findAnnotation(d, SeparateEncrypt.class) != null).collect(Collectors.toList());
				if (methods.size() == 0) {
					return;
				}
				restMethodHandler(methods, finalTypeUrl, cacheUrl);
			});
		}
	}

	/**
	 * restcontroller类型处理程序
	 *
	 * @param types    类型
	 * @param cacheUrl url缓存
	 * @since 2022-07-06 15:10:08
	 */
	private static void restcontrollerTypesHandler(List<Object> types, Set<String> cacheUrl) {
		if (types.size() > 0) {
			types.stream().forEach(t -> {
				Class<?> aClass = t.getClass();
				Method[] declaredMethods = aClass.getDeclaredMethods();
				String[] finalTypeUrl = typeUrl(aClass);
				if (declaredMethods.length == 0) {
					return;
				}
				List<Method> methods = Arrays.asList(declaredMethods);
				restMethodHandler(methods, finalTypeUrl, cacheUrl);
			});
		}
	}

	/**
	 * 类型url
	 *
	 * @param aClass 一个类
	 * @return {@link String[] }
	 * @since 2022-07-06 15:10:08
	 */
	private static String[] typeUrl(Class<?> aClass) {
		String[] typeUrl = null;
		if (AnnotationUtils.findAnnotation(aClass, RequestMapping.class) != null) {
			typeUrl = AnnotationUtils.findAnnotation(aClass, RequestMapping.class).value();
		}
		return typeUrl;
	}

	/**
	 * 其他方法处理程序
	 *
	 * @param methods      方法
	 * @param finalTypeUrl 最后输入url
	 * @param cacheUrl     url缓存
	 * @since 2022-07-06 15:10:08
	 */
	private static void restMethodHandler(List<Method> methods, String[] finalTypeUrl, Set<String> cacheUrl) {
		methods.forEach(m -> {
			if (AnnotationUtils.findAnnotation(m, PostMapping.class) != null || (AnnotationUtils.findAnnotation(m, RequestMapping.class) != null
					&& Arrays.stream(AnnotationUtils.findAnnotation(m, RequestMapping.class).method()).allMatch(r -> !r.equals(RequestMethod.GET)))) {
				urlHandler(m, finalTypeUrl, cacheUrl);
			}
		});
	}

	/**
	 * 方法处理程序
	 *
	 * @param methods      方法
	 * @param finalTypeUrl 最后输入url
	 * @param cacheUrl     url缓存
	 * @since 2022-07-06 15:10:09
	 */
	private static void MethodHandler(List<Method> methods, String[] finalTypeUrl, Set<String> cacheUrl) {
		methods.forEach(m -> {
			if ((AnnotationUtils.findAnnotation(m, PostMapping.class) != null && AnnotationUtils.findAnnotation(m, ResponseBody.class) != null) || (AnnotationUtils.findAnnotation(m, RequestMapping.class) != null
					&& Arrays.stream(AnnotationUtils.findAnnotation(m, RequestMapping.class).method()).allMatch(r -> !r.equals(RequestMethod.GET)) && AnnotationUtils.findAnnotation(m, ResponseBody.class) != null)) {
				urlHandler(m, finalTypeUrl, cacheUrl);
			}
		});
	}

	/**
	 * url处理程序
	 *
	 * @param m            米
	 * @param finalTypeUrl 最后输入url
	 * @param cacheUrl     url缓存
	 * @since 2022-07-06 15:10:09
	 */
	private static void urlHandler(Method m, String[] finalTypeUrl, Set<String> cacheUrl) {
		String[] urls = null;
		if (AnnotationUtils.findAnnotation(m, PostMapping.class) != null) {
			urls = AnnotationUtils.findAnnotation(m, PostMapping.class).value();
		} else {
			urls = AnnotationUtils.findAnnotation(m, RequestMapping.class).value();
		}
		if (urls != null) {
			Arrays.stream(urls).forEach(u -> {
				if (!u.startsWith("/")) {
					u = "/".concat(u);
				}
				if (finalTypeUrl != null && finalTypeUrl.length > 0) {
					String finalU = u;
					Arrays.stream(finalTypeUrl).forEach(f -> {
						if (!f.startsWith("/")) {
							f = "/".concat(f);
						}
						String uri = f.concat(finalU).replaceAll("//+", "/");
						if (uri.endsWith("/")) {
							uri = uri.substring(0, uri.length() - 1);
						}
						cacheUrl.add(uri);
					});
				} else {
					String uri = u.replaceAll("//+", "/");
					if (uri.endsWith("/")) {
						uri = uri.substring(0, uri.length() - 1);
					}
					cacheUrl.add(uri);
				}
			});
		}
	}
}
