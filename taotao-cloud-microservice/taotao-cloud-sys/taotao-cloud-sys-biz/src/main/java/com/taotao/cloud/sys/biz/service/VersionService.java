package com.taotao.cloud.sys.biz.service;

import com.taotao.cloud.sys.biz.tools.core.utils.Version;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class VersionService {
    private Version version;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void printVersion(){
        try {
//            ClassPathResource version = new ClassPathResource("version.txt");
            final Resource resource = applicationContext.getResource("classpath:version.txt");
//            String fileToString = FileUtils.readFileToString(resource.getFile(), StandardCharsets.UTF_8);
            final String fileToString = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
            this.version = new Version(fileToString);
            log.info("当前工具版本:{}",fileToString);
        } catch (IOException e) {log.error("获取当前工具版本失败:{}",e.getMessage());}
    }

    /**
     * 获取当前版本
     * @return
     */
    public Version currentVersion() {
        return version;
    }
}
