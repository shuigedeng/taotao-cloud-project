package com.taotao.cloud.sys.biz.tools.soap.service;

import com.taotao.cloud.common.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.wsdl.Definition;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * 功能:加载 webservice 主类 <br/>
 */
public class WsdlServiceLoader {
	
	private static WSDLFactory wsdlFactory = null;
	static{
		try {
			wsdlFactory = WSDLFactory.newInstance();
		} catch (WSDLException e) {
			LogUtil.error("WsdlServiceLoader init error: {}",e.getMessage(),e);
		}
	}

	/**
	 * 
	 * 功能:从网络路径加载 webservice<br/>
	 * @param url
	 * @return
	 */
	public static WsdlService loadService(URL url){
		if(url == null){
			throw new IllegalArgumentException("wsdl 地址为空");
		}
		String query = url.getQuery();
		if(query != null && !query.endsWith("wsdl")){
			throw new IllegalArgumentException("wsdl 文档地址需要以 wsdl 结尾 url:"+url);
		}
		WsdlContext wsdlContext = parserWsdlContext(url);
		WsdlService wsdlService = new WsdlService();
		wsdlService.setWsdlContext(wsdlContext);
		Service findAllService = findAllService(wsdlContext);
		wsdlService.setService(findAllService);
		wsdlService.parserPorts();
		return wsdlService;
	}
	
	/**
	 * 
	 * 功能: 只会解析第一个服务,有多个服务类以后再说 TODO  <br/>
	 * @param wsdlContext
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Service findAllService(WsdlContext wsdlContext) {
		Definition definition = wsdlContext.getDefinition();
		Map<String,Service> allServices = definition.getAllServices();
		Iterator<Entry<String, Service>> iterator = allServices.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Service> serviceEntry = iterator.next();
			Service service = serviceEntry.getValue();
			return service;
		}
		return null;
	}

	/**
	 * 
	 * 功能:从文件系统加载 webservice <br/>
	 * @param file
	 * @return
	 */
	public static WsdlService loadService(File file){
		if(file == null || !file.exists()){
			throw new IllegalArgumentException("wsdl 文件不存在");
		}
		try {
			URI fileURI = file.toURI();
			URL fileURL = fileURI.toURL();
			return loadService(fileURL);
		} catch (MalformedURLException e) {
			LogUtil.error("WsdlServiceLoader loadService error : {}",e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 
	 * 功能:解析 webservice 上下文方法 <br/>
	 * @param url
	 */
	private static WsdlContext parserWsdlContext(URL url) {
		WSDLReader wsdlReader = wsdlFactory.newWSDLReader();
		wsdlReader.setFeature("javax.wsdl.verbose", true);
		wsdlReader.setFeature("javax.wsdl.importDocuments", true);
		try {
			LogUtil.info("读取 wsdl 文件:"+url);
			Definition definition = wsdlReader.readWSDL(url.toString());
			WsdlContext wsdlContext = new WsdlContext();
			wsdlContext.setDefinition(definition);
			wsdlContext.setWsdlURL(url);
			String targetNamespace = definition.getTargetNamespace();
			wsdlContext.setTargetNamespace(targetNamespace);
			wsdlContext.processTypes();
			return wsdlContext;
		} catch (WSDLException e) {
			LogUtil.error("WsdlServiceLoader parserWsdlContext error : {}",e.getMessage(),e);
		}
		return null;
	}
	

}
