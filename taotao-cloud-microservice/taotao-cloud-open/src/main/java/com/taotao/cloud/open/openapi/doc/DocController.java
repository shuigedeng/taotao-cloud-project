package com.taotao.cloud.open.openapi.doc;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.open.openapi.common.constant.Constant;
import com.taotao.cloud.open.openapi.config.OpenApiConfig;
import com.taotao.cloud.open.openapi.doc.annotation.OpenApiDoc;
import com.taotao.cloud.open.openapi.doc.model.Api;
import com.taotao.cloud.open.openapi.doc.model.Method;
import com.taotao.cloud.open.openapi.doc.model.Param;
import com.taotao.cloud.open.openapi.doc.model.Property;
import com.taotao.cloud.open.openapi.doc.model.RetVal;
import com.taotao.cloud.open.openapi.model.ApiHandler;
import com.taotao.cloud.open.openapi.model.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档控制器
 *
 * @author wanghuidong
 * 时间： 2022/6/21 20:10
 */
@RestController
public class DocController {

	@Autowired
	private Context context;

	@Autowired
	private OpenApiConfig config;

	/**
	 * 忽略添加属性的类型
	 */
	private static final List<Class> ignoreAddPropertyTypes = Arrays.asList(
		String.class,
		Collection.class,
		Map.class
	);

	/**
	 * 返回接口文档数据
	 *
	 * @return 接口文档数据
	 */
	@GetMapping(value = Constant.DOC_PATH, produces = {MediaType.APPLICATION_JSON_VALUE})
	public String doc() {
		//不启用接口文档功能，直接返回空的API列表
		if (!config.enableDoc()) {
			return new JSONArray().toString();
		}
		//启用接口文档功能，则统计出所有的API文档
		Map<String, Api> apiMap = new HashMap<>();
		for (ApiHandler apiHandler : context.getApiHandlers()) {
			String beanName = apiHandler.getBeanName();

			Api api = apiMap.get(beanName);
			if (api == null) {
				api = getApi(apiHandler);
				if (api == null) {
					continue;
				}
				apiMap.put(beanName, api);
			}
			Method method = getMethod(apiHandler);
			if (method != null) {
				api.getMethods().add(method);
			}
		}
		Collection<Api> apiList = apiMap.values();
		return JSONUtil.toJsonStr(apiList);
	}

	/**
	 * 获取API信息
	 *
	 * @param apiHandler openapi处理器
	 * @return API信息
	 */
	private Api getApi(ApiHandler apiHandler) {
		Api api = new Api();
		api.setOpenApiName(apiHandler.getOpenApiName());

		Class apiClass = apiHandler.getBean().getClass();
		api.setName(apiClass.getSimpleName());
		api.setFullName(apiClass.getName());

		if (apiClass.isAnnotationPresent(OpenApiDoc.class)) {
			OpenApiDoc apiDoc = (OpenApiDoc) apiClass.getAnnotation(OpenApiDoc.class);
			api.setCnName(apiDoc.cnName());
			api.setDescribe(apiDoc.describe());
			if (apiDoc.ignore()) {
				//取消文档的生成
				api = null;
			}
		}
		return api;
	}

	/**
	 * 获取方法信息
	 *
	 * @param apiHandler openapi处理器
	 * @return 方法信息
	 */
	private Method getMethod(ApiHandler apiHandler) {
		Method method = new Method();

		//设置方法基本信息
		method.setOpenApiMethodName(apiHandler.getOpenApiMethodName());
		java.lang.reflect.Method reflectMethod = apiHandler.getMethod();
		method.setName(reflectMethod.getName());
		if (reflectMethod.isAnnotationPresent(OpenApiDoc.class)) {
			OpenApiDoc apiDoc = reflectMethod.getAnnotation(OpenApiDoc.class);
			method.setCnName(apiDoc.cnName());
			method.setDescribe(apiDoc.describe());
			if (apiDoc.ignore()) {
				//取消文档的生成
				return null;
			}
		}

		//设置方法参数信息
		for (int i = 0; i < apiHandler.getParamTypes().length; i++) {
			Type type = apiHandler.getParamTypes()[i];
			Parameter parameter = apiHandler.getParameters()[i];
			Param param = getParam(type, parameter);
			method.getParams().add(param);
		}

		//设置方法返回值信息
		RetVal retVal = getRetVal(reflectMethod);
		method.setRetVal(retVal);

		return method;
	}

	/**
	 * 获取参数信息
	 *
	 * @param type      参数类型
	 * @param parameter 参数对象
	 * @return 参数信息
	 */
	private Param getParam(Type type, Parameter parameter) {
		Param param = new Param();
		param.setType(type.getTypeName());
		param.setName(parameter.getName());
		param.setProperties(getProperties(type));

		if (parameter.isAnnotationPresent(OpenApiDoc.class)) {
			OpenApiDoc apiDoc = parameter.getAnnotation(OpenApiDoc.class);
			param.setCnName(apiDoc.cnName());
			param.setDescribe(apiDoc.describe());
		}
		return param;
	}

