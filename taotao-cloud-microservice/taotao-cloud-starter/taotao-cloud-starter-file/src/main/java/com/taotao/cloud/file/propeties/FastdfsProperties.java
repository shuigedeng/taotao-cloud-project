package com.taotao.cloud.file.propeties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Fastdfs文件服务Properties
 *
 * @author dengtao
 * @date 2020/10/26 09:40
 * @since v1.0
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "taotao.cloud.file.fastdfs")
public class FastdfsProperties {
    /**
     * 读取时间
     */
    private int soTimeout = 1000;
    /**
     * 连接超时时间
     */
    private int connectTimeout = 1000;
    /**
     * Tracker 地址列表
     */
    private List<String> trackerList = new ArrayList<>();
}
