/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.channel.jpush;


/**
 * 发送返回结果
 *
 * @author shuigedeng
 */
public class Result {

    private ErrorInfo error;

	public ErrorInfo getError() {
		return error;
	}

	public void setError(ErrorInfo error) {
		this.error = error;
	}

	public static final class ErrorInfo {

        private String code;

        private String message;


	    public String getCode() {
		    return code;
	    }

	    public void setCode(String code) {
		    this.code = code;
	    }

	    public String getMessage() {
		    return message;
	    }

	    public void setMessage(String message) {
		    this.message = message;
	    }
    }
}
