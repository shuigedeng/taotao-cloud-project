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
package com.taotao.cloud.front.graphql.services;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * ShowsServiceImpl
 *
 * @author shuigedeng
 *
 * @version v1.0
 * @since 2021/02/19 13:49
 */
@Service
public class ShowsServiceImpl implements ShowsService {
	@Override
	public List<Show> shows() {
		return List.of(
			//Show.newBuilder().id(1).title("Stranger Things").releaseYear(2016).build(),
			//Show.newBuilder().id(2).title("Ozark").releaseYear(2017).build(),
			//Show.newBuilder().id(3).title("The Crown").releaseYear(2016).build(),
			//Show.newBuilder().id(4).title("Dead to Me").releaseYear(2019).build(),
			//Show.newBuilder().id(5).title("Orange is the New Black").releaseYear(2013).build()
		);
	}
}
