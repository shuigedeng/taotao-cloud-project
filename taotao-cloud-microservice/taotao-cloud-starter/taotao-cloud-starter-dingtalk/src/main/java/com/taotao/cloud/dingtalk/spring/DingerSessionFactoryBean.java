package com.taotao.cloud.dingtalk.spring;

import com.taotao.cloud.dingtalk.session.DefaultDingerSessionFactory;
import com.taotao.cloud.dingtalk.session.DingerSessionFactory;
import com.taotao.cloud.dingtalk.session.SessionConfiguration;
import java.io.IOException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * DingerSessionFactoryBean
 *

 * @version 1.2
 */
public class DingerSessionFactoryBean implements FactoryBean<DingerSessionFactory>,
	InitializingBean {

	private DingerSessionFactory dingerSessionFactory;
	private SessionConfiguration sessionConfiguration;

	@Override
	public DingerSessionFactory getObject() throws Exception {
		if (this.dingerSessionFactory == null) {
			afterPropertiesSet();
		}
		return dingerSessionFactory;
	}

	@Override
	public Class<?> getObjectType() {
		return this.dingerSessionFactory == null ? DingerSessionFactory.class
			: this.dingerSessionFactory.getClass();
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
		return new DefaultDingerSessionFactory(sessionConfiguration);
	}

	public void setConfiguration(SessionConfiguration sessionConfiguration) {
		this.sessionConfiguration = sessionConfiguration;
	}
}
