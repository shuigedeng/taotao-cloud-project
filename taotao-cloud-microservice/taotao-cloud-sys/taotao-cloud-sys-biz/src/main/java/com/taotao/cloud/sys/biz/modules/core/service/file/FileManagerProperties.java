package com.taotao.cloud.sys.biz.modules.core.service.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Data
@ConfigurationProperties(prefix = "data.path")
public class FileManagerProperties {
    /**
     * 基础路径配置, 如果后面的路径没配置,则会默认以这个为基础路径 , 即
     * configs: $base/configs
     * tmp: $base/tmp
     * data: $base/data
     */
    private File base;
    /**
     * 配置文件路径
     */
    private File configs;
    /**
     * 临时文件路径
     */
    private File tmp;
    /**
     * 数据目录
     */
    private File data;
}
