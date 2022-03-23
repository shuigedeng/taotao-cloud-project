package com.taotao.cloud.common.support.cache.impl;


import com.taotao.cloud.common.support.cache.ICache;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.collection.MapUtil;
import com.taotao.cloud.common.utils.guava.Guavas;
import com.taotao.cloud.common.utils.reflect.PropertyDescriptorUtil;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 属性描述符列表缓存
 */
public class PropertyDescriptorListCache implements ICache<Class, List<PropertyDescriptor>> {

	/**
	 * 单例
	 */
	private static final PropertyDescriptorListCache INSTANCE = new PropertyDescriptorListCache();

	/**
	 * map 信息
	 */
	private static final Map<Class, List<PropertyDescriptor>> MAP = new ConcurrentHashMap<>();

	/**
	 * READ Method map key: 类型 value: 对应的 map ----------------------------- value.key: 属性名称
	 * value.value: 属性对应的读方法
	 */
	private static final Map<Class, Map<String, Method>> READ_METHOD_MAP = new ConcurrentHashMap<>();

	/**
	 * 获取单例
	 *
	 * @return 结果
	 */
	public static PropertyDescriptorListCache getInstance() {
		return INSTANCE;
	}

	@Override
	public List<PropertyDescriptor> get(Class key) {
		List<PropertyDescriptor> fieldList = MAP.get(key);
		if (CollectionUtil.isNotEmpty(fieldList)) {
			return fieldList;
		}

		fieldList = PropertyDescriptorUtil.getAllPropertyDescriptorList(key);
		this.set(key, fieldList);
		return fieldList;
	}

	@Override
	public void set(Class key, List<PropertyDescriptor> value) {
		MAP.put(key, value);
	}

	/**
	 * 获取读取方法的 map
	 *
	 * @param beanClass 类型
	 * @return 方法列表
	 */
	public Map<String, Method> getReadMethodMap(final Class beanClass) {
		Map<String, Method> readMethodMap = READ_METHOD_MAP.get(beanClass);

		if (MapUtil.isNotEmpty(readMethodMap)) {
			return readMethodMap;
		}

		// 构建
		List<PropertyDescriptor> propertyDescriptors = PropertyDescriptorListCache.getInstance()
			.get(beanClass);
		readMethodMap = Guavas.newHashMap(propertyDescriptors.size());
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			readMethodMap.put(propertyDescriptor.getName(),
				propertyDescriptor.getReadMethod());
		}

		// cache
		READ_METHOD_MAP.put(beanClass, readMethodMap);
		return readMethodMap;
	}

}
