package com.taotao.cloud.sys.biz.tools.soap.controller;

import com.taotao.cloud.sys.biz.tools.soap.dtos.WsdlParam;
import com.taotao.cloud.sys.biz.tools.soap.service.WsdlOperation;
import com.taotao.cloud.sys.biz.tools.soap.service.WsdlPort;
import com.taotao.cloud.sys.biz.tools.soap.service.WsdlService;
import com.taotao.cloud.sys.biz.tools.soap.service.WsdlServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/soap")
@Validated
public class SoapController {

    @Autowired
    private WsdlServiceClient wsdlServiceClient;

    /**
     * 目前已经有的 wsdl 解析列表,这个不做存储
     * @return
     */
    @GetMapping("/services")
    public Set<String> services(){
        return wsdlServiceClient.services();
    }

    /**
     * 列出所有的端点
     * @param wsdl wsdl 地址
     * @return
     */
    @GetMapping("/ports")
    public Set<String> ports(@NotNull String wsdl){
        WsdlService wsdlService = wsdlServiceClient.loadWebservice(wsdl);
        Map<String, WsdlPort> wsdlPortMap = wsdlService.getWsdlPortMap();
        return wsdlPortMap.keySet();
    }

    /**
     * 获取端点所有的方法
     * @param wsdl wsdl 地址
     * @param port port 名称
     * @return
     */
    @GetMapping("/{port}/methods")
    public Set<String> methods(@NotNull String wsdl, @PathVariable("port") String port){
        WsdlService wsdlService = wsdlServiceClient.loadWebservice(wsdl);
        if(wsdlService != null){
            WsdlPort wsdlPort = wsdlService.getWsdlPort(port);
            if (wsdlPort != null){
                Map<String, WsdlOperation> wsdlOperationMap = wsdlPort.getWsdlOperationMap();
                return wsdlOperationMap.keySet();
            }
        }
        return null;
    }

    /**
     * 获取方法的所有入参信息
     * @param wsdl wsdl 地址
     * @param port 端点名称
     * @param operation 方法名称
     * @return
     */
    @GetMapping("/{port}/{operation}/input")
    public WsdlParam methodInputParams(@NotNull String wsdl,@PathVariable("port") String port,@PathVariable("operation") String operation){
        WsdlService wsdlService = wsdlServiceClient.loadWebservice(wsdl);
        if(wsdlService != null){
            WsdlPort wsdlPort = wsdlService.getWsdlPort(port);
            if(wsdlPort != null){
                WsdlOperation wsdlOperation = wsdlPort.getWsdlOperation(operation);
                if(wsdlOperation != null){
                    return wsdlOperation.getInput();
                }
            }
        }
        return null;
    }

    /**
     * 获取方法的所有出参信息
     * @param wsdl wsdl 地址
     * @param port 端点名称
     * @param operation 方法名称
     * @return
     */
    @GetMapping("/{port}/{operation}/output")
    public WsdlParam methodOutputParam(@NotNull String wsdl,@PathVariable("port") String port,@PathVariable("operation") String operation){
        WsdlService wsdlService = wsdlServiceClient.loadWebservice(wsdl);
        if(wsdlService != null){
            WsdlPort wsdlPort = wsdlService.getWsdlPort(port);
            if(wsdlPort != null){
                WsdlOperation wsdlOperation = wsdlPort.getWsdlOperation(operation);
                if(wsdlOperation != null){
                    return wsdlOperation.getOutput();
                }
            }
        }
        return null;
    }

    /**
     * 构建 soap 消息模板,后面让用户输入参数后就可以调用了
     * @param wsdl wsdl 地址
     * @param port 端点名称
     * @param operation 方法名称
     * @return
     */
    @GetMapping("/{port}/{operation}/build")
    public String buildSoapMessageTemplate(@NotNull String wsdl, @PathVariable("port") String port, @PathVariable("operation") String operation){
        WsdlService wsdlService = wsdlServiceClient.loadWebservice(wsdl);
        if(wsdlService != null){
            WsdlPort wsdlPort = wsdlService.getWsdlPort(port);
            if(wsdlPort != null){
                WsdlOperation wsdlOperation = wsdlPort.getWsdlOperation(operation);
                if(wsdlOperation != null){
                    return wsdlOperation.buildRequestTemplate();
                }
            }
        }
        return null;
    }

    /**
     * 发起方法调用
     * @param wsdl wsdl 地址
     * @param port 端点名称
     * @param operation 方法名称
     * @param message 请求消息
     * @return
     * @throws IOException
     */
    @PostMapping("/{port}/{operation}/request")
    public String sendRequest(@NotNull String wsdl, @PathVariable("port") String port, @PathVariable("operation") String operation, @RequestBody String message) throws IOException {
        WsdlService wsdlService = wsdlServiceClient.loadWebservice(wsdl);
        if(wsdlService != null){
            WsdlPort wsdlPort = wsdlService.getWsdlPort(port);
            if(wsdlPort != null){
                WsdlOperation wsdlOperation = wsdlPort.getWsdlOperation(operation);
                if(wsdlOperation != null){
                    return wsdlOperation.invoke(message);
                }
            }
        }
        return null;
    }
}
