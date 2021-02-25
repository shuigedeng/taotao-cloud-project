package com.taotao.cloud.file.propeties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * nginx文件服务Properties
 *
 * @author dengtao
 * @date 2020/10/26 09:39
 * @since v1.0
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "taotao.cloud.file.nginx")
public class NginxProperties {
    /**
     * nginx上传路径
     */
    private String uploadPath;
    /**
     * nginx文件下载路径
     */
    private String downPath;
}
