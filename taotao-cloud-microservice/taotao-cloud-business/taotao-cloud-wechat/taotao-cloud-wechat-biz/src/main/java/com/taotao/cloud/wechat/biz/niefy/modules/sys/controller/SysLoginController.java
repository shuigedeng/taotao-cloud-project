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

package com.taotao.cloud.wechat.biz.niefy.modules.sys.controller;

import com.github.niefy.common.utils.R;
import com.github.niefy.modules.sys.entity.SysUserEntity;
import com.github.niefy.modules.sys.form.SysLoginForm;
import com.github.niefy.modules.sys.service.SysCaptchaService;
import com.github.niefy.modules.sys.service.SysUserService;
import com.github.niefy.modules.sys.service.SysUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录相关
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@Api(tags = {"系统登录-管理后台"})
public class SysLoginController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private SysCaptchaService sysCaptchaService;

    /** 验证码 */
    @GetMapping("captcha")
    @ApiOperation(value = "获取验证码", notes = "返回验证码图片")
    public void captcha(HttpServletResponse response, @ApiParam(value = "随意填，但每次不得重复", required = true) String uuid)
            throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        // 获取图片验证码
        BufferedImage image = sysCaptchaService.getCaptcha(uuid);

        try ( // try-with-resources 语法，自动关闭资源
        ServletOutputStream out = response.getOutputStream()) {
            ImageIO.write(image, "jpg", out);
        }
    }

    /** 登录 */
    @PostMapping("/sys/login")
    @ApiOperation(value = "登录", notes = "需先获取验证码")
    public Map<String, Object> login(@RequestBody SysLoginForm form) {
        boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
        if (!captcha) {
            return R.error("验证码不正确");
        }

        // 用户信息
        SysUserEntity user = sysUserService.queryByUserName(form.getUsername());

        // 账号不存在、密码错误
        if (user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
            return R.error("账号或密码不正确");
        }

        // 账号锁定
        if (user.getStatus() == 0) {
            return R.error("账号已被锁定,请联系管理员");
        }

        // 生成token，并保存到数据库
        return sysUserTokenService.createToken(user.getUserId());
    }

    /** 退出 */
    @PostMapping("/sys/logout")
    @ApiOperation(value = "退出登录", notes = "")
    public R logout() {
        sysUserTokenService.logout(getUserId());
        return R.ok();
    }
}
