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

package com.taotao.cloud.common.utils.common;

import com.taotao.cloud.common.utils.exception.ExceptionUtil;
import com.taotao.cloud.common.utils.io.IoUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.io.InputStream;
import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.springframework.lang.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * xpath解析xml
 *
 * <pre>
 *     文档地址：
 *     http://www.w3school.com.cn/xpath/index.asp
 * </pre>
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class XmlHelper {

	private final XPath path;
	private final Document doc;

	private XmlHelper(InputSource inputSource, boolean unsafe) throws Exception {
		DocumentBuilderFactory dbf = unsafe ?
			XmlHelper.getUnsafeDocumentBuilderFactory() :
			XmlHelper.getDocumentBuilderFactory();
		DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
		doc = documentBuilder.parse(inputSource);
		path = XmlHelper.getXpathFactory().newXPath();
	}

	private static XmlHelper createSafe(InputSource inputSource) {
		return create(inputSource, false);
	}

	private static XmlHelper createUnsafe(InputSource inputSource) {
		return create(inputSource, true);
	}

	private static XmlHelper create(InputSource inputSource, boolean unsafe) {
		try {
			return new XmlHelper(inputSource, unsafe);
		} catch (Exception e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 构造 XmlHelper，安全模式
	 *
	 * @param is InputStream
	 * @return XmlHelper
	 */
	public static XmlHelper safe(InputStream is) {
		InputSource inputSource = new InputSource(is);
		return createSafe(inputSource);
	}

	/**
	 * 构造 XmlHelper，安全模式
	 *
	 * @param xmlStr xml字符串
	 * @return XmlHelper
	 */
	public static XmlHelper safe(String xmlStr) {
		StringReader sr = new StringReader(xmlStr.trim());
		InputSource inputSource = new InputSource(sr);
		XmlHelper xmlHelper = XmlHelper.createSafe(inputSource);
		IoUtil.closeQuietly(sr);
		return xmlHelper;
	}

	/**
	 * 构造 XmlHelper，非安全模式
	 *
	 * @param is InputStream
	 * @return XmlHelper
	 */
	public static XmlHelper unsafe(InputStream is) {
		InputSource inputSource = new InputSource(is);
		return createUnsafe(inputSource);
	}

	/**
	 * 构造 XmlHelper，非安全模式
	 *
	 * @param xmlStr xml字符串
	 * @return XmlHelper
	 */
	public static XmlHelper unsafe(String xmlStr) {
		StringReader sr = new StringReader(xmlStr.trim());
		InputSource inputSource = new InputSource(sr);
		XmlHelper xmlHelper = XmlHelper.createUnsafe(inputSource);
		IoUtil.closeQuietly(sr);
		return xmlHelper;
	}

	/**
	 * 执行 xpath 语法
	 *
	 * @param expression xpath 语法
	 * @param item       子节点
	 * @param returnType 返回的类型
	 * @return {Object}
	 */
	public Object evalXPath(String expression, @Nullable Object item, QName returnType) {
		item = null == item ? doc : item;
		try {
			return path.evaluate(expression, item, returnType);
		} catch (XPathExpressionException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 获取String
	 *
	 * @param expression 路径
	 * @return String
	 */
	public String getString(String expression) {
		return (String) evalXPath(expression, null, XPathConstants.STRING);
	}

	/**
	 * 获取Boolean
	 *
	 * @param expression 路径
	 * @return String
	 */
	public Boolean getBoolean(String expression) {
		return (Boolean) evalXPath(expression, null, XPathConstants.BOOLEAN);
	}

	/**
	 * 获取Number
	 *
	 * @param expression 路径
	 * @return {Number}
	 */
	public Number getNumber(String expression) {
		return (Number) evalXPath(expression, null, XPathConstants.NUMBER);
	}

	/**
	 * 获取某个节点
	 *
	 * @param expression 路径
	 * @return {Node}
	 */
	public Node getNode(String expression) {
		return (Node) evalXPath(expression, null, XPathConstants.NODE);
	}

	/**
	 * 获取子节点
	 *
	 * @param expression 路径
	 * @return NodeList
	 */
	public NodeList getNodeList(String expression) {
		return (NodeList) evalXPath(expression, null, XPathConstants.NODESET);
	}

	/**
	 * 获取String
	 *
	 * @param node       节点
	 * @param expression 相对于node的路径
	 * @return String
	 */
	public String getString(Object node, String expression) {
		return (String) evalXPath(expression, node, XPathConstants.STRING);
	}

	/**
	 * 获取
	 *
	 * @param node       节点
	 * @param expression 相对于node的路径
	 * @return String
	 */
	public Boolean getBoolean(Object node, String expression) {
		return (Boolean) evalXPath(expression, node, XPathConstants.BOOLEAN);
	}

	/**
	 * 获取
	 *
	 * @param node       节点
	 * @param expression 相对于node的路径
	 * @return {Number}
	 */
	public Number getNumber(Object node, String expression) {
		return (Number) evalXPath(expression, node, XPathConstants.NUMBER);
	}

	/**
	 * 获取某个节点
	 *
	 * @param node       节点
	 * @param expression 路径
	 * @return {Node}
	 */
	public Node getNode(Object node, String expression) {
		return (Node) evalXPath(expression, node, XPathConstants.NODE);
	}

	/**
	 * 获取子节点
	 *
	 * @param node       节点
	 * @param expression 相对于node的路径
	 * @return NodeList
	 */
	public NodeList getNodeList(Object node, String expression) {
		return (NodeList) evalXPath(expression, node, XPathConstants.NODESET);
	}

	private static DocumentBuilderFactory getDocumentBuilderFactory() {
		return XmlHelperHolder.DOCUMENT_BUILDER_FACTORY;
	}

	/**
	 * 不安全的 Document 构造器，用来解析部分可靠的 html、xml
	 *
	 * @return DocumentBuilderFactory
	 */
	private static DocumentBuilderFactory getUnsafeDocumentBuilderFactory() {
		return XmlHelperHolder.UNSAFE_DOCUMENT_BUILDER_FACTORY;
	}

	private static XPathFactory getXpathFactory() {
		return XmlHelperHolder.XPATH_FACTORY;
	}

	/**
	 * 内部类单例
	 */
	private static class XmlHelperHolder {

		private static final String FEATURE_HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
		private static final String FEATURE_HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
		private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = XmlHelperHolder.newDocumentBuilderFactory();
		private static final DocumentBuilderFactory UNSAFE_DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
		private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();

		private static DocumentBuilderFactory newDocumentBuilderFactory() {
			DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			// Set parser features to prevent against XXE etc.
			// Note: setting only the external entity features on DocumentBuilderFactory instance
			// (ala how safeTransform does it for SAXTransformerFactory) does seem to work (was still
			// processing the entities - tried Oracle JDK 7 and 8 on OSX). Setting seems a bit extreme,
			// but looks like there's no other choice.
			df.setXIncludeAware(false);
			df.setExpandEntityReferences(false);
			df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
			setDocumentBuilderFactoryFeature(df, XMLConstants.FEATURE_SECURE_PROCESSING, true);
			setDocumentBuilderFactoryFeature(df,
				FEATURE_HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_GENERAL_ENTITIES, false);
			setDocumentBuilderFactoryFeature(df,
				FEATURE_HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_PARAMETER_ENTITIES, false);
			setDocumentBuilderFactoryFeature(df,
				"http://apache.org/xml/features/disallow-doctype-decl", true);
			setDocumentBuilderFactoryFeature(df,
				"http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			return df;
		}

		private static void setDocumentBuilderFactoryFeature(
			DocumentBuilderFactory documentBuilderFactory, String feature, boolean state) {
			try {
				documentBuilderFactory.setFeature(feature, state);
			} catch (Exception e) {
				LogUtil.warn(
					String.format("Failed to set the XML Document Builder factory feature %s to %s",
						feature, state), e);
			}
		}
	}

}
