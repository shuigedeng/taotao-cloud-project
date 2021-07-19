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
package com.taotao.cloud.backend.graphql.resolver;

import com.taotao.cloud.backend.graphql.entity.AddUserInput;
import com.taotao.cloud.backend.graphql.entity.Result;
import com.taotao.cloud.backend.graphql.entity.User;
import com.taotao.cloud.backend.graphql.entity.Article;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Resolver中的方法，入参及返回值类型，必须和graphqls文件中定义的一致，否则启动报错
 *
 * @author shuigedeng
 * @since 2020/11/9 09:55
 * @version 1.0.0
 */
@Component
public class MutationResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

	private static final Logger logger = LogManager.getLogger(MutationResolver.class);

	// @Resource
	// private UserService userService;

	public Result addUser(String mail, String nickname, String password, String description) {
		logger.info("Mutation Resolver ==> addUser");
		logger.info("params: mail:{}, nickname:{}, password:{}, description:{}",
			mail, nickname, password, description);
		return new Result(200, "addUser success");
		//return userService.addUser(mail, nickname, password, description);
	}

	public Result deleteUser(String id) {
		// if (StringUtils.isAnyBlank(id)) {
		// 	return ResponseBuilder.getRespCodeMsg(-101, "参数缺失");
		// }
		logger.info("Mutation Resolver ==> deleteUser");
		logger.info("params: id:{}", id);
		return new Result(200, "deleteUser success");
		//return userService.deleteUser(id);
	}

	public User updateUser(String id, String mail, String nickname, String description) {
		logger.info("Mutation Resolver ==> updateUser");
		logger.info("params: id:{}, mail:{}, nickname:{}, description:{}",
			id, mail, nickname, description);
		User user = new User();
		user.setId("1");
		user.setNickname("hello");
		return user;
		//return userService.updateUser(id, mail, nickname, description);
	}

	public User addUserByInput(AddUserInput addUserInput) {
		logger.info("Mutation Resolver ==> addUserByInput");
		logger.info("params: {}", addUserInput);
		User user = new User();
		user.setId("1");
		user.setNickname("addUserByInput");
		return user;
		//return userService.addUserInput(addUserInput);
	}

	public User registerUser(String mail, String nickname, String password){
		return new User();
	}

	public Article addArticle(String title, String content, String authorId) {
		return new Article();
	}
}
