package com.taotao.cloud.sys.biz.tools.soap.service;

import com.ibm.wsdl.extensions.http.HTTPAddressImpl;
import com.ibm.wsdl.extensions.soap.SOAPAddressImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12AddressImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-6-21下午2:51:46<br/>
 * 功能:webservice 服务 <br/>
 */
@Slf4j
public class WsdlService {
	private WsdlContext wsdlContext;
	private Service service;
	private Map<String,WsdlPort> wsdlPortMap = new HashMap<String, WsdlPort>();

	public WsdlContext getWsdlContext() {
		return wsdlContext;
	}

	void setWsdlContext(WsdlContext wsdlContext) {
		this.wsdlContext = wsdlContext;
	}

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-6-21下午2:49:24<br/>
	 * 功能:解析当前服务所有 port <br/>
	 * @param findAllService
	 */
	@SuppressWarnings("unchecked")
	void parserPorts() {
		QName qName = service.getQName();
		log.debug("正在解析服务:"+qName.getLocalPart());
		Map<String,Port> ports = service.getPorts();
		Iterator<Entry<String, Port>> iterator = ports.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Port> portEntry = iterator.next();
			Port port = portEntry.getValue();
			WsdlPort wsdlPort = new WsdlPort();
			wsdlPort.setWsdlContext(wsdlContext);
			wsdlPort.setPort(port);
			List<Object> addressImpls = port.getExtensibilityElements();
			if(addressImpls != null && addressImpls.size() > 0){
				Object addressImpl = addressImpls.get(0);
				if(addressImpl instanceof HTTPAddressImpl){
					HTTPAddressImpl httpAddressImpl = (HTTPAddressImpl) addressImpl;
					String locationURI = httpAddressImpl.getLocationURI();
					wsdlPort.setPostMessageUrl(locationURI);
				}else if(addressImpl instanceof SOAPAddressImpl){
					SOAPAddressImpl soapAddressImpl = (SOAPAddressImpl) addressImpl;
					String locationURI = soapAddressImpl.getLocationURI();
					wsdlPort.setPostMessageUrl(locationURI);
				}else if(addressImpl instanceof SOAP12AddressImpl){
					SOAP12AddressImpl soap12AddressImpl = (SOAP12AddressImpl) addressImpl;
					String locationURI = soap12AddressImpl.getLocationURI();
					wsdlPort.setPostMessageUrl(locationURI);
				}
			}
			wsdlPortMap.put(port.getName(), wsdlPort);
			wsdlPort.parserBindingOperations();
		}
	}


	void setService(Service service) {
		this.service = service;
	}

	public Service getService() {
		return service;
	}

	public Map<String, WsdlPort> getWsdlPortMap() {
		return wsdlPortMap;
	}
	
	public WsdlPort getWsdlPort(String portName){
		return wsdlPortMap.get(portName);
	}
}
