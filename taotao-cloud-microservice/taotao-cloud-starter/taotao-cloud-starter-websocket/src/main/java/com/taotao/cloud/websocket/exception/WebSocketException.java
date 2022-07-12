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
package com.taotao.cloud.websocket.exception;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;

/**
 * <p>Description: WebSocket Exception  </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 11:02:10
 */
public class WebSocketException extends BaseException {

	public WebSocketException(String message) {
		super(message);
	}

	public WebSocketException(Integer code, String message) {
		super(code, message);
	}

	public WebSocketException(Throwable e) {
		super(e);
	}

	public WebSocketException(String message, Throwable e) {
		super(message, e);
	}

	public WebSocketException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public WebSocketException(ResultEnum result) {
		super(result);
	}

	public WebSocketException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
