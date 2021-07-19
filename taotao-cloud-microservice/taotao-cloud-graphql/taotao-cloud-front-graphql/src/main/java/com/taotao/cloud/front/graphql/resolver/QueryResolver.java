/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.front.graphql.resolver;

import com.taotao.cloud.front.graphql.entity.Article;
import com.taotao.cloud.front.graphql.entity.User;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Resolver中的方法，入参及返回值类型，必须和graphqls文件中定义的一致，否则启动报错
 *
 * @author shuigedeng
 * @since 2020/11/9 09:54
 * @version 1.0.0
 */
@Component
public class QueryResolver implements GraphQLQueryResolver {

	private static final Logger logger = LogManager.getLogger(QueryResolver.class);

	// @Resource
	// private UserService userService;

	public User user(String nickname) {
		logger.info("Query Resolver ==> user");
		logger.info("params: nickname:{}", nickname);
		User user = new User();
		user.setId("1");
		user.setNickname("addUserByInput");
		return user;
		// return userService.getUserByNickname(nickname);
	}

	public List<User> users() {
		logger.info("Query Resolver ==> users");
		User user = new User();
		user.setId("1");
		user.setNickname("users");

		List<User> users = new ArrayList<>();
		users.add(user);
		return users;

		// return userService.listUsers();
	}

	public Article article(String title) {
		return new Article();
	}


}
