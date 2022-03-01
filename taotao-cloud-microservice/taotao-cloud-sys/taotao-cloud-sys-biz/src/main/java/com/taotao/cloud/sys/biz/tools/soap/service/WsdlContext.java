package com.taotao.cloud.sys.biz.tools.soap.service;

import com.ibm.wsdl.extensions.schema.SchemaImpl;
import com.taotao.cloud.sys.biz.tools.soap.dtos.WsdlType;
import javax.xml.bind.annotation.XmlSchemaType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.commons.schema.*;
import org.w3c.dom.Element;

import javax.wsdl.Definition;
import javax.wsdl.Types;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * 功能: wsdl 上下文环境,对于每一个 WsdlService 会有一个 <br/>
 * 思路:读取 wsdl 前面的 types 节点,把类型存入 typesMap 中,把节点存入参数映射 paramMap 
 */
public class WsdlContext {
	private URL wsdlURL ;
//	private String postMessageUrl;
	private String targetNamespace;
	private Definition definition;
	private Map<String, WsdlType> typesMap = new HashMap<String, WsdlType>();
	private Map<String, WsdlParam> paramMap = new HashMap<String, WsdlParam>();
//	private final static Log log = LogFactory.getLog(WsdlContext.class);
//	private final static String [] BASE_TYPES = {"string","decimal","integer","int","float","long","boolean","time","date","datetime","array","anyType"};
	
	public static String BASE_SOAP11_DOCUMENT;		//soap11 的模板字符串
	public static String BASE_SOAP12_DOCUMENT;		//soap12 的模板字符串
	
	static{
		//读取 wsdl 模板文件
		final ClassLoader classLoader = WsdlContext.class.getClassLoader();
		try(final InputStream soap11Stream = classLoader.getResourceAsStream("soap/soap11.xml");
			final InputStream soap12Stream = classLoader.getResourceAsStream("soap/soap12.xml")){

			BASE_SOAP11_DOCUMENT = IOUtils.toString(soap11Stream, StandardCharsets.UTF_8);
			BASE_SOAP12_DOCUMENT = IOUtils.toString(soap12Stream, StandardCharsets.UTF_8);
		} catch (IOException e) {
			log.info("init load soap xml error : {}",e.getMessage(),e);
		}
	}
	/**
	 * 
	 * 功能:解析所有 wsdl 类型 <br/>
	 */
	@SuppressWarnings("unchecked")
	public void processTypes(){
		//先加入基本类型
//		for (String baseTypeName : BASE_TYPES) {
//			WsdlType wsdlType = new WsdlType();
//			wsdlType.setSimple(true);
//			wsdlType.setChildParams(null);
//			wsdlType.setTypeName(baseTypeName);
//			typesMap.put(baseTypeName, wsdlType);
//		}
		
		//然后解析定义的类型
		Types types = definition.getTypes();
		XmlSchemaCollection xmlSchemaCollection = new XmlSchemaCollection();
		List<SchemaImpl> schemaImpls = types.getExtensibilityElements();
		for (SchemaImpl schemaImpl : schemaImpls) {
			//遍历所有的 schema ,并一个一个解析
			Element element = schemaImpl.getElement();
			XmlSchema xmlSchema = xmlSchemaCollection.read(element);
			parserXmlSchemaType( xmlSchema);		//处理完类型之后,再处理参数 
			parserXmlSchemaParam(xmlSchema);		//处理参数类型
		}
	}

