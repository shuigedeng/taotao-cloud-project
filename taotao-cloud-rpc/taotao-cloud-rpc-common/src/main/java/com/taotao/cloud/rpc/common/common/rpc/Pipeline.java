package com.taotao.cloud.rpc.common.common.rpc;

import java.util.List;

public interface Pipeline<T> {

	List<T> list();

	void addLast(T first);
}
