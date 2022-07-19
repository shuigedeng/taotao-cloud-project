package com.taotao.cloud.backend.graphql;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * @Controller
 * class MyController {
 *
 *     @QueryMapping
 *     Person person(@ContextValue String myHeader) {
 *             // ...
 *     }
 * }
 */
class HeaderInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        List<String> headerValue = request.getHeaders().get("myHeader");
        request.configureExecutionInput((executionInput, builder) ->
                builder.graphQLContext(Collections.singletonMap("myHeader", headerValue)).build());
        return chain.next(request);
    }

	public static class MyInterceptor implements WebGraphQlInterceptor {

		@Override
		public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
			return chain.next(request)
				.map(response -> {
					Object data = response.getData();
					Object updatedData = "sldfj" ;
					return response.transform(builder -> builder.data(updatedData));
				});
		}
	}
}

