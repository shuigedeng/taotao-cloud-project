package com.taotao.cloud.sys.biz.tools.soap.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sanri.tools.modules.core.utils.MybatisXNode;
import com.sanri.tools.modules.core.utils.MybatisXPathParser;
import com.sanri.tools.modules.soap.dtos.WsdlParam;
import com.sanri.tools.modules.soap.dtos.WsdlType;
import com.sanri.tools.modules.soap.exception.WsdlCallException;
import com.sanri.tools.modules.soap.utils.DOMUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.wsdl.*;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-6-21上午11:25:05<br/>
 * 功能: webservice 方法 <br/> 
 */
@Slf4j
public class WsdlOperation {
	private String name;				//方法名称
	private String style;				//document/rpc
	private String use;					//literal/Encoded
	private String soapActionURI;		//对于 soap1.1 需要加在请求头中
	private WsdlPort.SOAPType soapType;
	private WsdlContext wsdlContext;
	private BindingOperation bindingOperation;
	private Operation operation;
	private WsdlParam input;
	private WsdlParam output;
	private String postMessageUrl;

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-22下午3:44:47<br/>
	 * 功能:调用方法 <br/>
	 * @param jsonObject 传入对应参数类型的结构,去掉一层复杂类型;例如:
	 * <xs:complexType name="getWeatherbyCityName">
	 *	<xs:sequence>
	 *	<xs:element name="theCityName" type="xs:string" minOccurs="0"/>
	 *	</xs:sequence>
	 * </xs:complexType>
	 * 此时只需要传入 jsonObject = new JSONObject();jsonObject.put("theCityName","深圳") 就好
	 */
	@SuppressWarnings("unchecked")
	public JSONObject invoke(JSONObject jsonObject) throws WsdlCallException{
		Document buildRequest = buildRequest(jsonObject);
		String message = DOMUtil.toStringFromDoc(buildRequest);
		try {
			String postSoapMessage = invoke(message);
			org.dom4j.Document parserDocument = DocumentHelper.parseText(postSoapMessage);
			org.dom4j.Element rootElement = parserDocument.getRootElement();
			Namespace namespace = rootElement.getNamespace();
			org.dom4j.QName bodyQName = new org.dom4j.QName("Body",namespace);
			org.dom4j.Element bodyElement = rootElement.element(bodyQName);
			
			JSONObject outputObject = new JSONObject();
			org.dom4j.Element parentElement = bodyElement;			//需要解析的父级节点,document 样式下就是在 body 的下一级,rpc 模式为直接就是 body 元素的上一级
			List<Element> elements = bodyElement.elements();
			org.dom4j.Element nextElement = (org.dom4j.Element) elements.get(0);
			switch (soapType) {
			case SOAP11:
			case SOAP12:
				if("document".equalsIgnoreCase(style)){
					
				}
				break;
			default:
				break;
			}
			//解析异常信息 (nextElement)
			String elementName = nextElement.getName();
			if("Fault".equals(elementName)){
				JSONObject buildExceptionJsonObject = buildExceptionJsonObject(nextElement);
				WsdlCallException wsdlCallException = new WsdlCallException();
				wsdlCallException.setExceptionJsonObject(buildExceptionJsonObject);
				wsdlCallException.setXmlString(nextElement.asXML());
				throw wsdlCallException;
			}
			buildReturnObject(parentElement,outputObject,this.output);
			return outputObject;
		} catch (IOException | DocumentException e ) {
			log.error("WsdlOperation invoke error : {}",e.getMessage(),e);
		}
		return null;
	}

