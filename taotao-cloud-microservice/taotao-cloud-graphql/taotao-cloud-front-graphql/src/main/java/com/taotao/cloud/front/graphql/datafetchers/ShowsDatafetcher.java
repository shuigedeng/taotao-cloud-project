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
package com.taotao.cloud.front.graphql.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import com.taotao.cloud.front.graphql.services.Show;
import com.taotao.cloud.front.graphql.services.ShowsService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ShowsDatafetcher
 *
 * @author shuigedeng
 * @version v1.0
 * @since 2021/02/19 13:44
 */
@DgsComponent
public class ShowsDatafetcher {

	private final ShowsService showsService;

	public ShowsDatafetcher(ShowsService showsService) {
		this.showsService = showsService;
	}

	/**
	 * This datafetcher resolves the shows field on Query. It uses an @InputArgument to get the
	 * titleFilter from the Query if one is defined.
	 */
	@DgsData(parentType = "", field = "SubmittedReview")
	public List<Show> shows(@InputArgument("titleFilter") String titleFilter) {
		if (titleFilter == null) {
			return showsService.shows();
		}

		//return showsService.shows().stream().filter(s -> s.getTitle().contains(titleFilter))
		//	.collect(
		//		Collectors.toList());
		return null;
	}
}
