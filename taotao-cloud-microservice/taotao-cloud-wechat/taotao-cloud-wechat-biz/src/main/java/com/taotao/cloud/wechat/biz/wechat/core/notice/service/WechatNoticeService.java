package com.taotao.cloud.wechat.biz.wechat.core.notice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
* 微信消息通知功能
* @author xxm  
* @date 2022/7/15 
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatNoticeService {
    private final WxMpService wxMpService;

    /**
     * 发送模板信息
     */
    @SneakyThrows
    public String sentNotice(){
        WxMpTemplateMsgService templateMsgService = wxMpService.getTemplateMsgService();
        WxMpTemplateMessage message = new WxMpTemplateMessage();
        message.setToUser("o2RW45oGxo1Z8Fhcf6Oy9rMM7aT8");
        message.setTemplateId("2rFyFpTBtDcs8ua6etMA6lR_ullvSXcXw5hWbB9v2ZE");
        List<WxMpTemplateData> data = Arrays.asList(
                new WxMpTemplateData("first", "hello 测试"),
                new WxMpTemplateData("keyword1", "小小明'"),
                new WxMpTemplateData("keyword2", "捆绑吧"),
                new WxMpTemplateData("remark", "说明一下")
        );
        message.setData(data);
        return templateMsgService.sendTemplateMsg(message);
    }
}
