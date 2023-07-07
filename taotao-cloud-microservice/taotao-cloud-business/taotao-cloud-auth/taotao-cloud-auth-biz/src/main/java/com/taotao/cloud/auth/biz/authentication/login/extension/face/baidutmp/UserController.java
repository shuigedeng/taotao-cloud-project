//package com.taotao.cloud.auth.biz.authentication.login.extension.face.baidutmp;
//
//import com.alibaba.fastjson2.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.UUID;
//
//@Controller
//@RequestMapping("/user")
//public class UserController {
//
//	@Autowired
//	private FaceService faceService;
//
//	@RequestMapping("/test")
//	@ResponseBody
//	public String test() {
//		return "hello world";
//	}
//
//	/**
//	 * 人脸登录
//	 */
//	@RequestMapping("/login")
//	@ResponseBody
//	public JSONObject searchface(@RequestBody JSONObject jsonObject) {
//		StringBuffer imagebast64 = new StringBuffer(jsonObject.getString("imagebast64"));
//		String userId = faceService.loginByFace(imagebast64);
//		JSONObject res = new JSONObject();
//		res.put("userId", userId);
//		res.put("code", 200);
//		return res;
//	}
//
//	/**
//	 * 人脸注册
//	 */
//	@RequestMapping("/register")
//	@ResponseBody
//	public JSONObject registerFace(@RequestBody JSONObject jsonObject) {
//		StringBuffer imagebast64 = new StringBuffer(jsonObject.getString("imagebast64"));
//		String userId = UUID.randomUUID().toString().substring(0, 4);
//		Boolean registerFace = faceService.registerFace(userId, imagebast64);
//
//		JSONObject res = new JSONObject();
//		res.put("userId", userId);
//		if (registerFace) {
//			res.put("code", 200);
//		}
//		return res;
//	}
//}
