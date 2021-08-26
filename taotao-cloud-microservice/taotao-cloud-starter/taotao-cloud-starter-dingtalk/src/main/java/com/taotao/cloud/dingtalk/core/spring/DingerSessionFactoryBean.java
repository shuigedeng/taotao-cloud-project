package com.taotao.cloud.dingtalk.core.spring;

import com.taotao.cloud.dingtalk.core.session.DingerSessionFactory;
import com.taotao.cloud.dingtalk.core.session.defaults.DefaultDingerSessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

/**
 * DingerSessionFactoryBean
 *
 * @author Jaemon
 * @version 1.2
 */
public class DingerSessionFactoryBean implements FactoryBean<DingerSessionFactory>, InitializingBean {
    private DingerSessionFactory dingerSessionFactory;
    private Configuration configuration;

    @Override
    public DingerSessionFactory getObject() throws Exception {
        if (this.dingerSessionFactory == null) {
            afterPropertiesSet();
        }
        return dingerSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return this.dingerSessionFactory == null ? DingerSessionFactory.class : this.dingerSessionFactory.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.dingerSessionFactory = buildDingerSessionFactory();
    }


    protected DingerSessionFactory buildDingerSessionFactory() throws IOException {
        return new DefaultDingerSessionFactory(configuration);
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
