package com.taotao.cloud.wechat.biz.module.miniapp;

import cn.binarywang.wx.miniapp.api.WxMaService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author binary wang
 */
@RestController
@RequestMapping("/")
public class WxMiniappController {

    @Autowired
    private WxMaService maService;

    @GetMapping("/test")
    public String test() throws WxErrorException {
        return  this.maService.getAccessToken();
    }
}
