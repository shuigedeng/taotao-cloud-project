package com.taotao.cloud.gateway.predicates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.server.mvc.common.Configurable;
import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.web.servlet.function.RequestPredicate;

/**
 * @author 决定“这请求归不归我管”
 * @date 2025/12/15
 * @description 定义包含自定义Predicate的所有实现的类
 *
 * 提示：定义的谓词按一个类一个静态方法，多增加几个静态方法spring也只会加载第一个静态方法，静态方法一定要加@Shortcut注解，不然启动报错，报错原因分析及解决，将在另一篇关于gateway疑难杂症解决的文章中展开。
 * 谓词定义（SampleRequestPredicates.java）：一个类一个静态过滤器方法（一定要加上@ShortCut注解），多加了也没用spring只会加载第一个静态方法
 */
public class SampleRequestPredicates {
    private static final Logger log = LoggerFactory.getLogger(SampleRequestPredicates.class);
    /**
     *
     * @return
     * @date 2025/12/15
     * @description 请求头的判断（是否包含某个名称的请求头）,必须添加@Configurable注解且只能有一个参数，不然无法生效
     *
     */
    @Shortcut
    public static RequestPredicate predicateHeaderExists(String name) {
        log.error("======进入了自定义predicate的处理逻辑所需请求头：{}==========",name);
        return request -> {
            return request.headers().asHttpHeaders().containsHeader(name);
        };
    }

}

