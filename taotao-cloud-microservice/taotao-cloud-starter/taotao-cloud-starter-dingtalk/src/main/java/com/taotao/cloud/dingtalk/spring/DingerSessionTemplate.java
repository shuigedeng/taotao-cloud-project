/*
 * Copyright (c) ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.spring;

import com.taotao.cloud.dingtalk.session.DingerSession;
import com.taotao.cloud.dingtalk.session.DingerSessionFactory;
import com.taotao.cloud.dingtalk.session.SessionConfiguration;
import org.springframework.beans.factory.DisposableBean;

/**
 * DingerSessionTemplate
 *

 * @version 2022.03
 */
public class DingerSessionTemplate implements DingerSession, DisposableBean {

	private final DingerSessionFactory dingerSessionFactory;
	private final DingerSession dingerSession;
	private final SessionConfiguration sessionConfiguration;

	public DingerSessionTemplate(DingerSessionFactory dingerSessionFactory) {
		this.dingerSessionFactory = dingerSessionFactory;
		this.dingerSession = dingerSessionFactory.dingerSession();
		this.sessionConfiguration = dingerSessionFactory.getConfiguration();
	}

	@Override
	public <T> T getDinger(Class<T> type) {
		return dingerSession.getDinger(type);
	}

	@Override
	public SessionConfiguration configuration() {
		return sessionConfiguration;
	}

	@Override
	public void destroy() {

	}


	public DingerSessionFactory getDingerSessionFactory() {
		return dingerSessionFactory;
	}
}
