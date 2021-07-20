package com.taotao.cloud.standalone.system.modules.security.social.github.config;

import com.taotao.cloud.standalone.system.modules.security.properties.GithubProperties;
import com.taotao.cloud.standalone.system.modules.security.properties.PreSecurityProperties;
import com.taotao.cloud.standalone.system.modules.security.social.github.connect.GitHubConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;

/**
 * @Classname GitHub1
 * @Description github 社交登录的自动配置
 * @Author shuigedeng
 * @since 2019-07-08 22:04
 * @Version 1.0
 */
@Configuration
@EnableSocial
@ConditionalOnProperty(prefix = "pre.security.social.github", name = "app-id")
public class GithubAutoAuthConfig extends SocialConfigurerAdapter {

    @Autowired
    private PreSecurityProperties preSecurityProperties;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer,Environment environment) {
        configurer.addConnectionFactory(createConnectionFactory());
    }
    public ConnectionFactory<?> createConnectionFactory() {
        GithubProperties github = preSecurityProperties.getSocial().getGithub();
        return new GitHubConnectionFactory(github.getProviderId(), github.getAppId(), github.getAppSecret());
    }
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }

}
