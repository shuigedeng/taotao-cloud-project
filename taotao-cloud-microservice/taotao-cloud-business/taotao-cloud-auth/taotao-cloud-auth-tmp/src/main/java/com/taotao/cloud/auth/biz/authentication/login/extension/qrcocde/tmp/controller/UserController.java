package com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.controller;

import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.entity.Response;
import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.entity.User;
import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.service.UserService;
import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class UserController {

	@Autowired
    UserService userService;

	@Autowired
	private HostHolder hostHolder;

	@RequestMapping(path = "/getUser", method = RequestMethod.GET)
	@ResponseBody
	public Response getUser() {
		User user = hostHolder.getUser();
		if (user == null) {
			return Response.createErrorResponse("用户未登录");
		}
		return Response.createResponse(null, user);
	}
}
