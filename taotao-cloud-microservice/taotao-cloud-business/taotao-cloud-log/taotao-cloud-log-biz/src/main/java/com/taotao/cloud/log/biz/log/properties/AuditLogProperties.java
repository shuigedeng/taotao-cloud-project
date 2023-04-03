package com.taotao.cloud.log.biz.log.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**   
* 审计日志配置
* @author xxm  
* @date 2021/12/2 
*/
@Getter
@Setter
@ConfigurationProperties(prefix = "bootx.starter.audit-log")
public class AuditLogProperties {

    /**
     * 存储方式, 默认为数据库
     */
    private Store store = Store.JDBC;

    /**
     * 存储类型
     */
    public enum Store{
        /** 数据库 */
        JDBC,
        /** MongoDB */
        MONGO
    }
}
