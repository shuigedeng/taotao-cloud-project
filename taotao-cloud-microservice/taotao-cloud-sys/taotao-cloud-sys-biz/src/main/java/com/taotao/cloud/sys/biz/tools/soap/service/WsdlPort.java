package com.taotao.cloud.sys.biz.tools.soap.service;

import com.ibm.wsdl.extensions.http.HTTPBindingImpl;
import com.ibm.wsdl.extensions.http.HTTPOperationImpl;
import com.ibm.wsdl.extensions.http.HTTPUrlEncodedImpl;
import com.ibm.wsdl.extensions.mime.MIMEContentImpl;
import com.ibm.wsdl.extensions.soap.SOAPBindingImpl;
import com.ibm.wsdl.extensions.soap.SOAPBodyImpl;
import com.ibm.wsdl.extensions.soap.SOAPOperationImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12BindingImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12BodyImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12OperationImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.wsdl.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class WsdlPort {
	private Port port;
	private WsdlContext wsdlContext;
	private Map<String,WsdlOperation> wsdlOperationMap = new HashMap<String, WsdlOperation>();
	private String postMessageUrl;		//对于每一个 port 都有个发送请求的地址,尽管现在看来,发送的地址都是一样的 add by sanri at 2017/06/22 
	
	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-21下午6:51:44<br/>
	 * 功能:soap 类型 <br/>
	 */
	public enum SOAPType{
		SOAP11,SOAP12,HTTP_GET,HTTP_POST
	}

	public Port getPort() {
		return port;
	}

	void setPort(Port port) {
		this.port = port;
	}

	public WsdlContext getWsdlContext() {
		return wsdlContext;
	}

	void setWsdlContext(WsdlContext wsdlContext) {
		this.wsdlContext = wsdlContext;
	}

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-21下午2:56:26<br/>
	 * 功能:解析 port 绑定所有方法 <br/>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	void parserBindingOperations() {
		String name = port.getName();
		log.debug("正在解析 port:"+name);
		Binding binding = port.getBinding();
		//也有可能　style 就是放在　binding 中的，优先取　BindingOperation 中的，没有再取　binding 中的
		List bindingImpls = binding.getExtensibilityElements();
		String commonStyle = "";
		if(bindingImpls != null && bindingImpls.size() > 0){
			Object bindingImpl = bindingImpls.get(0);
			if(bindingImpl instanceof SOAPBindingImpl){
				SOAPBindingImpl soapBindingImpl = (SOAPBindingImpl) bindingImpl;
				commonStyle = soapBindingImpl.getStyle();
			}else if(bindingImpl instanceof SOAP12BindingImpl){
				SOAP12BindingImpl soapBinding12Impl = (SOAP12BindingImpl) bindingImpl;
				commonStyle = soapBinding12Impl.getStyle();
			}else if(bindingImpl instanceof HTTPBindingImpl){
				HTTPBindingImpl httpBindingImpl = (HTTPBindingImpl) bindingImpl;
			}else{
				System.out.println("还有其它绑定实现　"+bindingImpl);
			}
		}
		List<BindingOperation> operations = binding.getBindingOperations();
		if(operations != null && operations.size() > 0){
			for (BindingOperation bindingOperation : operations) {
				Operation operation = bindingOperation.getOperation();
				WsdlOperation wsdlOperation = new WsdlOperation();
				wsdlOperation.setOperation(operation);
				wsdlOperation.setBindingOperation(bindingOperation);
				wsdlOperation.setWsdlContext(wsdlContext);
				wsdlOperation.setPostMessageUrl(postMessageUrl);
				wsdlOperation.setName(operation.getName());
				wsdlOperationMap.put(bindingOperation.getName(), wsdlOperation);
				//解析 style 和 use 
				List operationImpls = bindingOperation.getExtensibilityElements();
				if(operationImpls != null && operationImpls.size() > 0){
					Object operationImpl = operationImpls.get(0);
					if(operationImpl instanceof SOAPOperationImpl){
						SOAPOperationImpl soapOperation = (SOAPOperationImpl) operationImpl;
						String style = soapOperation.getStyle();
						String soapActionURI = soapOperation.getSoapActionURI();
						wsdlOperation.setSoapActionURI(soapActionURI);
						if(StringUtils.isNotBlank(style)){
							wsdlOperation.setStyle(style);
						}else{
							wsdlOperation.setStyle(commonStyle);
						}
					}else if(operationImpl instanceof SOAP12OperationImpl){
						SOAP12OperationImpl soap12OperationImpl = (SOAP12OperationImpl) operationImpl;
						String style = soap12OperationImpl.getStyle();
						if(StringUtils.isNotBlank(style)){
							wsdlOperation.setStyle(style);
						}else{
							wsdlOperation.setStyle(commonStyle);
						}
					}else if(operationImpl instanceof HTTPOperationImpl){
						HTTPOperationImpl httpOperationImpl = (HTTPOperationImpl) operationImpl;
//						System.out.println(httpOperationImpl);
					}else{
						log.info("其它类型");
					}
				}
				BindingInput bindingInput = bindingOperation.getBindingInput();
				List bodyImpls = bindingInput.getExtensibilityElements();
				if(bodyImpls != null && bodyImpls.size() > 0){
					Object bodyImpl = bodyImpls.get(0);
					if(bodyImpl instanceof SOAPBodyImpl){
						SOAPBodyImpl soapBodyImpl = (SOAPBodyImpl) bodyImpl;
						String use = soapBodyImpl.getUse();
						wsdlOperation.setUse(use);
						wsdlOperation.setSoapType(SOAPType.SOAP11);
					}else if(bodyImpl instanceof MIMEContentImpl){
						MIMEContentImpl mimeContentImpl = (MIMEContentImpl) bodyImpl;
//						System.out.println(mimeContentImpl);
						wsdlOperation.setSoapType(SOAPType.HTTP_POST);
					}else if(bodyImpl instanceof SOAP12BodyImpl){
						SOAP12BodyImpl soap12BodyImpl = (SOAP12BodyImpl) bodyImpl;
						String use = soap12BodyImpl.getUse();
						wsdlOperation.setUse(use);
						wsdlOperation.setSoapType(SOAPType.SOAP12);
					}else if(bodyImpl instanceof HTTPUrlEncodedImpl){
						HTTPUrlEncodedImpl httpUrlEncodedImpl = (HTTPUrlEncodedImpl) bodyImpl;
						wsdlOperation.setSoapType(SOAPType.HTTP_GET);
//						System.out.println(httpUrlEncodedImpl);
					}else{
						System.out.println("其它类型2222");
					}
				}
				wsdlOperation.parserInput();
				wsdlOperation.parserOutput();
			}
		}
	}

	public Map<String, WsdlOperation> getWsdlOperationMap() {
		return wsdlOperationMap;
	}
	
	public WsdlOperation getWsdlOperation(String operationName){
		return wsdlOperationMap.get(operationName);
	}

	public String getPostMessageUrl() {
		return postMessageUrl;
	}

	void setPostMessageUrl(String postMessageUrl) {
		this.postMessageUrl = postMessageUrl;
	}
	
}
