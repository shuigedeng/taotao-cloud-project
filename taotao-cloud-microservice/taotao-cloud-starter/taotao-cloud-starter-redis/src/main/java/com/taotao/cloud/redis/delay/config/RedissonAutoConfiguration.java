package com.taotao.cloud.redis.delay.config;

import com.taotao.cloud.redis.delay.consts.ServerType;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Objects;


@Configuration
@ConditionalOnClass({RedissonClient.class, Redisson.class})
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {

    @Scope(BeanDefinition.SCOPE_SINGLETON)
    @Bean(name = RedissonConfigUtils.REDISSON_QUEUE_BEAN_PROCESSOR_BEAN_NAME)
    public RedissonQueueBeanPostProcessor redissonQueueBeanPostProcessor() {
        return new RedissonQueueBeanPostProcessor();
    }

    @Scope(BeanDefinition.SCOPE_SINGLETON)
    @Bean(name = RedissonConfigUtils.REDISSON_QUEUE_REGISTRY_BEAN_NAME)
    public RedissonQueueRegistry redissonQueueRegistry() {
        return new RedissonQueueRegistry();
    }

    @Scope(BeanDefinition.SCOPE_SINGLETON)
    @Bean
    @ConditionalOnMissingBean
    public RedissonTemplate redissonTemplate() {
        return new RedissonTemplate();
    }

    @Scope(BeanDefinition.SCOPE_SINGLETON)
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(RedissonProperties properties) {
        final ServerType serverType = properties.getServerType();
        final String serverAddress = properties.getServerAddress();
        Assert.hasText(serverAddress, "redis cluster nodes config error");
        final String password = properties.getPassword();
        Config config = new Config();
        config.setThreads(properties.getThreads());
        config.setNettyThreads(properties.getNettyThreads());
        config.setLockWatchdogTimeout(properties.getLockWatchdogTimeoutMillis());
        if (serverType == ServerType.SINGLE) {
            SingleServerConfig singleServerConfig = config.useSingleServer();
            singleServerConfig.setAddress(this.checkAndFixAddress(serverAddress));
            if (StringUtils.hasText(password)) {
                singleServerConfig.setPassword(password);
            }
            singleServerConfig.setDatabase(properties.getDatabase());
            singleServerConfig.setConnectTimeout(properties.getConnectTimeoutMillis());
            singleServerConfig.setTimeout(properties.getSocketTimeoutMillis());
            singleServerConfig.setKeepAlive(properties.isKeepAlive());
            singleServerConfig.setRetryAttempts(properties.getRetryAttempts());
            singleServerConfig.setRetryInterval(properties.getRetryIntervalMillis());
            singleServerConfig.setConnectionPoolSize(properties.getMaxPoolSize());
            singleServerConfig.setConnectionMinimumIdleSize(properties.getMinIdleSize());
            singleServerConfig.setIdleConnectionTimeout(properties.getMaxIdleMillis());
            return Redisson.create(config);
        }
        if (serverType == ServerType.CLUSTER) {
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            String[] nodes = serverAddress.split("[,;]");
            for (String node : nodes) {
                clusterServersConfig.addNodeAddress(this.checkAndFixAddress(node));
            }
            if (StringUtils.hasText(password)) {
                clusterServersConfig.setPassword(password);
            }
            clusterServersConfig.setConnectTimeout(properties.getConnectTimeoutMillis());
            clusterServersConfig.setTimeout(properties.getSocketTimeoutMillis());
            clusterServersConfig.setKeepAlive(properties.isKeepAlive());
            clusterServersConfig.setRetryAttempts(properties.getRetryAttempts());
            clusterServersConfig.setRetryInterval(properties.getRetryIntervalMillis());
            final int masterMaxPoolSize = Objects.nonNull(properties.getMaster()) ? properties.getMaster().getMaxPoolSize() : properties.getMaxPoolSize();
            final int masterMinIdleSize = Objects.nonNull(properties.getMaster()) ? properties.getMaster().getMinIdleSize() : properties.getMinIdleSize();
            final int slaveMaxPoolSize = Objects.nonNull(properties.getSlave()) ? properties.getSlave().getMaxPoolSize() : properties.getMaxPoolSize();
            final int slaveMinIdleSize = Objects.nonNull(properties.getSlave()) ? properties.getSlave().getMinIdleSize() : properties.getMinIdleSize();
            clusterServersConfig.setMasterConnectionPoolSize(masterMaxPoolSize);
            clusterServersConfig.setMasterConnectionMinimumIdleSize(masterMinIdleSize);
            clusterServersConfig.setSlaveConnectionPoolSize(slaveMaxPoolSize);
            clusterServersConfig.setSlaveConnectionMinimumIdleSize(slaveMinIdleSize);
            clusterServersConfig.setIdleConnectionTimeout(properties.getMaxIdleMillis());
            return Redisson.create(config);
        }
        throw new BeanCreationException("redis server type is illegal");
    }

    private String checkAndFixAddress(String address) {
        final String protocol = "redis://";
        final String sslProtocol = "rediss://";
        if (address.startsWith(protocol) || address.startsWith(sslProtocol)) {
            return address;
        }
        return protocol + address;
    }

}
