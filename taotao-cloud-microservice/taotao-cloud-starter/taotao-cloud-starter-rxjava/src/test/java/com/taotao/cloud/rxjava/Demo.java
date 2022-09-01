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
package com.taotao.cloud.rxjava;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.reactivex.Observable;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demonstrates usage of this component.
 *
 * @author Jakub Narloch
 */
public class Demo {

	@Value("${local.server.port}")
	private int port = 0;

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Configuration
	@EnableAutoConfiguration
	@RestController
	protected static class InvoiceResource {

		@RequestMapping(method = RequestMethod.GET, value = "/invoices", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		public Observable<Invoice> getInvoices() {

			return Observable.just(
				new Invoice("Acme", new Date()),
				new Invoice("Oceanic", new Date())
			);
		}
	}

	@Test
	public void shouldRetrieveInvoices() {

		// when
		ResponseEntity<List<Invoice>> response = restTemplate.exchange(path("/invoices"),
			HttpMethod.GET, null, new ParameterizedTypeReference<List<Invoice>>() {
			});

		// then
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Acme", response.getBody().get(0).getTitle());
	}

	private String path(String context) {
		return String.format("http://localhost:%d%s", port, context);
	}

	private static class Invoice {

		private final String title;

		private final Date issueDate;

		@JsonCreator
		public Invoice(@JsonProperty("title") String title,
			@JsonProperty("issueDate") Date issueDate) {
			this.title = title;
			this.issueDate = issueDate;
		}

		public String getTitle() {
			return title;
		}

		public Date getIssueDate() {
			return issueDate;
		}
	}
}
