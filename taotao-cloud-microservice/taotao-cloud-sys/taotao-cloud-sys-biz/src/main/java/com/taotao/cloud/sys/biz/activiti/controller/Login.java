package com.taotao.cloud.sys.biz.activiti.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import boot.spring.pagemodel.MSG;
import boot.spring.po.Role;
import boot.spring.po.Role_permission;
import boot.spring.po.User;
import boot.spring.po.User_role;
import boot.spring.service.LoginService;
import boot.spring.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(value = "登录登出接口")
@Controller
public class Login {
	@Autowired
	LoginService loginservice;
	
	@Autowired
	SystemService systemservice;
	
	@ApiOperation("登录认证")
	@RequestMapping(value="/loginvalidate",method = RequestMethod.POST)
	public String loginvalidate(@RequestParam("username") String username,@RequestParam("password") String pwd,HttpSession httpSession){
		if(username==null)
			return "login";
		String realpwd=loginservice.getpwdbyname(username);
		if(realpwd!=null&&pwd.equals(realpwd))
		{
			httpSession.setAttribute("username", username);
			return "index";
		}else
			return "fail";
	}
	
	@RequestMapping(value="/login",method = RequestMethod.GET)
	public String login(){
		return "login";
	}
	
	@RequestMapping(value="/logout",method = RequestMethod.GET)
	public String logout(HttpSession httpSession){
		httpSession.removeAttribute("username");
		return "login";
	}
	
	@ApiOperation("获取当前登录用户信息")
	@RequestMapping(value="/currentuser",method = RequestMethod.GET)
	@ResponseBody
	public MSG currentuser(HttpSession httpSession){
		String userid=(String) httpSession.getAttribute("username");
		return new MSG(userid);
	}	
	
	@ApiOperation("获取当前用户的权限列表")
	@RequestMapping(value="/currentuserpermission",method = RequestMethod.GET)
	@ResponseBody
	public List<String> currentuserpermission(HttpSession httpSession){
		String username=(String) httpSession.getAttribute("username");
		int uid = systemservice.getUidByusername(username);
		User user = systemservice.getUserByid(uid);
		List<User_role> roles = user.getUser_roles();
		List<String> list = new ArrayList<>();
		for (User_role ur : roles) {
			int rid = ur.getRole().getRid();
			Role role = systemservice.getRolebyid(rid);
			List<Role_permission> rps = role.getRole_permission();
			for (Role_permission rp : rps) {
				list.add(rp.getPermission().getPermissionname());
			}
		}
		return list;
	}
}
