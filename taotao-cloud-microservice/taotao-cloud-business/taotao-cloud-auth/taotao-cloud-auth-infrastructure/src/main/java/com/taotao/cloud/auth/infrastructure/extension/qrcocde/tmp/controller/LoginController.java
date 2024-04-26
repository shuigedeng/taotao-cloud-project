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

package com.taotao.cloud.auth.infrastructure.extension.qrcocde.tmp.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.entity.Response;
import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.service.LoginService;
import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.service.UserService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/login")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @RequestMapping(path = "/getQrCodeImg", method = RequestMethod.GET)
    public String createQrCodeImg(Model model) {
        String uuid = loginService.createQrImg();
        String qrCode =
                Base64.encodeBase64String(QrCodeUtil.generatePng("http://127.0.0.1:8080/login/uuid=" + uuid, 300, 300));

        model.addAttribute("uuid", uuid);
        model.addAttribute("QrCode", qrCode);

        return "login";
    }

    @RequestMapping(path = "/getQrCodeStatus", method = RequestMethod.GET)
    @ResponseBody
    public Response getQrCodeStatus(@RequestParam String uuid, @RequestParam int currentStatus)
            throws InterruptedException {
        JSONObject data = loginService.getQrCodeStatus(uuid, currentStatus);
        return Response.createResponse(null, data);
    }

    /*
    以下两个方法用来模拟手机端的操作
     */
    @RequestMapping(path = "/scan", method = RequestMethod.POST)
    @ResponseBody
    public Response scanQrCodeImg(@RequestParam String uuid) {
        JSONObject data = loginService.scanQrCodeImg(uuid);
        if (data.getBoolean("valid")) {
            return Response.createResponse("扫码成功", data);
        }
        return Response.createErrorResponse("二维码已失效");
    }

    @RequestMapping(path = "/confirm", method = RequestMethod.POST)
    @ResponseBody
    public Response confirmLogin(@RequestParam String uuid) {
        boolean logged = loginService.confirmLogin(uuid);
        String msg = logged ? "登录成功!" : "二维码已过期!";
        return Response.createResponse(msg, logged);
    }
}
