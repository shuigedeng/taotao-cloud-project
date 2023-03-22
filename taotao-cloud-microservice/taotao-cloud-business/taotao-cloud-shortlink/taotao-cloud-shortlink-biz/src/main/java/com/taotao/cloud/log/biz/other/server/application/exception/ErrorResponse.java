package com.taotao.cloud.log.biz.other.server.application.exception;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {

	static public ResponseEntity notFound() {
		return ResponseEntity.status(404).body(notFound(""));
	}

	static public String notFound(String string) {
		JSONObject result = new JSONObject();
		result.put("code", 404);
		result.put("errMsg", "No found");
		return result.toString();
	}

	static public String serverError(String string) {
		JSONObject result = new JSONObject();
		result.put("code", 500);
		result.put("errMsg", " server busy");
		return result.toString();
	}


}
