package com.taotao.cloud.sys.biz.tools.core.utils;

import com.taotao.cloud.common.utils.LogUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;


/**
 * 
 * 功能:属性工具类 <br/>
 */
public final class PropertyEditUtil {
	
	private static final PrimitiveHandler DEFAULT_PRIMITIVE_HANDLER;
	
	static{
		DEFAULT_PRIMITIVE_HANDLER = new PrimitiveHandler() {

			@Override
			public Object handler(String name, Object dest, Object source) {
				// source 一定不为空,肯定要空判断
				Class<? extends Object> clazz = source.getClass();
				String sourceValue_  = Objects.toString(source,"0");
				if(clazz == int.class){
					int sourceValue = NumberUtils.toInt(sourceValue_);
					if(sourceValue == 0 ){
						return dest;
					}
				}else if(clazz == long.class){
					long sourceValue = NumberUtils.toLong(sourceValue_);
					if(sourceValue == 0 ){
						return dest;
					}
				}else if(clazz == double.class){
					double sourceValue = NumberUtils.toDouble(sourceValue_);
					if(sourceValue == 0 ){
						return dest;
					}
				}else if(clazz == float.class){
					float sourceValue = NumberUtils.toFloat(sourceValue_);
					if(sourceValue == 0 ){
						return dest;
					}
				}
				
				return source;
			}
			
		};
	}
	
	
	/**
	 * 
	 * 功能:获取简单属性,这里使用 apache.commons 的 <br/>
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Object getSimpleProperty(Object source,String name){
		try {
			return PropertyUtils.getSimpleProperty(source, name);
		} catch (Exception e) {
			LogUtil.error("getSimpleProperty() error : {}",e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 
	 * 功能:获取重叠结构的属性数据 eg: 对象是由对象聚合而成,则有聚合属性 a.b ;  使用 apache.commons<br/>
	 * @param source 
	 * @param name
	 * @return
	 */
	public static Object getNestedProperty(Object source,String name){
		try {
			return PropertyUtils.getNestedProperty(source, name);
		} catch (Exception e) {
			LogUtil.error("getNestedProperty() error : {}",e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 
	 * 功能:获取指定类型的简单属性 <br/>
	 * @param clazz
	 * @param name
	 * @param valueClass
	 * @return
	 */
	public static <T> T getSimpleProperty(Object source,String name,Class<T> valueClass){
		Object simpleProperty = getSimpleProperty(source, name);
		if(simpleProperty == null) {
            return null;
        }
		
		//类型匹配才做转换
		if(simpleProperty.getClass() == valueClass){
			return valueClass.cast(simpleProperty);
		}
		return null;
	}
	
	/**
	 * 
	 * 功能:获取字符串属性(常用) <br/>
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static String getStringProperty(Object source,String name){
		return getSimpleProperty(source, name,String.class);
	}
	
	/**
	 * 
	 * 功能:获取所有属性名 <br/>
	 * @param source
	 * @return
	 */
	public static List<String> propertyNames(Object source){
		List<String> names = new ArrayList<String>();
		PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(source);
		if(ArrayUtils.isNotEmpty(propertyDescriptors)){
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				String name = propertyDescriptor.getName();
				names.add(name);
			}
		}
		
		return names;
	}
	
	/**
	 * 
	 * 功能:描述一个bean,排除部分字段 <br/>
	 * @param source
	 * @param excludes
	 * @return
	 */
	public static Map<String,Object> describeExclude(Object source,String... excludes){
		PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(source);
		Map<String,Object> describe = new HashMap<String,Object>();
		if(ArrayUtils.isNotEmpty(propertyDescriptors)){
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				String name = propertyDescriptor.getName();
				if(ArrayUtils.contains(excludes, name)){
					continue;
				}
				
				Method readMethod = propertyDescriptor.getReadMethod();
				if(readMethod == null){continue;}
				
				try {
					Object propertyValue = readMethod.invoke(source);
					describe.put(name, propertyValue);
				} catch (Exception e) {
					LogUtil.error("describeExclude() error : {}",e.getMessage(),e);
				}
				
			}
		}
		return describe;
	}
	
	/**
	 * 
	 * 功能:描述一个 bean ,指定字段 <br/>
	 * @param source
	 * @param includes
	 * @return
	 */
	public static Map<String,Object> describeInclude(Object source,String... includes){
		List<String> propertyNames = propertyNames(source);
		List<String> excludes = new ArrayList<String>();
		for (String propertyName : propertyNames) {
			if(!ArrayUtils.contains(includes, propertyName)){
				excludes.add(propertyName);
			}
		}
		
		return describeExclude(source, excludes.toArray(new String []{}));
	}
	
	/**
	 * 
	 * 功能:复制字段到目标类字段; 类型不区分,缺失字段忽略; 不管是否为空直接复制   <br/>
	 * @param dest
	 * @param source
	 * @param excludes 不能复制的字段列表
	 */
	public static void copyExclude(Object dest,Object source,String... excludes){
		Map<String, PropertyDescriptor> destPropertyDescriptorMap = propertyDescriptorMap(dest, excludes);
		Map<String, PropertyDescriptor> sourcePropertyDescriptorMap = propertyDescriptorMap(source, excludes);
		
		if(!destPropertyDescriptorMap.isEmpty()){
			Iterator<Entry<String, PropertyDescriptor>> iterator = destPropertyDescriptorMap.entrySet().iterator();
			try {
				while(iterator.hasNext()){
					Entry<String, PropertyDescriptor> entry = iterator.next();
					String key = entry.getKey();
					
					PropertyDescriptor destPropertyDescriptor = entry.getValue();
					PropertyDescriptor sourcePropertyDescriptor = sourcePropertyDescriptorMap.get(key);
					// 源数据列中没有这个字段
					if(sourcePropertyDescriptor == null){continue;}
					
					if(sourcePropertyDescriptor != null && destPropertyDescriptor != null 
							&& sourcePropertyDescriptor.getPropertyType() == destPropertyDescriptor.getPropertyType()){
						Method readMethod = sourcePropertyDescriptor.getReadMethod();
						Method writeMethod = destPropertyDescriptor.getWriteMethod();
						
						if(readMethod != null && writeMethod != null){
							Object invoke = readMethod.invoke(source);
							writeMethod.invoke(dest, invoke);
						}
					}else if(sourcePropertyDescriptor.getPropertyType() != destPropertyDescriptor.getPropertyType()){
						LogUtil.warn("属性类型不匹配,不能复制:"+sourcePropertyDescriptor.getName());
					}
				}
			} catch (Exception e) {
				LogUtil.error("复制属性失败dest:{}",e.getMessage(),e);
			}
		}
	}
	
	/**
	 * 
	 * 功能:复制指定字段到目标<br/>
	 * @param dest
	 * @param source
	 * @param includes
	 */
	public static void copyInclude(Object dest,Object source,String...includes){
		List<String> propertyNames = propertyNames(source);
		List<String> excludes = new ArrayList<String>();
		for (String propertyName : propertyNames) {
			if(!ArrayUtils.contains(includes, propertyName)){
				excludes.add(propertyName);
			}
		}
		
		copyExclude(dest, source, excludes.toArray(new String[]{}));
	}
	
	/**
	 * 
	 * 功能:复制不为空的字段到目标 <br/>
	 * @param dest
	 * @param source
	 * @param primitiveHandler
	 * @param excludes
	 */
	public static void copyNotNull(Object dest,Object source,PrimitiveHandler primitiveHandler,String...excludes){
		if(primitiveHandler == null){
			LogUtil.warn("原始类型处理没有定义,使用默认设置,int,long,float,double ,当发现源数据为 0 时,使用目标值");
			primitiveHandler = DEFAULT_PRIMITIVE_HANDLER;
		}
		Map<String, PropertyDescriptor> destPropertyDescriptorMap = propertyDescriptorMap(dest, excludes);
		Map<String, PropertyDescriptor> sourcePropertyDescriptorMap = propertyDescriptorMap(source, excludes);
		
		if(!destPropertyDescriptorMap.isEmpty()){
			Iterator<Entry<String, PropertyDescriptor>> iterator = destPropertyDescriptorMap.entrySet().iterator();
			try {
				while(iterator.hasNext()){
					Entry<String, PropertyDescriptor> entry = iterator.next();
					String key = entry.getKey();
					
					PropertyDescriptor destPropertyDescriptor = entry.getValue();
					PropertyDescriptor sourcePropertyDescriptor = sourcePropertyDescriptorMap.get(key);
					
					if(sourcePropertyDescriptor != null && destPropertyDescriptor != null 
							&& sourcePropertyDescriptor.getPropertyType() == destPropertyDescriptor.getPropertyType()){
						Method readMethod = sourcePropertyDescriptor.getReadMethod();
						Method writeMethod = destPropertyDescriptor.getWriteMethod();
						
						if(readMethod != null && writeMethod != null){
							Object invoke = readMethod.invoke(source);
							if(invoke != null ){
								Class<? extends Object> valueClass = invoke.getClass();
								if(valueClass.isPrimitive()){
									invoke = primitiveHandler.handler(key, invoke, source);
								}
								writeMethod.invoke(dest, invoke);
							}
						}
					}
				}
			} catch (Exception e) {
				LogUtil.error("复制属性失败:{}",e.getMessage(),e);
			}
		}
	}
	
	/**
	 * 
	 * 功能:复制不为空的字段到目标,使用默认原始类型处理 <br/>
	 * @param dest
	 * @param source
	 * @param excludes
	 */
	public static void copyNotNull(Object dest,Object source,String...excludes){
		copyNotNull(dest, source, DEFAULT_PRIMITIVE_HANDLER, excludes);
	}
	
	/**
	 * 
	 * 功能:bean 填入 map 数据 <br/>
	 */
	public static void populateMapData(Object dest,Map data,String ...excludes){
		Map<String, PropertyDescriptor> propertyDescriptorMap = propertyDescriptorMap(dest,excludes);
		if(!propertyDescriptorMap.isEmpty()){
			Iterator<Entry<String, PropertyDescriptor>> iterator = propertyDescriptorMap.entrySet().iterator();
			try {
				while(iterator.hasNext()){
					Entry<String, PropertyDescriptor> entry = iterator.next();
					String key = entry.getKey();
					PropertyDescriptor propertyDescriptor = entry.getValue();
					
					Object object = data.get(key);
					Method writeMethod = propertyDescriptor.getWriteMethod();
	
					if(object != null && writeMethod != null){
						Class<?> propertyType = propertyDescriptor.getPropertyType();
						if(propertyType == object.getClass()){
							//属性类型匹配才可以写入,这个写入会抛出异常; 只要有一个属性写入失败,则整体失败
							writeMethod.invoke(dest, object);
							
						}
					}
					
				}
			} catch (Exception e) {
				LogUtil.error("属性值注入失败,数据为:{}",e.getMessage(),e);
			}
		}
	}
	
	
	/**
	 * 
	 * 功能:获取 map 结构的属性描述器 <br/>
	 * @param source
	 * @return
	 */
	private static Map<String,PropertyDescriptor> propertyDescriptorMap(Object source,String ...excludes){
		Map<String,PropertyDescriptor> descriptorMap = new HashMap<String, PropertyDescriptor>();

		PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(source);
		if(ArrayUtils.isNotEmpty(propertyDescriptors)){
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				String name = propertyDescriptor.getName();
				if(ArrayUtils.contains(excludes, name)){continue;}
				
				descriptorMap.put(name, propertyDescriptor);
			}
		}
		return descriptorMap;
	}
	
	/**
	 * 
	 * 功能:用于复制非空数据时,原始型数据处理<br/>
	 */
	public interface PrimitiveHandler{
		Object handler(String name, Object dest, Object source);
	}


}
