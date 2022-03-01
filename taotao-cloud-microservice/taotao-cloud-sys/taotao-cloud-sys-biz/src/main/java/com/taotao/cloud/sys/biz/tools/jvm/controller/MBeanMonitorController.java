package com.taotao.cloud.sys.biz.tools.jvm.controller;

import com.taotao.cloud.sys.biz.tools.core.service.connect.ConnectService;
import com.taotao.cloud.sys.biz.tools.jvm.service.MBeanMonitorService;
import com.taotao.cloud.sys.biz.tools.jvm.service.dtos.InvokeParam;
import com.taotao.cloud.sys.biz.tools.jvm.service.dtos.NameInfo;
import java.io.IOException;
import java.lang.management.PlatformManagedObject;
import java.util.*;

import javax.management.*;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/jvm/mbean")
@Validated
public class MBeanMonitorController {
    @Autowired
    private MBeanMonitorService mBeanMonitorService;
    @Autowired
    private ConnectService connectService;

    /**
     * 类似 jconsole 那样展示 mbean 树结构
     * @param jmxHostAndPort
     * @return
     */
    @GetMapping("/names")
    public List<NameInfo> mBeans(@NotBlank String connName) throws IOException, IntrospectionException, InstanceNotFoundException, ReflectionException {
        return mBeanMonitorService.mBeans(connName);
    }

    /**
     * 获取 mbean 的详细信息
     * @param jmxHostAndPort 开启了JMX的主机:端口
     * @param mBeanName mBean名称
     * @return
     */
    @GetMapping("/detail")
    public MBeanInfo mBeanInfo(@NotBlank String connName, @NotBlank String mBeanName) throws  IntrospectionException, ReflectionException, InstanceNotFoundException, IOException {
        return mBeanMonitorService.mBeanInfo(connName,mBeanName);
    }

    /**
     * 获取指定 mBean 的单个代理类
     * @param connName 连接名
     * @param mBeanName mBean
     * @param className 类名
     * @return
     */
    @GetMapping("/proxyMXBean")
    public Object proxyMXBean(@NotBlank String connName, @NotBlank String mBeanName, @NotBlank String className) throws IOException, ClassNotFoundException {
        return mBeanMonitorService.proxyMXBean(connName,mBeanName,className);
    }

    /**
     * 获取代理类列表
     * @param connName 连接名
     * @param interfaceName 接口名称
     * @return
     */
    @GetMapping("/proxyMXBeans")
    public List<PlatformManagedObject> proxyMXBeans(@NotBlank String connName, @NotBlank String interfaceName) throws IOException, ClassNotFoundException {
        return mBeanMonitorService.proxyMXBeans(connName,interfaceName);
    }

    /**
     * 获取属性值
     * @param jmxHostAndPort 开启了JMX的主机:端口
     * @param mBeanName bean名称
     * @param attrNames 属性名列表
     * @return
     */
    @GetMapping("/attrValue")
    public List<Attribute> attrValue(@NotBlank String connName, @NotBlank String mBeanName, String[] attrNames) throws  ReflectionException, InstanceNotFoundException, IOException {
        return mBeanMonitorService.attrValue(connName,mBeanName,attrNames);
    }

    /**
     * 调用 mbean 方法
     * @param invokeParam 调用 mbean 的方法参数信息
     * @return
     */
    @PostMapping("/invoke")
    public Object invoke(@RequestBody @Validated InvokeParam invokeParam) throws  ReflectionException, MBeanException, InstanceNotFoundException, IOException, ClassNotFoundException, IntrospectionException {
        return mBeanMonitorService.invokeMBean(invokeParam);
    }
}