	/**
	 * 
	 * 功能:递归解析参数映射 ,获取参数类型<br/>
	 */
	@SuppressWarnings("unchecked")
	private void parserXmlSchemaParam(XmlSchema xmlSchema) {
		XmlSchemaObjectCollection includes = xmlSchema.getIncludes();
		Iterator<XmlSchemaImport> importSchemas = includes.getIterator();
		while(importSchemas.hasNext()){
			XmlSchemaImport xmlSchemaImport = importSchemas.next();
			XmlSchema childXmlSchema = xmlSchemaImport.getSchema();
			parserXmlSchemaParam(childXmlSchema);
		}
		//处理所有的参数,参数的类型有可能不是 parserXmlSchemaType 解析出来的参数类型,而是做为一个子元素包含在参数体里面
		XmlSchemaObjectTable elements = xmlSchema.getElements();		//getElements 不能获取所有
		Iterator<XmlSchemaElement> values = elements.getValues();
		while(values.hasNext()){
			XmlSchemaElement xmlSchemaElement = values.next();
			XmlSchemaType schemaType = xmlSchemaElement.getSchemaType();
			String schemaTypeName = schemaType.getName();
			
			String paramName = xmlSchemaElement.getName();
			if("getStationAndTimeByTrainCode".equals(paramName)){
				System.out.println();
			}
			long minOccurs = xmlSchemaElement.getMinOccurs();
			long maxOccurs = xmlSchemaElement.getMaxOccurs();
			String defaultValue = xmlSchemaElement.getDefaultValue();
			String fixedValue = xmlSchemaElement.getFixedValue();
			
			//设置基本参数,以后要加其它参数需要这加 TODO 
			WsdlParam wsdlParam = new WsdlParam();
			wsdlParam.setParamName(paramName);
			wsdlParam.setMaxOccurs(maxOccurs);
			wsdlParam.setMinOccurs(minOccurs);
			wsdlParam.setDefaultVal(defaultValue);
			wsdlParam.setFixed(fixedValue);
			
			if(schemaType instanceof XmlSchemaSimpleType){
				//解决有些 wsdl 需要设置简单类型为非空造成错误,也加入参数映射 add by sanri at 2017/06/23
				XmlSchemaSimpleType xmlSchemaSimpleType = (XmlSchemaSimpleType) schemaType;
				String simpleTypeName = xmlSchemaSimpleType.getName();
				WsdlType paramType = new WsdlType();
				paramType.setSimple(true);
				paramType.setTypeName(simpleTypeName);
				wsdlParam.setParamType(paramType);
				paramMap.put(paramName, wsdlParam);
				continue;
			}

			if(StringUtils.isBlank(schemaTypeName)){
				//内部有复杂类型,这里只是一个名称而已
				WsdlType wsdlType = new WsdlType();			//新建类型,名称取自参数名称
				parserXmlSchemaType(schemaType, wsdlType);		//解析内部包含的类型
				wsdlType.setTypeName(paramName);
				typesMap.put(paramName, wsdlType);
				schemaTypeName = paramName;			//schemaTypeName 取自 paramName
			}
			//否则就是直接引用类型,这里与包含类型统一了
			WsdlType paramType = typesMap.get(schemaTypeName);
			wsdlParam.setParamType(paramType);
			//对于直接包含类型和引用类型,都会包含重复的参数,所以这里去掉一级
			//对参数进行去重操作，因为内部节点的话会造成两个重复节点 ,去重会存在问题,对于内部参数超过两个的不适用,不能在这里去重
			paramMap.put(paramName, wsdlParam);
//			WsdlType parentWsdlType = wsdlParam.getParamType();
//			List<WsdlParam> childParams = parentWsdlType.getChildParams();
//			if(childParams != null && childParams.size() > 0){
//				//当内部参数超过 2 个时不要去重还有点问题
//				if(childParams.size() <= 1){
//					WsdlParam realWsdlParam = childParams.get(0);
//					paramMap.put(paramName, realWsdlParam);
//				}else{
//					paramMap.put(paramName, wsdlParam);
//				}
//			}
		}
	}

	/**
	 * 
	 * 功能:递归解析类型映射 ,可能有导入类型<br/>
	 */
	@SuppressWarnings({ "unchecked"})
	private void parserXmlSchemaType(XmlSchema xmlSchema) {
		XmlSchemaObjectCollection includes = xmlSchema.getIncludes();
		Iterator<XmlSchemaImport> importSchemas = includes.getIterator();
		while(importSchemas.hasNext()){
			XmlSchemaImport xmlSchemaImport = importSchemas.next();
			XmlSchema childXmlSchema = xmlSchemaImport.getSchema();
			parserXmlSchemaType(childXmlSchema);
		}
		//先解析出所有的类型
		XmlSchemaObjectTable smlSchemaObjectTable = xmlSchema.getSchemaTypes();			//这个是用于获取所有的类型的
		Iterator<XmlSchemaType> typesIterator = smlSchemaObjectTable.getValues();
		while(typesIterator.hasNext()){
			XmlSchemaType xmlSchemaType = typesIterator.next();
			WsdlType wsdlType = new WsdlType();			//新类型
			parserXmlSchemaType(xmlSchemaType, wsdlType);
			//加入类型　map 
			typesMap.put(wsdlType.getTypeName(), wsdlType);
		}
	}

