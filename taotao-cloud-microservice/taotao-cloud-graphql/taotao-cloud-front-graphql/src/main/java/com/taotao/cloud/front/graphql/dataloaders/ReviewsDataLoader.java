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
package com.taotao.cloud.front.graphql.dataloaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.taotao.cloud.front.graphql.services.DefaultReviewsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.dataloader.MappedBatchLoader;

/**
 * ReviewsDataLoader
 *
 * @author dengtao
 * @version v1.0
 * @since 2021/02/19 13:44
 */
@DgsDataLoader(name = "reviews")
public class ReviewsDataLoader implements MappedBatchLoader<Integer, List<Review>> {
	private final DefaultReviewsService reviewsService;

	public ReviewsDataLoader(DefaultReviewsService reviewsService) {
		this.reviewsService = reviewsService;
	}

	/**
	 * This method will be called once, even if multiple datafetchers use the load() method on the DataLoader.
	 * This way reviews can be loaded for all the Shows in a single call instead of per individual Show.
	 */
	@Override
	public CompletionStage<Map<Integer, List<Review>>> load(Set<Integer> keys) {

		return CompletableFuture.supplyAsync(() -> reviewsService.reviewsForShows(new ArrayList<>(keys)));
	}
}