	public static final ContentType XML_UTF8 = ContentType.APPLICATION_XML.withCharset(StandardCharsets.UTF_8);

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-22下午9:01:15<br/>
	 * 功能:发送 soap 消息串,得到响应结果 <br/>
	 * @param soapMessage xml字符串 soap 格式
	 * @return xml 字符串,soap 格式
	 * @throws IOException 
	 */
	public String invoke(String soapMessage) throws IOException{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(postMessageUrl);
		httpPost.addHeader("SOAPAction",soapActionURI);
		httpPost.addHeader("Content-Type", "text/xml; charset=utf-8");
		httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
		HttpEntity httpEntity = null;
		switch (soapType){
			case SOAP11:
				httpEntity = new StringEntity(soapMessage,XML_UTF8);
				break;
			case SOAP12:
				httpEntity = new StringEntity(soapMessage,ContentType.APPLICATION_SOAP_XML);
				break;
			default:
		}
		if (httpEntity == null){
			log.error("不支持的 soap 请求 {}",soapType);
			return  null;
		}

		httpPost.setEntity(httpEntity);
		log.info("调用 {} 的: {} 方法时发送的　soap 消息为:\n{}",postMessageUrl,this.getOperation().getName(),soapMessage);
		String postSoapMessage = httpClient.execute(httpPost, new BasicResponseHandler());
		log.info("调用 {} 的: {} 方法响应的　soap 消息为:\n{}",postMessageUrl,this.getOperation().getName(),postSoapMessage);
		return postSoapMessage;
	}

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-22下午7:25:35<br/>
	 * 功能:构建异常消息 <br/>
	 * @param nextElement
	 */
	private JSONObject buildExceptionJsonObject(org.dom4j.Element nextElement) {
		log.error("暂时未实现异常调用构建 ");
		return new JSONObject();
	}

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-22下午5:34:24<br/>
	 * 功能:构建返回值信息 <br/>
	 * @param parentElement 父级元素
	 * @param outputObject 输出对象
	 * @param paramInfo 出参信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void buildReturnObject(org.dom4j.Element parentNode, JSONObject outputObject, WsdlParam paramInfo) {
		String paramName = paramInfo.getParamName();
		WsdlType paramType = paramInfo.getParamType();
		if(paramInfo.isArray()){
			//如果是一个数组,如果简单元素,则做为数组添加,如果是复杂元素,需再次递归添加
			JSONArray jsonArray = new JSONArray();
			outputObject.put(paramName, jsonArray);
			List<org.dom4j.Element> elements = parentNode.elements(paramName);
			if(paramType.isSimple()){
				//简单元素直接循环添加
				for (org.dom4j.Element element : elements) {
					String textContent = element.getText();
					jsonArray.add(textContent);
				}
			}else{
				//复杂元素递归添加到 jsonArray
				for (org.dom4j.Element element : elements) {
					//对于每一个元素又是 jsonObject 
					JSONObject childJsonObject = new JSONObject();
					jsonArray.add(childJsonObject);
					
					List<WsdlParam> childParams = paramType.getChildParams();
					if(childParams != null && childParams.size() > 0){
						for (WsdlParam childParam : childParams) {
							String childParamName = childParam.getParamName();
							org.dom4j.Element childElement = element.element(childParamName);
							buildReturnObject(childElement, childJsonObject, childParam);
						}
					}
				}
			}
		}else{
			//非数组元素
			if(paramType.isSimple()){
				String textContent = parentNode.getText();
				//简单类型直接添加值
				outputObject.put(paramName, textContent);
			}else{
				//复杂对象类型递归添加值
				org.dom4j.Element childElement = parentNode.element(paramName);
				JSONObject childJsonObject = new JSONObject();
				outputObject.put(paramName, childJsonObject);
				List<WsdlParam> wsdlParams =  paramType.getChildParams();
				if(wsdlParams != null && wsdlParams.size() > 0){
					for (WsdlParam childParam : wsdlParams) {
						buildReturnObject(childElement, childJsonObject, childParam);
					}
				}
			}
		}
	}

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-21上午11:22:35<br/>
	 * 功能:构建消息模板 <br/>
	 * @return
	 */
	public String buildRequestTemplate(){
		Document buildRequest = buildRequest(null);
		return DOMUtil.toStringFromDoc(buildRequest);
	}
	
	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-21上午11:24:24<br/>
	 * 功能:内部使用,构建消息模板 <br/>
	 * 暂时只实现了 soap11 
	 * @return
	 */
	private Document buildRequest(JSONObject inputValue){
		switch (soapType) {
		case SOAP11:
			try {
				Document buildDocument = DOMUtil.parse(WsdlContext.BASE_SOAP11_DOCUMENT);
				NodeList bodyElements = buildDocument.getElementsByTagName("soap:Body");
				Element bodyNode = (Element) bodyElements.item(0);
				bodyNode.setAttribute("xmlns", this.wsdlContext.getTargetNamespace());		//设置命名空间
				if("document".equalsIgnoreCase(style)){
					JSONObject methodJsonObject = new JSONObject();			//多拼接一个方法元素
					methodJsonObject.put(this.name, inputValue);
					//先拼接方法名称
//					Element methodElement = DOMUtil.addElement(bodyNode, this.operation.getTitle());
//					methodElement.setAttribute("xmlns", this.wsdlContext.getTargetNamespace());		//设置命名空间
					if(this.input == null){
						//如果参数是空的,则可以直接返回了
						return buildDocument;
					}
					if("literal".equalsIgnoreCase(use)){
						//modify by sanri at 2017/06/23 对参数进行去重操作,使用参数类型来构建
						buildSoapMessage(bodyNode, this.input,methodJsonObject);
					}else if("Encoded".equalsIgnoreCase(use)){
						buildEncodedSoapMessage(bodyNode,this.input,methodJsonObject);
					}else{
						throw new IllegalArgumentException("不支持的 use 类型:"+use);
					}
				}else if("rpc".equalsIgnoreCase(style)){
					if("literal".equalsIgnoreCase(use)){
						
					}else if("Encoded".equalsIgnoreCase(use)){
						
					}else{
						throw new IllegalArgumentException("不支持的 use 类型:"+use);
					}
				}else{
					throw new IllegalArgumentException("不支持的 style 类型:"+style);
				}
				return buildDocument;
			} catch (SAXException | IOException e) {
				log.error("WsdlOperation buildRequest error : {}",e.getMessage(),e);
			}
			break;
		case SOAP12:
			break;
		case HTTP_GET:
			break;
		case HTTP_POST:
			break;
		default:
			throw new IllegalArgumentException("不支持的 soap 类型:"+soapType);
		}
		return null;
	}

//	/**
//	 * 
//	 * 作者:sanri <br/>
//	 * 时间:2017-6-23上午11:44:41<br/>
//	 * 功能: 递归构建 soap 消息,使用 wsdlType 来构建 <br/>
//	 * @param parentElement 父级元素
//	 * @param paramType 当前类型
//	 * @param inputValue 输入值
//	 */
//	private void buildSoapMessage(Element parentElement, WsdlType paramType, JSONObject inputValue) {
//		String typeName = paramType.getTypeName();
//		Element curentElement = DOMUtil.addElement(parentElement, typeName);
//		List<WsdlParam> childParams = paramType.getChildParams();
//		if(childParams != null && childParams.size() > 0){
//			for (WsdlParam wsdlParam : childParams) {
//				WsdlType childParamType = wsdlParam.getParamType();
//				buildSoapMessage(curentElement, childParamType, inputValue.getJSONObject(typeName));
//			}
//		}
//	}

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-21下午7:47:20<br/>
	 * 功能:递归构建 soap 消息 <br/>
	 * @param inputValue  输入值
	 * @param parentElement　 父级元素
	 * @param currentParam 当前参数
	 */
	private void buildSoapMessage(Element parentElement, WsdlParam currentParam, JSONObject inputValue) {
		String paramName = currentParam.getParamName();
		Element paramElement = DOMUtil.addElement(parentElement, paramName);
		WsdlType paramType = currentParam.getParamType();
		if(!paramType.isSimple()){
			List<WsdlParam> childParams = paramType.getChildParams();
			if(childParams != null && childParams.size() > 0){
				for (WsdlParam wsdlParam : childParams) {
					if(inputValue != null){
						buildSoapMessage(paramElement, wsdlParam,inputValue.getJSONObject(paramName));
					}else{
						buildSoapMessage(paramElement, wsdlParam,null);
					}
				}
			}
		}else{
			//简单类型则设置值 
			if(inputValue != null){
				paramElement.setTextContent(String.valueOf(inputValue.getString(paramName)));
			}else{
				//如果没有输入值，则构建为--元素名称--
				paramElement.setTextContent("--"+paramElement.getNodeName()+"--");
			}
		}
	}
	
	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-21下午7:49:36<br/>
	 * 功能:递归构建 Encoded 格式的 soap 消息  <br/>
	 * @param inputValue  输入值
	 * @param parentElement　 父级元素
	 * @param currentParam 当前参数
	 */
	private void buildEncodedSoapMessage(Element parentElement, WsdlParam currentParam, JSONObject inputValue) {
		String paramName = currentParam.getParamName();
		Element paramElement = DOMUtil.addElement(parentElement, paramName);
		WsdlType paramType = currentParam.getParamType();
		if(!paramType.isSimple()){
			List<WsdlParam> childParams = paramType.getChildParams();
			if(childParams != null && childParams.size() > 0){
				for (WsdlParam wsdlParam : childParams) {
					if(inputValue != null){
						buildEncodedSoapMessage(paramElement, wsdlParam,inputValue.getJSONObject(paramName));		//TODO 这里的　getJSONObject 可能存在问题，因为有可能是数组，出问题后修改
					}else{
						buildEncodedSoapMessage(paramElement, wsdlParam,null);
					}
				}
			}
		}else{
			//如果是简单类型,还需要设置类型属性
			paramElement.setAttribute("xsi:type", "xsd:"+paramType.getTypeName());		//TODO 这里命名空间有可能被改变
			if(inputValue != null){
				paramElement.setTextContent(String.valueOf(inputValue.getString(paramName)));
			}
		}
	}

