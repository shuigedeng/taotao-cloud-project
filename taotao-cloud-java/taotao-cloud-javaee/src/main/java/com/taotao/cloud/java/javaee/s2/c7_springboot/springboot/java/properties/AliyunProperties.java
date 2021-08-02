package com.taotao.cloud.java.javaee.s2.c7_springboot.springboot.java.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "aliyun")
@Component
public class AliyunProperties {

    private String xxxx;

    private String yyyy;

    private String zzzz;

    private String aaaa;

    private String bbbb;

}
