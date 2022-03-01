package com.taotao.cloud.sys.biz.tools.serializer.controller;

import com.sanri.tools.modules.serializer.service.SerializerChoseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/serializer")
public class SerializerController {
    @Autowired
    private SerializerChoseService serializerChoseService;

    /**
     * 序列化工具列表
     * @return
     */
    @GetMapping("/names")
    public Set<String> serializers(){
        return serializerChoseService.serializers();
    }
}
