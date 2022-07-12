package com.taotao.cloud.wechat.biz.miniapp;

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
@SpringBootApplication
public class WxMiniappDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(WxMiniappDemoApplication.class, args);
    }

    @Autowired
    private WxMaService maService;

    @GetMapping("/test")
    public String test() throws WxErrorException {
        return  this.maService.getAccessToken();
    }
}
