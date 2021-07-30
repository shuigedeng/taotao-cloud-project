package com.taotao.cloud.shardingsphere.annotation;

import com.taotao.cloud.shardingsphere.ShardingJdbcConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * TaoTaoCloudApplication
 *
 * @author shuigedeng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ShardingJdbcConfiguration.class})
public @interface EnableTaoTaoCloudShardingsphere {

}
