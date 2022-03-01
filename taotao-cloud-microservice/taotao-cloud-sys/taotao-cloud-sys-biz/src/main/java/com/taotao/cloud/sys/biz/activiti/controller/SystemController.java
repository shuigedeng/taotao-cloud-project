package com.taotao.cloud.sys.biz.activiti.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import boot.spring.pagemodel.DataGrid;
import boot.spring.pagemodel.Userinfo;
import boot.spring.po.Permission;
import boot.spring.po.Role;
import boot.spring.po.User;
import boot.spring.po.UserRole;
import boot.spring.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "系统管理接口")
@Controller
public class SystemController {
	@Autowired
	SystemService systemservice;
	
	@RequestMapping(value="/useradmin",method=RequestMethod.GET)
	String useradmin(){
		return "system/useradmin";
	}
	
	@RequestMapping(value="/roleadmin",method=RequestMethod.GET)
	String roleadmin(){
		return "system/roleadmin";
	}
	
	@RequestMapping(value="/permissionadmin",method=RequestMethod.GET)
	String permissionadmin(){
		return "system/permissionadmin";
	}
	
	@ApiOperation("获取用户列表")
	@RequestMapping(value="/userlist",method=RequestMethod.GET)
	@ResponseBody
	DataGrid<Userinfo> userlist(@RequestParam("current") int current,@RequestParam("rowCount") int rowCount){
		int total=systemservice.getallusers().size();
		List<User> userlist=systemservice.getpageusers(current,rowCount);
		List<Userinfo> users=new ArrayList<Userinfo>();
		for(User user:userlist){
			Userinfo u=new Userinfo();
			int userid = user.getUid();
			u.setId(userid);
			u.setAge(user.getAge());
			u.setPassword(user.getPassword());
			u.setTel(user.getTel());
			u.setUsername(user.getUsername());
			String rolename="";
			List<UserRole> ur = systemservice.listRolesByUserid(userid);
			if( ur != null && ur.size() > 0 ){
				for( UserRole userole : ur ){
					int roleid = userole.getRoleid();
					Role r = systemservice.getRolebyid(roleid);
					rolename=rolename+","+r.getRolename();
				}
				if(rolename.length()>0)
					rolename=rolename.substring(1,rolename.length());
				u.setRolelist(rolename);
			}
			users.add(u);
		}
		DataGrid<Userinfo> grid=new DataGrid<Userinfo>();
		grid.setCurrent(current);
		grid.setRows(users);
		grid.setRowCount(rowCount);
		grid.setTotal(total);
		return grid;
	}
	
	@ApiOperation("获取一个用户信息")
	@RequestMapping(value="/user/{uid}",method=RequestMethod.GET)
	@ResponseBody
	User getuserinfo(@PathVariable("uid") int userid){
		return systemservice.getUserByid(userid);
	}
	
	@ApiOperation("获取角色列表")
	@RequestMapping(value="/rolelist",method=RequestMethod.GET)
	@ResponseBody
	List<Role> getroles(){
		return systemservice.getRoles();
	}
	
	@ApiOperation("分页获取角色列表")
	@RequestMapping(value="/roles",method=RequestMethod.GET)
	@ResponseBody
	DataGrid<Role> getallroles(@RequestParam("current") int current,@RequestParam("rowCount") int rowCount){
		List<Role> roles=systemservice.getRoleinfo();
		int from = (current-1) * rowCount;
		int end = current * rowCount;
		List<Role> rows ;
		if (end >= roles.size()) {
			rows = roles.subList(from, roles.size());
		} else {
			rows = roles.subList(from, end);
		}
		DataGrid<Role> grid=new DataGrid<Role>();
		grid.setCurrent(current);
		grid.setRowCount(rowCount);
		grid.setTotal(roles.size());
		grid.setRows(rows);
		return grid;
	}
	
