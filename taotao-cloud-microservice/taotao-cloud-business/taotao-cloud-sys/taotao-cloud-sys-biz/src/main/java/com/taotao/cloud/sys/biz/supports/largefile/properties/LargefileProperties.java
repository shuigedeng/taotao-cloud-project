package com.taotao.cloud.sys.biz.supports.largefile.properties;

import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * LargefileProperties
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = LargefileProperties.PREFIX)
public class LargefileProperties {

    public static final String PREFIX = "upload";

    private Integer chunkSize;
    private Integer threadMaxSize;
    private Integer queueMaxSize;
    private String queueDir;

}
