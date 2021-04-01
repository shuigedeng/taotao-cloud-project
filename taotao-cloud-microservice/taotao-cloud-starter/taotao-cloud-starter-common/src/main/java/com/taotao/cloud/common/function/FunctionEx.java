package com.taotao.cloud.common.function;

@FunctionalInterface
public interface FunctionEx<T,R> {

	R apply(T t) throws Exception;
}