	/**
	 * 获取返回值信息
	 *
	 * @param method 方法
	 * @return 返回值信息
	 */
	private RetVal getRetVal(java.lang.reflect.Method method) {
		Type type = method.getGenericReturnType();
		RetVal retVal = new RetVal();
		retVal.setRetType(type.getTypeName());
		retVal.setProperties(getProperties(type));

		if (method.isAnnotationPresent(OpenApiDoc.class)) {
			OpenApiDoc apiDoc = method.getAnnotation(OpenApiDoc.class);
			retVal.setCnName(apiDoc.retCnName());
			retVal.setDescribe(apiDoc.retDescribe());
		}
		return retVal;
	}

	/**
	 * 获取指定类型里的属性信息
	 *
	 * @param type 类型（包括Class,ParameterizedType,GenericArrayType,TypeVariable,WildcardType）
	 * @return 属性信息
	 */
	private List<Property> getProperties(Type type) {
		List<Property> properties = null;
		if (type instanceof Class) {
			//基本类型直接返回
			if (ClassUtil.isBasicType((Class) type)) {
				return null;
			}

			//枚举类型、接口类型等直接返回
			if (((Class<?>) type).isEnum() || ((Class<?>) type).isInterface() || ((Class<?>) type).isAnnotation()) {
				return null;
			}

			//忽略的类型(及其子类)直接返回
			for (Class ignoreType : ignoreAddPropertyTypes) {
				if (ignoreType.isAssignableFrom((Class) type)) {
					return null;
				}
			}

			//数组类型则获取元素的属性
			if (((Class) type).isArray()) {
				Class elementType = ((Class) type).getComponentType();
				return getProperties(elementType);
			}

			properties = new ArrayList<>();
			Field[] fields = ReflectUtil.getFields((Class) type);
			for (Field field : fields) {
				Property property = new Property();
				property.setName(field.getName());
				property.setType(field.getGenericType().getTypeName());
				//递归设置属性
				property.setProperties(getProperties(field.getGenericType()));

				if (field.isAnnotationPresent(OpenApiDoc.class)) {
					OpenApiDoc apiDoc = field.getAnnotation(OpenApiDoc.class);
					property.setCnName(apiDoc.cnName());
					property.setDescribe(apiDoc.describe());
					if (apiDoc.ignore()) {
						//取消文档的生成
						continue;
					}
				}

				properties.add(property);
			}
		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Class rawType = (Class) parameterizedType.getRawType();
			//集合泛型类（Collection/List/Set等）
			if (Collection.class.isAssignableFrom(rawType)) {
				//取第一个泛型参数(集合元素)的属性
				Type elementType = parameterizedType.getActualTypeArguments()[0];
				return getProperties(elementType);
			}
			//Map泛型类(HashMap、Hashtable、TreeMap等)
			if (Map.class.isAssignableFrom(rawType)) {
				properties = getMapProperties(parameterizedType);
			}
		} else if (type instanceof GenericArrayType) {
			//是泛型数组 or 类型参数数组
			Type elementType = ((GenericArrayType) type).getGenericComponentType();
			return getProperties(elementType);
		}
		return properties;
	}

	/**
	 * 获取Map<Key,Value>类型的属性，分别提取Key、Value中的属性
	 *
	 * @param parameterizedType 参数化类型
	 * @return 属性集合（[Key:{properties},Value:{properties}]）
	 */
	private List<Property> getMapProperties(ParameterizedType parameterizedType) {
		List<Property> properties;
		properties = new ArrayList<>();

		//取第一个泛型参数(key)的属性
		Type[] typeArgumentTypes = parameterizedType.getActualTypeArguments();
		Type keyType = typeArgumentTypes[0];
		Property property = new Property();
		property.setName("key");
		property.setType(keyType.getTypeName());
		property.setProperties(getProperties(keyType));
		Class keyClass = getClassByType(keyType);
		if (keyClass != null && keyClass.isAnnotationPresent(OpenApiDoc.class)) {
			OpenApiDoc apiDoc = (OpenApiDoc) keyClass.getAnnotation(OpenApiDoc.class);
			property.setCnName(apiDoc.cnName());
			property.setDescribe(apiDoc.describe());
		}
		properties.add(property);

		//取第二个泛型参数(value)的属性
		Type valueType = typeArgumentTypes[1];
		property = new Property();
		property.setName("value");
		property.setType(valueType.getTypeName());
		property.setProperties(getProperties(valueType));
		Class valueClass = getClassByType(valueType);
		if (valueClass != null && valueClass.isAnnotationPresent(OpenApiDoc.class)) {
			OpenApiDoc apiDoc = (OpenApiDoc) valueClass.getAnnotation(OpenApiDoc.class);
			property.setCnName(apiDoc.cnName());
			property.setDescribe(apiDoc.describe());
		}
		properties.add(property);
		return properties;
	}

	/**
	 * 获取类型的Class
	 *
	 * @param type 类型
	 * @return Class
	 */
	private Class getClassByType(Type type) {
		Class typeClass;
		if (type instanceof Class) {
			typeClass = (Class) type;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			typeClass = (Class) parameterizedType.getRawType();
		} else {
			return null;
		}
		return typeClass;
	}

}
