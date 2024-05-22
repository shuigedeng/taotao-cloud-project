package com.taotao.cloud.rpc.common.common.rpc;

import java.util.List;

public class DefaultPipeline<T> implements Pipeline<T> {

	@Override
	public List<T> list() {
		return List.of();
	}

	@Override
	public void addLast(T first) {

	}
}
