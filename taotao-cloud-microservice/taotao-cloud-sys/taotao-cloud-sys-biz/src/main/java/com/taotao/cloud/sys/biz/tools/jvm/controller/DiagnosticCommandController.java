package com.taotao.cloud.sys.biz.tools.jvm.controller;

import com.sanri.tools.modules.jvm.service.DiagnosticCommandService;
import com.sanri.tools.modules.jvm.service.dtos.HeapHistogramImpl;
import com.sanri.tools.modules.jvm.service.dtos.VMParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.VM;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/jvm/diagnostic")
@Validated
public class DiagnosticCommandController {
    @Autowired
    private DiagnosticCommandService heapService;

    /**
     * 监控堆区内存数据
     * @return
     */
    @GetMapping("/gcClassHistogram")
    public HeapHistogramImpl gcClassHistogram(@NotBlank String connName, boolean all) throws MalformedObjectNameException, ReflectionException, MBeanException, InstanceNotFoundException, IOException {
        return heapService.gcClassHistogram(connName,all);
    }

    @GetMapping("/flagsSetted")
    public List<VMParam> flagsSetted(@NotBlank String connName) throws ReflectionException, MBeanException, InstanceNotFoundException, IOException {
        return heapService.flagsSetted(connName);
    }
}
