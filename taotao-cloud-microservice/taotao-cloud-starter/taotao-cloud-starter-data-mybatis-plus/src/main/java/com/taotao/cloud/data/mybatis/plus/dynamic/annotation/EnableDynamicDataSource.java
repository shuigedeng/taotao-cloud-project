
package com.taotao.cloud.data.mybatis.plus.dynamic.annotation;

import com.fxz.common.database.DynamicDataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author fxz
 * @date 2022/3/30
 * <p>
 * 开启动态数据源
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(DynamicDataSourceAutoConfiguration.class)
public @interface EnableDynamicDataSource {

}
