package com.taotao.cloud.java.springannotation.userdefinedannotation.test;

import com.taotao.cloud.java.springannotation.userdefinedannotation.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath:spring2.xml")
@Component
public class MyServer3 {
    @Autowired
	HelloService helloService;

    public void helloTest1() {
        System.out.println("开始junit测试……");
        String hello = helloService.hello("ooooooo");
        System.out.println(hello);
    }

}
