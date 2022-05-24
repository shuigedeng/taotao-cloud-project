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
package com.taotao.cloud.rxjava.async;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.taotao.cloud.rxjava.dto.EventDto;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tests the {@link ObservableDeferredResult} class.
 *
 * @author Jakub Narloch
 */
public class ObservableDeferredResultTest {

	@Value("${local.server.port}")
	private int port = 0;

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@AutoConfiguration
	@EnableAutoConfiguration
	@RestController
	protected static class Application {

		@RequestMapping(method = RequestMethod.GET, value = "/empty")
		public ObservableDeferredResult<String> empty() {
			return new ObservableDeferredResult<String>(Observable.<String>empty());
		}

		@RequestMapping(method = RequestMethod.GET, value = "/single")
		public ObservableDeferredResult<String> single() {
			return new ObservableDeferredResult<String>(Observable.just("single value"));
		}

		@RequestMapping(method = RequestMethod.GET, value = "/multiple")
		public ObservableDeferredResult<String> multiple() {
			return new ObservableDeferredResult<String>(Observable.just("multiple", "values"));
		}

		@RequestMapping(method = RequestMethod.GET, value = "/event", produces = APPLICATION_JSON_UTF8_VALUE)
		public ObservableDeferredResult<EventDto> event() {
			return new ObservableDeferredResult<EventDto>(
				Observable.just(
					new EventDto("Spring.io", new Date()),
					new EventDto("JavaOne", new Date())
				)
			);
		}

		@RequestMapping(method = RequestMethod.GET, value = "/throw")
		public ObservableDeferredResult<Object> error() {
			return new ObservableDeferredResult<Object>(
				Observable.error(new RuntimeException("Unexpected")));
		}

		@RequestMapping(method = RequestMethod.GET, value = "/timeout")
		public ObservableDeferredResult<String> timeout() {
			return new ObservableDeferredResult<String>(
				Observable.timer(1, TimeUnit.MINUTES).map(new Function<Long, String>() {
					@Override
					public String apply(Long aLong) {
						return "single value";
					}
				}));
		}
	}

	@Test
	public void shouldRetrieveEmptyResponse() {

		// when
		ResponseEntity<List> response = restTemplate.getForEntity(path("/empty"), List.class);

		// then
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Collections.emptyList(), response.getBody());
	}

	@Test
	public void shouldRetrieveSingleValue() {

		// when
		ResponseEntity<List> response = restTemplate.getForEntity(path("/single"), List.class);

		// then
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Collections.singletonList("single value"), response.getBody());
	}

	@Test
	public void shouldRetrieveMultipleValues() {

		// when
		ResponseEntity<List> response = restTemplate.getForEntity(path("/multiple"), List.class);

		// then
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Arrays.asList("multiple", "values"), response.getBody());
	}

	@Test
	public void shouldRetrieveJsonSerializedListValues() {

		// when
		ResponseEntity<List<EventDto>> response = restTemplate.exchange(path("/event"),
			HttpMethod.GET, null,
			new ParameterizedTypeReference<List<EventDto>>() {
			});

		// then
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
		assertEquals("JavaOne", response.getBody().get(1).getName());
	}

	@Test
	public void shouldRetrieveErrorResponse() {

		// when
		ResponseEntity<Object> response = restTemplate.getForEntity(path("/throw"), Object.class);

		// then
		assertNotNull(response);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void shouldTimeoutOnConnection() {

		// when
		ResponseEntity<Object> response = restTemplate.getForEntity(path("/timeout"), Object.class);

		// then
		assertNotNull(response);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	private String path(String context) {
		return String.format("http://localhost:%d%s", port, context);
	}
}
