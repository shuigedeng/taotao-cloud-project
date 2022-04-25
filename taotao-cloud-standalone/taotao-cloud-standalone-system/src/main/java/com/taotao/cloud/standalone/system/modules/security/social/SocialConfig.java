package com.taotao.cloud.standalone.system.modules.security.social;


import com.taotao.cloud.standalone.system.modules.security.properties.PreSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * @Classname SocialConfig
 * @Description
 * @Author shuigedeng
 * @since 2019-07-08 21:49
 * 
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PreSecurityProperties preSecurityProperties;

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        PreJdbcUsersConnectionRepository repository = new PreJdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator, Encryptors.noOpText());
        repository.setTablePrefix("social_");
        return repository;
    }

    @Bean
    public SpringSocialConfigurer mySpringSocialSecurityConfig() {
        String filterProcessesUrl = preSecurityProperties.getSocial().getFilterProcessesUrl();
        PreSpringSocialConfigurer configurer = new PreSpringSocialConfigurer(filterProcessesUrl);
        //1、认证失败跳转注册页面
        // 跳转到signUp controller，从session中获取用户信息并通过生成的uuid保存到redis里面，然后跳转bind页面
        // 前端绑定后发送用户信息到后台bind controller，1）保存到自己系统用户；2）保存一份userconnection表数据，Spring Social通过这里面表数据进行判断是否绑定
        configurer.signupUrl("/signUp");
        //2、认证成功跳转后处理器，跳转带token的成功页面
//        configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);

        return configurer;
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }


    /**
     * 从Session中获取社交账号信息
     * @param connectionFactoryLocator
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator, getUsersConnectionRepository(connectionFactoryLocator)) {};
    }
}
