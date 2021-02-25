package com.taotao.cloud.standalone.system.modules.security.social.qq.config;

import com.taotao.cloud.standalone.system.modules.security.properties.PreSecurityProperties;
import com.taotao.cloud.standalone.system.modules.security.properties.QQProperties;
import com.taotao.cloud.standalone.system.modules.security.social.SocialAutoConfigurerAdapter;
import com.taotao.cloud.standalone.system.modules.security.social.qq.connection.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;

/**
 * 把QQ登录的自定义配置传给ConnectionFactory
 */

@Configuration
@EnableSocial
@ConditionalOnProperty(prefix = "pre.security.social.qq",name = "app-id")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private PreSecurityProperties preSecurityProperties;



    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(createConnectionFactory());
    }
    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        QQProperties qqProperties = preSecurityProperties.getSocial().getQq();
        String providerId = qqProperties.getProviderId();
        String appId = qqProperties.getAppId();
        String appSecret = qqProperties.getAppSecret();
        return new QQConnectionFactory(providerId, appId, appSecret);
    }
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }

}
