package com.taotao.cloud.common.function;

@FunctionalInterface
public interface SupplierEx<T> {

	T get() throws Exception;
}
