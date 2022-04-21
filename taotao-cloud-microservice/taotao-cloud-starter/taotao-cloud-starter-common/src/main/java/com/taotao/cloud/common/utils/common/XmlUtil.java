///*
// * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.common.utils;
//
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.xml.DomDriver;
//import com.thoughtworks.xstream.mapper.MapperWrapper;
//import com.thoughtworks.xstream.security.AnyTypePermission;
//import java.io.File;
//import java.nio.charset.StandardCharsets;
//
///**
// * Xml 工具类
// *
// * @author shuigedeng
// * @version 2021.9
// * @since 2021-09-02 14:33:51
// */
//public class XmlUtil {
//
//	private XmlUtil() {
//	}
//
//	/**
//	 * xml tag
//	 */
//	private static final String XML_TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
//
//	/**
//	 * 单例方法
//	 *
//	 * @return {@link XStream }
//	 * @author shuigedeng
//	 * @since 2021-09-02 14:34:22
//	 */
//	private static XStream getInstance() {
//		XStream xStream = new XStream(new DomDriver(StandardCharsets.UTF_8.name())) {
//			/**
//			 * 忽略xml中多余字段
//			 */
//			@Override
//			protected MapperWrapper wrapMapper(MapperWrapper next) {
//				return new MapperWrapper(next) {
//					@Override
//					public boolean shouldSerializeMember(Class definedIn, String fieldName) {
//						if (definedIn == Object.class) {
//							return false;
//						}
//						return super.shouldSerializeMember(definedIn, fieldName);
//					}
//				};
//			}
//		};
//		//设置默认的安全校验
//		XStream.setupDefaultSecurity(xStream);
//		//使用本地的类加载器
//		xStream.setClassLoader(XmlUtil.class.getClassLoader());
//		//允许所有的类进行转换
//		xStream.addPermission(AnyTypePermission.ANY);
//		return xStream;
//	}
//
//	/**
//	 * 将xml字符串转化为java对象
//	 *
//	 * @param xml   xml字符串
//	 * @param clazz 对象类型
//	 * @param <T>   T
//	 * @return T
//	 * @author shuigedeng
//	 * @since 2021-09-02 14:35:22
//	 */
//	public static <T> T xml2Obj(String xml, Class<T> clazz) {
//		return xml2Obj(xml, clazz, clazz.getSimpleName());
//	}
//
//	/**
//	 * 将xml字符串转化为java对象
//	 *
//	 * @param xml         xml字符串
//	 * @param clazz       对象类型
//	 * @param rootElement 根节点名称
//	 * @param <T>         T
//	 * @return T
//	 * @author shuigedeng
//	 * @since 2021-09-02 14:35:36
//	 */
//	public static <T> T xml2Obj(String xml, Class<T> clazz, String rootElement) {
//		XStream xStream = getInstance();
//		xStream.processAnnotations(clazz);
//		xStream.alias(rootElement, clazz);
//		xStream.ignoreUnknownElements();
//		Object object = xStream.fromXML(xml);
//		return clazz.cast(object);
//	}
//
//	/**
//	 * 将xml文件转化为java对象
//	 *
//	 * @param xml   xml文件
//	 * @param clazz 对象类型
//	 * @param <T>   T
//	 * @return T
//	 * @author shuigedeng
//	 * @since 2021-09-02 14:35:46
//	 */
//	public static <T> T xml2Obj(File xml, Class<T> clazz) {
//		return xml2Obj(xml, clazz, clazz.getSimpleName());
//	}
//
//	/**
//	 * 将xml文件转化为java对象
//	 *
//	 * @param xml         xml文件
//	 * @param clazz       对象类型
//	 * @param rootElement 根节点名称
//	 * @param <T>         T
//	 * @return T
//	 * @author shuigedeng
//	 * @since 2021-09-02 14:35:55
//	 */
//	public static <T> T xml2Obj(File xml, Class<T> clazz, String rootElement) {
//		XStream xStream = getInstance();
//		xStream.processAnnotations(clazz);
//		xStream.alias(rootElement, clazz);
//		xStream.ignoreUnknownElements();
//		Object object = xStream.fromXML(xml);
//		return clazz.cast(object);
//	}
//
//	/**
//	 * 将java对象转化为xml字符串（包含xml头部信息）
//	 *
//	 * @param object java对象
//	 * @return {@link String }
//	 * @author shuigedeng
//	 * @since 2021-09-02 14:36:05
//	 */
//	public static String obj2XmlWithTag(Object object) {
//		return obj2XmlWithTag(object, object.getClass().getSimpleName());
//	}
//
//	/**
//	 * 将java对象转化为xml字符串（包含xml头部信息）
//	 *
//	 * @param object      java对象
//	 * @param rootElement 根节点
//	 * @return {@link String }
//	 * @author shuigedeng
//	 * @since 2021-09-02 14:36:13
//	 */
//	public static String obj2XmlWithTag(Object object, String rootElement) {
//		return XML_TAG + "\n" + obj2Xml(object, rootElement);
//	}
//
//	/**
//	 * 将java对象转化为xml字符串
//	 *
//	 * @param object java对象
//	 * @return {@link String }
//	 * @author shuigedeng
//	 * @since 2021-09-02 14:36:41
//	 */
//	public static String obj2Xml(Object object) {
//		return obj2Xml(object, object.getClass().getSimpleName());
//	}
//
//	/**
//	 * 将java对象转化为xml字符串
//	 *
//	 * @param object      java对象
//	 * @param rootElement 根节点
//	 * @return {@link String }
//	 * @author shuigedeng
//	 * @since 2021-09-02 14:36:53
//	 */
//	public static String obj2Xml(Object object, String rootElement) {
//		XStream xStream = getInstance();
//		xStream.alias(rootElement, object.getClass());
//		xStream.processAnnotations(object.getClass());
//		xStream.ignoreUnknownElements();
//		return xStream.toXML(object);
//	}
//}