	/**
	 * 
	 * 功能:解析参数类型,这里是独产出来的复杂类型 <br/>
	 */
	@SuppressWarnings("rawtypes")
	private void parserXmlSchemaType(XmlSchemaType xmlSchemaType, WsdlType wsdlType) {
		if(xmlSchemaType instanceof XmlSchemaComplexType){
			XmlSchemaComplexType complexType= (XmlSchemaComplexType) xmlSchemaType;
			wsdlType.setSimple(false);				//是复杂类型
			String complexTypeName = complexType.getName();
			if(StringUtils.isNotBlank(complexTypeName)){
				wsdlType.setTypeName(complexTypeName);
			}
			XmlSchemaParticle particle = complexType.getParticle();
			if(particle instanceof XmlSchemaSequence){
				// 参数是按照顺序排列的,大部分是这种情况,也有例外 
				XmlSchemaSequence sequence = (XmlSchemaSequence) particle;
				XmlSchemaObjectCollection items = sequence.getItems();
				Iterator iterator = items.getIterator();
				while(iterator.hasNext()){
					//对于复杂类型的每一个属性的处理,挂载到父级类型
					WsdlParam wsdlParam = new WsdlParam();
					WsdlType childWsdlType = new WsdlType();
					wsdlParam.setParamType(childWsdlType);
					wsdlType.addChildParam(wsdlParam);
					//解析子类型
					Object nextChildElement = iterator.next();
					if(nextChildElement instanceof XmlSchemaElement ){
						XmlSchemaElement childXmlSchemaElement = (XmlSchemaElement)nextChildElement ;
						String paramName = childXmlSchemaElement.getName();
						long minOccurs = childXmlSchemaElement.getMinOccurs();
						long maxOccurs = childXmlSchemaElement.getMaxOccurs();
						String defaultValue = childXmlSchemaElement.getDefaultValue();
						String fixedValue = childXmlSchemaElement.getFixedValue();
						//设置子类型参数的一些信息,以后再需要其它的信息可以从这里加 TODO 
						wsdlParam.setParamName(paramName);
						wsdlParam.setMinOccurs(minOccurs);
						wsdlParam.setMaxOccurs(maxOccurs);
						wsdlParam.setDefaultVal(defaultValue);
						wsdlParam.setFixed(fixedValue);
						if(maxOccurs == Long.MAX_VALUE){			//当最大值没有绑定,wsdl4j 取的是 long 的最大值,所以是数组类型
							wsdlParam.setArray(true);
						}
						
						XmlSchemaType childSchemaType = childXmlSchemaElement.getSchemaType();
						parserXmlSchemaType(childSchemaType, childWsdlType);		//递归解析子类型
					}else if(nextChildElement instanceof XmlSchemaAny) {
						XmlSchemaAny xmlSchemaAny = (XmlSchemaAny) nextChildElement;
						log.debug("any 元素:"+xmlSchemaAny);
					}else{
						log.debug("其它元素");
					}
				}
			}else{
				log.debug("其它类型,对于空的复杂类型也会进这里");
			}
		}else if(xmlSchemaType instanceof XmlSchemaSimpleType){
			XmlSchemaSimpleType xmlSchemaSimpleType = (XmlSchemaSimpleType) xmlSchemaType;
			String typeName = xmlSchemaSimpleType.getName();
			wsdlType.setSimple(true);
			wsdlType.setChildParams(null);
			wsdlType.setTypeName(typeName);
		}
	}
	
	/**
	 * 
	 * 功能:获取参数 <br/>
	 */
	public WsdlParam getParam(String paramName){
		return paramMap.get(paramName);
	}
	
	public URL getWsdlURL() {
		return wsdlURL;
	}
	public void setWsdlURL(URL wsdlURL) {
		this.wsdlURL = wsdlURL;
	}
	public String getTargetNamespace() {
		return targetNamespace;
	}
	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}
	public Definition getDefinition() {
		return definition;
	}
	public void setDefinition(Definition definition) {
		this.definition = definition;
	}
	public Map<String, WsdlType> getTypesMap() {
		return typesMap;
	}
	public void setTypesMap(Map<String, WsdlType> typesMap) {
		this.typesMap = typesMap;
	}


}