	public String getStyle() {
		return style;
	}

	void setStyle(String style) {
		this.style = style;
	}

	public String getUse() {
		return use;
	}

	void setUse(String use) {
		this.use = use;
	}

	public WsdlContext getWsdlContext() {
		return wsdlContext;
	}

	void setWsdlContext(WsdlContext wsdlContext) {
		this.wsdlContext = wsdlContext;
	}

	public BindingOperation getBindingOperation() {
		return bindingOperation;
	}

	void setBindingOperation(BindingOperation bindingOperation) {
		this.bindingOperation = bindingOperation;
	}

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-21下午3:04:57<br/>
	 * 功能:解析出参 <br/> TODO 对于 http get 和 post 还没有做处理
	 */
	void parserOutput() {
		log.debug("解析方法:"+bindingOperation.getName()+" 出参");
		if(soapType == WsdlPort.SOAPType.SOAP11 || soapType == WsdlPort.SOAPType.SOAP12){
			Output output = operation.getOutput();
			Message message = output.getMessage();
			this.output = findWsdlParam(message);
			log.info(this.name+" 得到出参为:"+this.output);
		}else if(soapType == WsdlPort.SOAPType.HTTP_GET){
			log.warn("暂时未实现 get 请求参数解析");
		}else if(soapType == WsdlPort.SOAPType.HTTP_POST){
			log.warn("暂时未实现 post 请求参数解析");
		}
	}

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-21下午3:05:10<br/>
	 * 功能:解析入参 <br/>
	 */
	void parserInput() {
		log.debug("解析方法:"+bindingOperation.getName()+" 入参");
		if(soapType == WsdlPort.SOAPType.SOAP11 || soapType == WsdlPort.SOAPType.SOAP12){
			Input input = operation.getInput();
			Message message = input.getMessage();
			this.input = findWsdlParam(message);
			log.info(this.name+" 得到入参为:"+this.input);
		}else if(soapType == WsdlPort.SOAPType.HTTP_GET){
			log.warn("暂时未实现 get 请求参数解析");
		}else if(soapType == WsdlPort.SOAPType.HTTP_POST){
			log.warn("暂时未实现 post 请求参数解析");
		}
	}

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-21下午7:03:54<br/>
	 * 功能:获取参数,一般消息都只有一个部分 part,如以后有多个 part 需更改这里 TODO  <br/>
	 * @param message
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private WsdlParam findWsdlParam(Message message) {
		Map<String,Part> parts = message.getParts();
		Iterator<Part> iterator = parts.values().iterator();
		while(iterator.hasNext()){
			Part part = iterator.next();
			QName elementName = part.getElementName();
			String paramName = elementName.getLocalPart();
			WsdlParam param = wsdlContext.getParam(paramName);
			return param;
		}
		return null;
	}

	public WsdlPort.SOAPType getSoapType() {
		return soapType;
	}

	void setSoapType(WsdlPort.SOAPType soapType) {
		this.soapType = soapType;
	}

	public String getSoapActionURI() {
		return soapActionURI;
	}

	void setSoapActionURI(String soapActionURI) {
		this.soapActionURI = soapActionURI;
	}

	public Operation getOperation() {
		return operation;
	}

	void setOperation(Operation operation) {
		this.operation = operation;
	}

	public WsdlParam getInput() {
		return input;
	}

	public WsdlParam getOutput() {
		return output;
	}

	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	public String getPostMessageUrl() {
		return postMessageUrl;
	}

	void setPostMessageUrl(String postMessageUrl) {
		this.postMessageUrl = postMessageUrl;
	}

	
}