	@ApiOperation("删除一个用户")
	@RequestMapping(value="/deleteuser/{uid}",method=RequestMethod.GET)
	String deleteuser(@PathVariable("uid")int uid){
		systemservice.deleteuser(uid);
		return "system/useradmin";
	}
	
	@ApiOperation("添加一个用户")
	@RequestMapping(value="/adduser",method=RequestMethod.GET)
	String adduser(@ModelAttribute("user")User user,@RequestParam(value="rolename[]",required = false)String[] rolename){
		if(rolename==null)
			systemservice.adduser(user);
		else
			systemservice.adduser(user, rolename);
		return "system/useradmin";
	}
	
	@ApiOperation("修改一个用户")
	@RequestMapping(value="/updateuser/{uid}",method=RequestMethod.POST)
	String updateuser(@PathVariable("uid")int uid,@ModelAttribute("user")User user,@RequestParam(value="rolename[]",required = false)String[] rolename){
		systemservice.updateuser(uid, user, rolename);
		return "system/useradmin";
	}
	
	@ApiOperation("获取权限列表")
	@RequestMapping(value="permissionlist",method=RequestMethod.GET)
	@ResponseBody
	List<Permission> getPermisions(){
		return systemservice.getPermisions();
	}
	
	@ApiOperation("添加一个角色")
	@RequestMapping(value="/addrole",method=RequestMethod.GET)
	String addrole(@RequestParam("rolename") String rolename,@RequestParam(value="permissionname[]")String[] permissionname){
		Role r=new Role();
		r.setRolename(rolename);
		systemservice.addrole(r, permissionname);
		return "system/roleadmin";
	}
	
	@ApiOperation("删除一个角色")
	@RequestMapping(value="/deleterole/{rid}",method=RequestMethod.GET)
	String deleterole(@PathVariable("rid")int rid){
		systemservice.deleterole(rid);
		return "system/roleadmin";
	}
	
	@ApiOperation("获取一个角色信息")
	@RequestMapping(value="roleinfo/{rid}",method=RequestMethod.GET)
	@ResponseBody
	Role getRolebyrid(@PathVariable("rid")int rid){
		return systemservice.getRolebyid(rid);
	}
	
	@ApiOperation("修改一个角色信息")
	@RequestMapping(value="updaterole/{rid}",method=RequestMethod.POST)
	String updaterole(@PathVariable("rid")int rid,@RequestParam(value="permissionname[]")String[] permissionnames){
		systemservice.deleterolepermission(rid);
		systemservice.updaterole(rid, permissionnames);
		return "system/roleadmin";
	}
	
	@ApiOperation("获取权限列表")
	@RequestMapping(value="permissions",method=RequestMethod.GET)
	@ResponseBody
	DataGrid<Permission> getpermissions(@RequestParam("current") int current,@RequestParam("rowCount") int rowCount){
		List<Permission> p=systemservice.getPermisions();
		List<Permission> list=systemservice.getPagePermisions(current, rowCount);
		DataGrid<Permission> grid=new DataGrid<Permission>();
		grid.setCurrent(current);
		grid.setRowCount(rowCount);
		grid.setTotal(p.size());
		grid.setRows(list);
		return grid;
	}
	
	@ApiOperation("添加一个权限")
	@RequestMapping(value="addpermission",method=RequestMethod.POST)
	String addpermission(@RequestParam("permissionname") String permissionname){
		systemservice.addPermission(permissionname);
		return "system/permissionadmin";
	}
	
	@ApiOperation("删除一个权限")
	@RequestMapping(value="deletepermission/{pid}",method=RequestMethod.GET)
	String deletepermission(@PathVariable("pid") int pid){
		systemservice.deletepermission(pid);
		return "system/permissionadmin";
	}
	
	@ApiOperation("列出所有用户")
	@RequestMapping(value = "listUsers", method = RequestMethod.GET)
	@ResponseBody
	public List<User> listUsers() {
		List<User> users = systemservice.getallusers();
		return users;
	}
}
