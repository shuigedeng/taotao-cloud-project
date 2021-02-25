package com.taotao.cloud.standalone.common.utils;

import com.alibaba.fastjson.JSONArray;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @Classname BeanUtil
 * @Description TODO
 * @Author Created by Lihaodong (alias:小东啊) im.lihaodong@gmail.com
 * @Date 2019-09-23 15:22
 * @Version 1.0
 */
public class BeanUtil {
	/**
	 * 利用反射将map集合封装成bean对象
	 *
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static <T> T mapToBean(Map<String, Object> map, Class<?> clazz) throws Exception {
		Object obj = clazz.newInstance();
		if (map != null && !map.isEmpty() && map.size() > 0) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String propertyName = entry.getKey();    // 属性名
				Object value = entry.getValue();        // 属性值
				String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
				Field field = getClassField(clazz, propertyName);    //获取和map的key匹配的属性名称
				if (field == null) {
					continue;
				}
				Class<?> fieldTypeClass = field.getType();
				value = convertValType(value, fieldTypeClass);
				try {
					clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
		return (T) obj;
	}

	/**
	 * 根据给定对象类匹配对象中的特定字段
	 *
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	private static Field getClassField(Class<?> clazz, String fieldName) {
		if (Object.class.getName().equals(clazz.getName())) {
			return null;
		}
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		Class<?> superClass = clazz.getSuperclass();    //如果该类还有父类，将父类对象中的字段也取出
		if (superClass != null) {                        //递归获取
			return getClassField(superClass, fieldName);
		}
		return null;
	}

	/**
	 * 将map的value值转为实体类中字段类型匹配的方法
	 *
	 * @param value
	 * @param fieldTypeClass
	 * @return
	 */
	private static Object convertValType(Object value, Class<?> fieldTypeClass) {
		Object retVal = null;

		if (Long.class.getName().equals(fieldTypeClass.getName())
			|| long.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Long.parseLong(value.toString());
		} else if (Integer.class.getName().equals(fieldTypeClass.getName())
			|| int.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Integer.parseInt(value.toString());
		} else if (Float.class.getName().equals(fieldTypeClass.getName())
			|| float.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Float.parseFloat(value.toString());
		} else if (Double.class.getName().equals(fieldTypeClass.getName())
			|| double.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Double.parseDouble(value.toString());
		} else {
			retVal = value;
		}
		return retVal;
	}


	public static void main(String[] args) {
		String a = "[{id=21, contestId=16, placeId=1, curriculumId=1, curriculumType=1, identificationNumber=1, startTime=2019-11-11, expectFinishTime=2019-11-11, state=1, createTime=2019-11-11, updateTime=null, delFlag=0, contestDetailPersonList=null, contestDetailEquipmentList=null, contestDetailMonitorList=null, curriculumName=null, placeName=null}]";

		JSONArray objects = JSONArray.parseArray(a);
		System.out.println(objects);
	}
}
