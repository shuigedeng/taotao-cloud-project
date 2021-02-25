package com.taotao.cloud.standalone.system.modules.data.tenant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname PreTenantConfigProperties
 * @Description 多租户动态配置
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-08-09 23:36
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "pre.tenant")
public class PreTenantConfigProperties {

    /**
     * 维护租户id
     */
    private String tenantIdColumn;

    /**
     * 多租户的数据表集合
     */
    private List<String> ignoreTenantTables = new ArrayList<>();
}
