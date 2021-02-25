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
package com.taotao.cloud.front.graphql.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.InputArgument;
import com.sun.tools.javac.util.List;
import com.taotao.cloud.front.graphql.dataloaders.ReviewsDataLoader;
import com.taotao.cloud.front.graphql.services.DefaultReviewsService;
import com.taotao.cloud.front.graphql.services.Review;
import com.taotao.cloud.front.graphql.services.Show;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.dataloader.DataLoader;
import org.reactivestreams.Publisher;

/**
 * ReviewsDataFetcher
 *
 * @author dengtao
 * @version v1.0
 * @since 2021/02/19 13:43
 */
@DgsComponent
public class ReviewsDataFetcher {

	private final DefaultReviewsService reviewsService;

	public ReviewsDataFetcher(DefaultReviewsService reviewsService) {
		this.reviewsService = reviewsService;
	}

	/**
	 * This datafetcher will be called to resolve the "reviews" field on a Show.
	 * It's invoked for each individual Show, so if we would load 10 shows, this method gets called 10 times.
	 * To avoid the N+1 problem this datafetcher uses a DataLoader.
	 * Although the DataLoader is called for each individual show ID, it will batch up the actual loading to a single method call to the "load" method in the ReviewsDataLoader.
	 * For this to work correctly, the datafetcher needs to return a CompletableFuture.
	 */
	@DgsData(parentType = DgsConstants.SHOW.TYPE_NAME, field = DgsConstants.SHOW.Reviews)
	public CompletableFuture<List<Review>> reviews(DgsDataFetchingEnvironment dfe) {
		//Instead of loading a DataLoader by name, we can use the DgsDataFetchingEnvironment and pass in the DataLoader classname.
		DataLoader<Integer, List<Review>> reviewsDataLoader = dfe.getDataLoader(ReviewsDataLoader.class);

		//Because the reviews field is on Show, the getSource() method will return the Show instance.
		Show show = dfe.getSource();

		//Load the reviews from the DataLoader. This call is async and will be batched by the DataLoader mechanism.
		return reviewsDataLoader.load(show.getId());
	}

	@DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.AddReview)
	public List<Review> addReview(@InputArgument("review")SubmittedReview reviewInput) {
		reviewsService.saveReview(reviewInput);

		List<Review> reviews = reviewsService.reviewsForShow(reviewInput.getShowId());

		return Objects.requireNonNullElseGet(reviews, List::of);
	}

	@DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.AddReviews)
	public List<Review> addReviews(@InputArgument(value = "reviews", collectionType=SubmittedReview.class) List<SubmittedReview> reviewsInput) {
		reviewsService.saveReviews(reviewsInput);

		List<Integer> showIds = reviewsInput.stream().map( review -> review.getShowId() ).collect(
			Collectors.toList());
		Map<Integer, List<Review>> reviews = reviewsService.reviewsForShows(showIds);

		return new ArrayList(reviews.values());
	}

	@DgsData(parentType = DgsConstants.SUBSCRIPTION_TYPE, field = DgsConstants.SUBSCRIPTION.ReviewAdded)
	public Publisher<Review> reviewAddedSubscription(@InputArgument("showId") Integer showId) {
		return reviewsService.getReviewsPublisher();
	}
}
