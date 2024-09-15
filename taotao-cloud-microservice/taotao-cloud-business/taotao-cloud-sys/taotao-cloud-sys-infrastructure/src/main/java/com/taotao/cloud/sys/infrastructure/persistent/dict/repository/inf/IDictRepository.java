/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.sys.infrastructure.persistent.dict.repository.inf;

import com.taotao.cloud.sys.infrastructure.persistent.dict.po.DictPO;
import com.taotao.boot.web.base.repository.BaseInterfaceSuperRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.QueryRewriter;

/**
 * CompanyMapper
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/13 22:50
 */
public interface IDictRepository extends BaseInterfaceSuperRepository<DictPO, Long>, QueryRewriter {

	@Override
	default String rewrite(String query, Sort sort) {
//		return query.replaceAll("original_user_alias", "rewritten_user_alias");
		return query;
	}

	//Stream<T>您可以使用 Java 8作为返回类型来增量处理查询方法的结果。不是将查询结果包装在Stream，而是使用数据存储特定的方法来执行流处理。
	//Stream可能会包装底层数据存储特定的资源，因此必须在使用后关闭。您可以Stream使用close()方法或使用 Java 7try-with-resources块手动关闭，如下例所示：
	//前并非所有 Spring Data 模块都支持Stream<T>返回类型。spring官网提示

//	Stream<SysUser> findByNameLike(String name);

	// Plain query method
	//@Lock(LockModeType.READ)
	//List<User> findByLastname(String lastname);

	//您可以使用Spring 的异步方法运行功能来异步运行存储库查询。这意味着该方法在调用后立即返回，
	// 而实际查询发生在已提交给 Spring 的任务中TaskExecutor。异步查询与反应式查询不同，不应混合使用。
	// 有关反应式支持的更多详细信息，请参阅特定的文档。以下示例显示了许多异步查询：
//	@Async
//	Future<User> findByFirstname(String firstname);
//	@Async
//	CompletableFuture<User> findOneByFirstname(String firstname);
//	@Async
//	ListenableFuture<User> findOneByLastname(String lastname);

	//@Query(value = "select original_user_alias.* from SD_USER original_user_alias",
	//	nativeQuery = true,
	//	queryRewriter = MyRepository.class)
	//List<User> findByNativeQuery(String param);
//
	//@Query(value = "select original_user_alias from User original_user_alias",
	//	queryRewriter = MyRepository.class)
	//List<User> findByNonNativeQuery(String param);
//

}
