package com.taotao.cloud.standalone.system.modules.security.social;

import org.omg.CORBA.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

/**
 * @Classname SocialAutoConfigurerAdapter
 * @Description 不想引入1.5版本的springboot的话只能自己按照源码重写
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-08-06 14:45
 * @Version 1.0
 */
public abstract class SocialAutoConfigurerAdapter extends SocialConfigurerAdapter {
    public SocialAutoConfigurerAdapter() {
    }
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(this.createConnectionFactory());
    }
    protected abstract ConnectionFactory<?> createConnectionFactory();
}
