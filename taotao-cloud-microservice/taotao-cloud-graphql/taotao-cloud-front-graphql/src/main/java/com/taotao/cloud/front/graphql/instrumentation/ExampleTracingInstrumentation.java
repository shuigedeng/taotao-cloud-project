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
package com.taotao.cloud.front.graphql.instrumentation;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ExampleTracingInstrumentation
 *
 * @author shuigedeng
 * @version v1.0
 * @since 2021/02/19 13:45
 */
@Component
public class ExampleTracingInstrumentation extends SimpleInstrumentation {

	private final static Logger LOGGER = LoggerFactory.getLogger(
		ExampleTracingInstrumentation.class);

	@Override
	public InstrumentationState createState() {
		return new TracingState();
	}

	@Override
	public InstrumentationContext<ExecutionResult> beginExecution(
		InstrumentationExecutionParameters parameters) {
		TracingState tracingState = parameters.getInstrumentationState();
		tracingState.startTime = System.currentTimeMillis();
		return super.beginExecution(parameters);
	}

	@Override
	public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher,
		InstrumentationFieldFetchParameters parameters) {
		// We only care about user code
		if (parameters.isTrivialDataFetcher()) {
			return dataFetcher;
		}

		return environment -> {
			long startTime = System.currentTimeMillis();
			Object result = dataFetcher.get(environment);
			if (result instanceof CompletableFuture) {
				((CompletableFuture<?>) result).whenComplete((r, ex) -> {
					long totalTime = System.currentTimeMillis() - startTime;
					LOGGER.info("Async datafetcher {} took {}ms", findDatafetcherTag(parameters),
						totalTime);
				});
			} else {
				long totalTime = System.currentTimeMillis() - startTime;
				LOGGER.info("Datafetcher {} took {}ms", findDatafetcherTag(parameters), totalTime);
			}

			return result;
		};
	}

	@Override
	public CompletableFuture<ExecutionResult> instrumentExecutionResult(
		ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
		TracingState tracingState = parameters.getInstrumentationState();
		long totalTime = System.currentTimeMillis() - tracingState.startTime;
		LOGGER.info("Total execution time: {}ms", totalTime);

		return super.instrumentExecutionResult(executionResult, parameters);
	}

	private String findDatafetcherTag(InstrumentationFieldFetchParameters parameters) {
		GraphQLOutputType type = parameters.getExecutionStepInfo().getParent().getType();
		GraphQLObjectType parent;
		if (type instanceof GraphQLNonNull) {
			parent = (GraphQLObjectType) ((GraphQLNonNull) type).getWrappedType();
		} else {
			parent = (GraphQLObjectType) type;
		}

		return parent.getName() + "." + parameters.getExecutionStepInfo().getPath()
			.getSegmentName();
	}

	static class TracingState implements InstrumentationState {

		long startTime;
	}
}
