package com.taotao.cloud.lock.kylin.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * zookeeper注入条件判断类
 * 只要配置zk服务器地址 则生效
 *
 * @author wangjinkui
 */
public class ZookeeperCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, @SuppressWarnings("NullableProblems") AnnotatedTypeMetadata annotatedTypeMetadata) {
        final Environment environment = conditionContext.getEnvironment();
        return environment.containsProperty("kylin.lock.zookeeper.zkServers")
                || environment.containsProperty("kylin.lock.zookeeper.zk-servers");
    }
}
