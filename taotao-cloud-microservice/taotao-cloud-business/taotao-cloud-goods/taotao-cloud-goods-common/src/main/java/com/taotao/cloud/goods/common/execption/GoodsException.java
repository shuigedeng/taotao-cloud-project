package com.taotao.cloud.goods.common.execption;

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;

public class GoodsException extends BusinessException {

	public GoodsException(String message) {
		super(message);
	}

	public GoodsException(Integer code, String message) {
		super(code, message);
	}

	public GoodsException(String message, Throwable e) {
		super(message, e);
	}

	public GoodsException(Throwable e) {
		super(e);
	}

	public GoodsException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public GoodsException(ResultEnum result) {
		super(result);
	}

	public GoodsException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
