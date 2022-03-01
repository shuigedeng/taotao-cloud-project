package com.taotao.cloud.sys.biz.tools.jvm.controller;

import com.taotao.cloud.sys.biz.tools.jvm.service.ThreadMXBeanService;
import com.taotao.cloud.sys.biz.tools.jvm.service.dtos.ThreadPoolInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.lang.management.ThreadInfo;
import java.util.List;

@RestController
@RequestMapping("/jvm/thread")
@Validated
public class ThreadMXBeanController {

    @Autowired
    private ThreadMXBeanService threadMXBeanService;

    /**
     * 展示所有线程列表
     * @param connName 连接名称
     * @return
     * @throws IOException
     */
    @GetMapping("/list")
    public ThreadInfo[] threads(@NotBlank String connName) throws IOException {
        return threadMXBeanService.threads(connName);
    }

    /**
     * 获取线程详细信息
     * @param connName
     * @param threadId
     * @return
     * @throws IOException
     */
    @GetMapping("/detail")
    public ThreadInfo thread(@NotBlank String connName,long threadId) throws IOException {
        return threadMXBeanService.thread(connName, threadId);
    }

    /**
     * 将线程池进行合并, 展示树状的线程列表
     * @param connName
     * @return
     */
    @GetMapping("/list/threadPools")
    public List<ThreadPoolInfo> threadPools(@NotBlank String connName) throws IOException {
        return threadMXBeanService.threadPoolInfos(connName);
    }

    /**
     * 检查死锁
     * @param connName
     * @return
     */
    @GetMapping("/checkDeadLock")
    public ThreadInfo[] checkDeadLock(@NotBlank String connName) throws ReflectionException, IntrospectionException, IOException, InstanceNotFoundException, MBeanException, ClassNotFoundException {
        return threadMXBeanService.checkDeadLock(connName);
    }
}
