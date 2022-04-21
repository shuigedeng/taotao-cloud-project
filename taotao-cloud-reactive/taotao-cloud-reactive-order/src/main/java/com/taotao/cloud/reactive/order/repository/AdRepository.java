/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.reactive.order.repository;

import com.taotao.cloud.reactive.order.bean.AdBean;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * AdRepository
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/03/03 16:27
 */
public interface AdRepository extends ReactiveCrudRepository<AdBean,Integer> {

//	@Query("select * from message where id = ?")
//	Mono selectByMsgId(Long msgId);
//
//	@Modifying
//	@Query("delete from message where id = :msgId")
//	Mono deleteByMsgId(Long msgId);
}
