package com.taotao.cloud.sys.biz.api.controller.tools.dubbo.service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.taotao.cloud.sys.biz.api.controller.tools.classloader.ClassloaderService;
import com.taotao.cloud.sys.biz.api.controller.tools.core.exception.ToolException;
import com.taotao.cloud.sys.biz.api.controller.tools.dubbo.DubboProviderDto;
import com.taotao.cloud.sys.biz.api.controller.tools.core.service.connect.ConnectService;
import com.taotao.cloud.sys.biz.api.controller.tools.core.service.connect.dtos.ConnectOutput;
import com.taotao.cloud.sys.biz.api.controller.tools.dubbo.dtos.DubboInvokeParam;
import com.taotao.cloud.sys.biz.api.controller.tools.zookeeper.service.ZookeeperService;
import org.apache.dubbo.remoting.exchange.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MainDubboService {

    @Autowired
    private ZookeeperService zookeeperService;
    @Autowired
    private ClassloaderService classloaderService;

    @Autowired
    private ConnectService connectService;

    /**
     * 检查是否存在 dubbo 服务
     * @param connName
     * @return
     * @throws IOException
     */
    public boolean checkIsDubbo(String connName) throws IOException {
        return zookeeperService.exists(connName,"/dubbo");
    }

    /**
     * 这个主要从 zookeeper 上取有哪些服务
     * 依赖于 zookeeper
     * @param connName
     * @return
     */
    public List<String> services(String connName) throws IOException {
        List<String> childrens = zookeeperService.childrens(connName, "/dubbo");
        return childrens;
    }

    /**
     * 从 zookeeper 上获取,当前服务有哪些提供者
     * @param connName
     * @param serviceName
     * @return
     * @throws IOException
     */
    public List<DubboProviderDto> providers(String connName, String serviceName) throws IOException {
        List<DubboProviderDto> dubboProviderDtos = new ArrayList<>();

        List<String> childrens = zookeeperService.childrens(connName, "/dubbo/" + serviceName+"/providers");
        for (String children : childrens) {
            String decode = URLDecoder.decode(children, StandardCharsets.UTF_8.name());
            URL url = URL.valueOf(decode);
            String address = url.getAddress();
            String serviceInterface = url.getServiceInterface();
            String methods = url.getParameter("methods");
            String group = url.getParameter("group");
            String version = url.getParameter("version");
            String dubbo = url.getParameter("dubbo");
            long timestamp = url.getParameter("timestamp",System.currentTimeMillis());
            String application = url.getParameter("application");
            DubboProviderDto dubboProviderDto = new DubboProviderDto(url.toString(),address);
            dubboProviderDto.config(serviceInterface,group,version,methods,dubbo,timestamp,application);

            dubboProviderDtos.add(dubboProviderDto);
        }

        return dubboProviderDtos;
    }

    private String [] primitiveTypeNames = {"long"};

    public Object invoke(DubboInvokeParam dubboInvokeParam) throws ClassNotFoundException, NoSuchMethodException, RemotingException, ExecutionException, InterruptedException {
        String classloaderName = dubboInvokeParam.getClassloaderName();
        String serviceClassName = dubboInvokeParam.getServiceName();

        // 解析出 class
        ClassLoader classloader = classloaderService.getClassloader(classloaderName);
        if (classloader == null){
            classloader = ClassLoader.getSystemClassLoader();
        }
        Class<?> clazz = classloader.loadClass(serviceClassName);

        // 解析出方法
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Method method = null;
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals(dubboInvokeParam.getMethodName())){
                method = declaredMethod;
                break;
            }
        }

        // 解析参数
        Class<?>[] parameterTypes = method.getParameterTypes();
        JSONArray args = dubboInvokeParam.getArgs();
        Object [] argArray = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Object object = args.get(i);
            if (object instanceof JSONObject){
                JSONObject current = (JSONObject) object;
                object = JSON.parseObject(current.toJSONString(),parameterTypes[i]);
            }
            argArray[i] = object;
        }

        // 得到要请求的提供者信息
        String providerURL = dubboInvokeParam.getProviderURL();
        URL provider = URL.valueOf(providerURL);
        provider = provider.addParameter(Constants.CODEC_KEY, "dubbo");

        // 请求体封装
        HashMap<String, String> map = getAttachmentFromUrl(provider);
        Request request = new Request();
        request.setVersion(provider.getParameter("version"));
        request.setTwoWay(true);
        request.setData(new RpcInvocation(method, argArray,map));

        // 请求数据
        DoeClient client = new DoeClient(provider);
        client.doConnect();
        client.send(request);
        CompletableFuture<RpcResult> future = ResponseDispatcher.getDispatcher().getFuture(request);
        RpcResult rpcResult = future.get();
        ResponseDispatcher.getDispatcher().removeFuture(request);
        return rpcResult.getValue();
    }

    public static HashMap<String,String> getAttachmentFromUrl(URL url) {

        String interfaceName = url.getParameter(Constants.INTERFACE_KEY, "");
        if (StringUtils.isEmpty(interfaceName)) {
            throw new ToolException("找不到接口名称！");
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Constants.PATH_KEY, interfaceName);
        map.put(Constants.VERSION_KEY, url.getParameter(Constants.VERSION_KEY));
        map.put(Constants.GROUP_KEY, url.getParameter(Constants.GROUP_KEY));
        /**
         *  doesn't necessary to set these params.
         *
         map.put(Constants.SIDE_KEY, Constants.CONSUMER_SIDE);
         map.put(Constants.DUBBO_VERSION_KEY, Version.getVersion());
         map.put(Constants.TIMESTAMP_KEY, String.valueOf(System.currentTimeMillis()));
         map.put(Constants.PID_KEY, String.valueOf(ConfigUtils.getPid()));
         map.put(Constants.METHODS_KEY, methodNames);
         map.put(Constants.INTERFACE_KEY, interfaceName);
         map.put(Constants.VERSION_KEY, "1.0"); // 不能设置这个，不然服务端找不到invoker
         */
        return map;
    }

//    @PostConstruct
//    public void register(){
//        pluginManager.register(PluginDto.builder().module("call").name("dubbo").author("9420").desc("依赖 zookeeper ,在线调用 dubbo 方法").logo("dubbo.jpg").build());
//    }

    public List<String> connects() {
        final List<ConnectOutput> connectOutputs = connectService.moduleConnects(ZookeeperService.module);
//        List<String> names = connectService.names(ZookeeperService.module);
        List<String> connects = new ArrayList<>();
        for (ConnectOutput connectOutput : connectOutputs) {
            final String name = connectOutput.getConnectInput().getBaseName();
            try {
                boolean isDubbo = checkIsDubbo(name);
                if (isDubbo){
                    connects.add(name);
                }
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        return connects;
    }
}
