/**
 * Project Name: my-projects
 * Package Name: com.taotao.rpc.server
 * Descroption: 标注rpc服务
 * Date: 2020/2/27 14:03
 * Author: dengtao
 */
package com.taotao.cloud.rpc.other.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注是rpc服务 <br>
 *
 * @author dengtao
 * @version v1.0.0
 * @create 2020/2/27 14:03
 * @since v1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {
    Class<?> value();
}
