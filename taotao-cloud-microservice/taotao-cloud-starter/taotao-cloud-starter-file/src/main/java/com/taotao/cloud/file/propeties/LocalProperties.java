package com.taotao.cloud.file.propeties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 本地文件服务Properties
 *
 * @author dengtao
 * @date 2020/10/26 09:40
 * @since v1.0
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "taotao.cloud.file.local")
public class LocalProperties {

    public static String sysPath = System.getProperty("user.dir");

    private String endpoint = "http://127.0.0.1:8080";

    private String filePath = sysPath + "/upload";

    private String filDir = "/upload";
}
