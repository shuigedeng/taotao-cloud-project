package com.taotao.cloud.rpc.base;

import com.taotao.cloud.rpc.base.pojo.BlogJSONResult;

public interface HelloWorldService {

	BlogJSONResult sayHello(String message);
}
