// package com.taotao.cloud.uc.biz.controller;
//
// import cn.hutool.core.bean.BeanUtil;
// import cn.hutool.core.collection.CollectionUtil;
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.taotao.cloud.common.exception.BusinessException;
// import com.taotao.cloud.common.model.PageResult;
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.core.model.SecurityUser;
// import com.taotao.cloud.core.utils.AuthUtil;
// import com.taotao.cloud.core.utils.SecurityUtil;
// import com.taotao.cloud.log.annotation.SysOperateLog;
// import com.taotao.cloud.uc.api.dto.UserAddDTO;
// import com.taotao.cloud.uc.api.dto.UserRestPasswordDTO;
// import com.taotao.cloud.uc.api.dto.UserUpdateDTO;
// import com.taotao.cloud.uc.api.query.UserListQuery;
// import com.taotao.cloud.uc.api.vo.UserAddVO;
// import com.taotao.cloud.uc.biz.entity.SysUser;
// import com.taotao.cloud.uc.biz.service.ISysUserService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.web.bind.annotation.*;
//
// /**
//  * 用户管理API
//  *
//  * @author dengtao
//  * @date 2020/4/30 13:12
//  */
// @RestController
// @RequestMapping("/user")
// @Api(value = "用户管理API", tags = {"用户管理API"})
// public class SysUserController {
//
//     @Autowired
//     private ISysUserService userService;
//
//     @ApiOperation("保存用户包括角色和部门")
//     @SysOperateLog(description = "保存用户包括角色和部门")
//     @PreAuthorize("hasAuthority('sys:user:add')")
//     @PostMapping(value = "/save")
//     public Result<UserAddVO> saveUser(@RequestBody UserAddDTO userAddDto) {
//         return Result.succeed(userService.insertUser(userAddDto));
//     }
//
//     @ApiOperation("查询用户集合")
//     @SysOperateLog(description = "查询用户集合")
//     @PreAuthorize("hasAuthority('sys:user:view')")
//     @PostMapping(value = "/page")
//     public PageResult<SysUser> getUserList(@RequestBody UserListQuery userListQuery) {
//         IPage<SysUser> pageResult = userService.getUsersWithRolePage(userListQuery);
//         return PageResult.succeed(pageResult);
//     }
//
//     @ApiOperation("更新用户包括角色和部门")
//     @SysOperateLog(description = "更新用户包括角色和部门")
//     @PreAuthorize("hasAuthority('sys:user:update')")
//     @PostMapping(value = "/update")
//     public Result<Boolean> updateUser(@RequestBody UserUpdateDTO updateDTO) {
//         return Result.succeed(userService.updateUser(updateDTO));
//     }
//
//     @ApiOperation("根据用户id删除用户包括角色和部门")
//     @SysOperateLog(description = "根据用户id删除用户包括角色和部门")
//     @PreAuthorize("hasAuthority('sys:user:delete')")
//     @DeleteMapping("/{userId}")
//     public Result<Boolean> deleteUser(@PathVariable("userId") Integer userId) {
//         return Result.succeed(userService.removeUser(userId));
//     }
//
//     @ApiOperation("重置密码")
//     @SysOperateLog(description = "重置密码")
//     @PreAuthorize("hasAuthority('sys:user:rest:password')")
//     @PutMapping("/rest/password")
//     public Result<Boolean> restPass(@RequestBody UserRestPasswordDTO restPasswordDTO) {
//         return Result.succeed(userService.restPass(restPasswordDTO.getUserId(), restPasswordDTO.getNewPassword()));
//     }
//
//     @ApiOperation("获取当前登录人信息")
//     @GetMapping("/info")
//     public Result<SysUser> getUserInfo() {
//         return Result.succeed(userService.findUserInByName(SecurityUtil.getUser().getUsername()));
//     }
//
//     @ApiOperation("获取用户信息")
//     @SysOperateLog(description = "获取用户信息")
//     @GetMapping("/info/{userIdOrUserNameOrMobileOrEmail}")
//     public Result<SecurityUser> getUserInfo(@PathVariable String userIdOrUserNameOrMobileOrEmail) {
//         SysUser user = userService.findUserByUserIdOrUserNameOrMobile(userIdOrUserNameOrMobileOrEmail);
//         if (user == null) {
//             throw new BusinessException("未查询到用户数据");
//         }
//         return Result.succeed(userService.findUserInfo(user));
//     }
//
//     @ApiOperation("第三方登录调用获取用户信息")
//     @SysOperateLog(description = "第三方登录调用获取用户信息")
//     @GetMapping("/info/social")
//     public Result<SecurityUser> getUserInfoBySocial(@RequestParam(value = "providerId") String providerId,
//                                                     @RequestParam(value = "providerUserId") int providerUserId) {
//         SysUser sysUser = userService.getUserBySocial(providerId, providerUserId);
//         SecurityUser securityUser = new SecurityUser(sysUser.getId(), sysUser.getUsername(),
//                 sysUser.getPassword(), CollectionUtil.newHashSet(), CollectionUtil.newHashSet());
//
//         BeanUtil.copyProperties(sysUser, securityUser);
//         return Result.succeed(securityUser);
//     }
//
//     @ApiOperation("修改密码")
//     @SysOperateLog(description = "修改密码")
//     @PreAuthorize("hasAuthority('sys:user:update:password')")
//     @PostMapping("/update/password")
//     public Result<Boolean> updatePass(@RequestBody SysUser sysUser) {
//         // 校验密码
//         SysUser user = userService.findUserByUserIdOrUserNameOrMobile(sysUser.getUsername());
//         if (!AuthUtil.validatePass(sysUser.getPassword(), user.getPassword())) {
//             throw new BusinessException("原密码错误");
//         }
//         // 修改密码
//         SysUser userForPass = new SysUser();
//         userForPass.setId(user.getId());
//         BCryptPasswordEncoder passwordEncoder = AuthUtil.getPasswordEncoder();
//         userForPass.setPassword(passwordEncoder.encode(sysUser.getPassword()));
//         return Result.succeed(userService.updateUserInfo(userForPass));
//     }
// }
//
