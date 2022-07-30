package com.taotao.cloud.wechat.biz.wechat.controller;

import cn.bootx.common.core.rest.Res;
import cn.bootx.common.core.rest.ResResult;
import cn.bootx.starter.wechat.core.notice.service.WeChatTemplateService;
import cn.bootx.starter.wechat.core.notice.service.WechatNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**   
* @author xxm
* @date 2022/7/16 
*/
@Tag(name = "微信模板消息")
@RestController
@RequestMapping("/wechat/template")
@RequiredArgsConstructor
public class WeChatTemplateController {
    private final WeChatTemplateService weChatTemplateService;
    private final WechatNoticeService wechatNoticeService;

    @Operation(summary = "查看模板列表")
    @GetMapping("/findAll")
    public ResResult<List<WxMpTemplate>> findAll(){
        return Res.ok(weChatTemplateService.findAll());
    }

    @Operation(summary = "发送信息")
    @PostMapping("/sendMsg")
    public ResResult<Void> sendMsg(){
        wechatNoticeService.sentNotice();
        return Res.ok();
    }

}
