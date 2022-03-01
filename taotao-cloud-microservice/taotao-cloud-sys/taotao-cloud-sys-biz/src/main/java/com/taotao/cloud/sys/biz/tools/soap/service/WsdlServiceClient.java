package com.taotao.cloud.sys.biz.tools.soap.service;

import com.taotao.cloud.common.utils.LogUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class WsdlServiceClient {
    private Map<String,WsdlService> wsdlServiceMap = new ConcurrentHashMap<>();



    public Set<String> services(){
        return wsdlServiceMap.keySet();
    }

    /**
     * 加载一个 webservice
     * @param wsdl
     * @return
     */
    public WsdlService loadWebservice(String wsdl){
        URL url = fixedDeployDocumentURL(wsdl);
        String fixedWsdlUrl = url.toString();
        WsdlService wsdlService = wsdlServiceMap.get(fixedWsdlUrl);
        if(wsdlService == null) {
            wsdlService = WsdlServiceLoader.loadService(url);
            wsdlServiceMap.put(fixedWsdlUrl,wsdlService);
        }
        return wsdlService;
    }


    /**
     *
     * 功能:修正为 wsdl 正确地址<br/>
     * 创建时间:2017-6-7下午9:02:54<br/>
     * 作者：sanri<br/>
     * @param webserviceUrl 传入的 url 要么不带一个参数,要么带 ?wsdl ,其它情况不支持,暂时不会报错,以后将要修改方法使其抛出异常
     * @return<br/>
     */
    private static URL fixedDeployDocumentURL(String webserviceUrl){
        if(StringUtils.isBlank(webserviceUrl)){
            throw new IllegalArgumentException("请传入合法的 wsdl 地址,目前传入为:"+webserviceUrl);
        }
        if(!webserviceUrl.endsWith("?wsdl")){
            webserviceUrl = webserviceUrl + "?wsdl";
	        LogUtil.warn("修正 wsdl 地址为:"+webserviceUrl);
        }
        URL url = null;
        try {
            url = new URL(webserviceUrl);
            return url;
        } catch (MalformedURLException e) {
	        LogUtil.error("传入路径不合法:{},请使用 wsdl 文档地址",webserviceUrl,e);
        }
        return null;
    }

//    @PostConstruct
//    public void register(){
//        pluginManager.register(PluginDto.builder().module("call").name("soap").author("9420").logo("soap.jpg").desc("快速调用soap 接口").build());
//    }
}
