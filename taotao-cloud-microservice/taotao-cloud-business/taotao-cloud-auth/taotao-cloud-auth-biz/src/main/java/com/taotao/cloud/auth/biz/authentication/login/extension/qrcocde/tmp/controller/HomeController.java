package com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.controller;

import org.springframework.web.bind.annotation.*;

public class HomeController {

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String home() {
        return "index";
    }

}
