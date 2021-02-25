package com.taotao.cloud.standalone.system.modules.sys.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.standalone.common.constant.PreConstant;
import com.taotao.cloud.standalone.common.exception.PreBaseException;
import com.taotao.cloud.standalone.common.utils.R;
import com.taotao.cloud.standalone.log.annotation.SysOperaLog;
import com.taotao.cloud.standalone.security.util.SecurityUtil;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysUser;
import com.taotao.cloud.standalone.system.modules.sys.dto.UserDTO;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysUserService;
import com.taotao.cloud.standalone.system.modules.sys.util.EmailUtil;
import com.taotao.cloud.standalone.system.modules.sys.util.PreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private EmailUtil emailUtil;

    /**
     * 保存用户包括角色和部门
     *
     * @param userDto
     * @return
     */
    @SysOperaLog(descrption = "保存用户包括角色和部门")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:add')")
    public R insert(@RequestBody UserDTO userDto) {
        return R.ok(userService.insertUser(userDto));
    }


    /**
     * 获取用户列表集合
     *
     * @param page
     * @param userDTO
     * @return
     */
    @SysOperaLog(descrption = "查询用户集合")
    @GetMapping
    @PreAuthorize("hasAuthority('sys:user:view')")
    public R getList(Page page, UserDTO userDTO) {
        return R.ok(userService.getUsersWithRolePage(page, userDTO));
    }

    /**
     * 更新用户包括角色和部门
     *
     * @param userDto
     * @return
     */
    @SysOperaLog(descrption = "更新用户包括角色和部门")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:user:update')")
    public R update(@RequestBody UserDTO userDto) {
        return R.ok(userService.updateUser(userDto));
    }

    /**
     * 删除用户包括角色和部门
     *
     * @param userId
     * @return
     */
    @SysOperaLog(descrption = "根据用户id删除用户包括角色和部门")
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public R delete(@PathVariable("userId") Integer userId) {
        return R.ok(userService.removeUser(userId));
    }


    /**
     * 重置密码
     *
     * @param userId
     * @return
     */
    @SysOperaLog(descrption = "重置密码")
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('sys:user:rest')")
    public R restPass(@PathVariable("userId") Integer userId) {
        return R.ok(userService.restPass(userId));
    }


    /**
     * 获取个人信息
     *
     * @return
     */
    @SysOperaLog(descrption = "获取个人信息")
    @GetMapping("/info")
    public R getUserInfo() {
        return R.ok(userService.findByUserInfoName(SecurityUtil.getUser().getUsername()));
    }

    /**
     * 修改密码
     *
     * @return
     */
    @SysOperaLog(descrption = "修改密码")
    @PutMapping("updatePass")
    @PreAuthorize("hasAuthority('sys:user:updatePass')")
    public R updatePass(@RequestParam String oldPass, @RequestParam String newPass) {
        // 校验密码流程
        SysUser sysUser = userService.findSecurityUserByUser(new SysUser().setUsername(SecurityUtil.getUser().getUsername()));
        if (!PreUtil.validatePass(oldPass, sysUser.getPassword())) {
            throw new PreBaseException("原密码错误");
        }
        if (StrUtil.equals(oldPass, newPass)) {
            throw new PreBaseException("新密码不能与旧密码相同");
        }
        // 修改密码流程
        SysUser user = new SysUser();
        user.setUserId(sysUser.getUserId());
        user.setPassword(PreUtil.encode(newPass));
        return R.ok(userService.updateUserInfo(user));
    }

    /**
     * 检测用户名是否存在 避免重复
     *
     * @param userName
     * @return
     */
    @PostMapping("/vailUserName")
    public R vailUserName(@RequestParam String userName) {
        SysUser sysUser = userService.findSecurityUserByUser(new SysUser().setUsername(userName));
        return R.ok(ObjectUtil.isNull(sysUser));
    }

    /**
     * 发送邮箱验证码
     *
     * @param to
     * @param request
     * @return
     */
    @PostMapping("/sendMailCode")
    public R sendMailCode(@RequestParam String to, HttpServletRequest request) {
        emailUtil.sendSimpleMail(to, request);
        return R.ok();
    }

    /**
     * 修改密码
     *
     * @return
     */
    @SysOperaLog(descrption = "修改邮箱")
    @PutMapping("updateEmail")
    @PreAuthorize("hasAuthority('sys:user:updateEmail')")
    public R updateEmail(@RequestParam String mail, @RequestParam String code, @RequestParam String pass, HttpServletRequest request) {
        // 校验验证码流程
        String ccode = (String) request.getSession().getAttribute(PreConstant.RESET_MAIL);
        if (ObjectUtil.isNull(ccode)) {
            throw new PreBaseException("验证码已过期");
        }
        if (!StrUtil.equals(code.toLowerCase(), ccode)) {
            throw new PreBaseException("验证码错误");
        }
        // 校验密码流程
        SysUser sysUser = userService.findSecurityUserByUser(new SysUser().setUsername(SecurityUtil.getUser().getUsername()));
        if (!PreUtil.validatePass(pass, sysUser.getPassword())) {
            throw new PreBaseException("密码错误");
        }
        // 修改邮箱流程
        SysUser user = new SysUser();
        user.setUserId(sysUser.getUserId());
        user.setEmail(mail);
        return R.ok(userService.updateUserInfo(user));
    }



}

