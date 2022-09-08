package com.taotao.cloud.data.mybatisplus.datascope.perm.configuration;

import com.taotao.cloud.data.mybatisplus.datascope.perm.scope.DataScopeInterceptor;
import com.taotao.cloud.data.mybatisplus.datascope.perm.select.SelectFieldPermInterceptor;
import com.taotao.cloud.data.mybatisplus.interceptor.MpInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**   
* 数据权限配置
*/
@Configuration
public class DatePermConfiguration {

    /**
     * 数据范围权限插件
     */
    @Bean
    @ConditionalOnProperty(prefix = "bootx.starter.data-perm", value = "enableDataPerm", havingValue = "true",matchIfMissing = true)
    public MpInterceptor dataPermInterceptorMp(DataScopeInterceptor dataScopeInterceptor) {
        return new MpInterceptor(dataScopeInterceptor);
    }

    /**
     * 查询字段权限插件
     */
    @Bean
    @ConditionalOnProperty(prefix = "bootx.starter.data-perm", value = "enableSelectFieldPerm", havingValue = "true",matchIfMissing = true)
    public MpInterceptor selectFieldPermInterceptorMp(SelectFieldPermInterceptor bootxDataPermissionHandler) {
        return new MpInterceptor(bootxDataPermissionHandler);
    }
}
