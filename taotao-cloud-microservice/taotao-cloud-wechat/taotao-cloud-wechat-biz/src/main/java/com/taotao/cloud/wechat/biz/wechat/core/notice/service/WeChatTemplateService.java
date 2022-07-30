package com.taotao.cloud.wechat.biz.wechat.core.notice.service;

import cn.bootx.starter.wechat.core.notice.dao.WeChatTemplateManager;
import cn.bootx.starter.wechat.core.notice.entity.WeChatTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**   
* 微信消息模板
* @author xxm  
* @date 2022/7/15 
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatTemplateService {
    private final WxMpService wxMpService;
    private final WeChatTemplateManager templateManager;

    /**
     * 获取模板列表
     */
    @SneakyThrows
    public List<WxMpTemplate> findAll(){
        WxMpTemplateMsgService templateMsgService = wxMpService.getTemplateMsgService();

        List<WxMpTemplate> templates = templateMsgService.getAllPrivateTemplate();
        return templates;
    }

    /**
     * 同步
     */
    @SneakyThrows
    @Async("asyncExecutor")
    @Transactional(rollbackFor = Exception.class)
    public void sync(){
        WxMpTemplateMsgService templateMsgService = wxMpService.getTemplateMsgService();
        List<WxMpTemplate> wxTemplates = templateMsgService.getAllPrivateTemplate();
        List<WeChatTemplate> weChatTemplates = templateManager.findAll();

        List<String> wxTemplateIds = wxTemplates.stream().map(WxMpTemplate::getTemplateId).collect(Collectors.toList());
        List<String> weChatTemplatesIds = weChatTemplates.stream().map(WeChatTemplate::getTemplateId).collect(Collectors.toList());

        // 停用 本地有且启用,服务端没有
        List<String> disableIds =  weChatTemplates.stream()
                .filter(WeChatTemplate::isEnable)
                .map(WeChatTemplate::getTemplateId)
                .filter(o->!wxTemplateIds.contains(o))
                .collect(Collectors.toList());
        // 启用 本地有且停用, 服务端有
        List<String> enableIds = weChatTemplates.stream()
                .filter(o->!o.isEnable())
                .map(WeChatTemplate::getTemplateId)
                .filter(wxTemplateIds::contains)
                .collect(Collectors.toList());
        // 新增 服务端有且本地没有
        List<WeChatTemplate> saveTemplate = wxTemplates.stream()
                .filter(o -> !weChatTemplatesIds.contains(o.getTemplateId()))
                .map(WeChatTemplate::init)
                .collect(Collectors.toList());
        templateManager.saveAll(saveTemplate);
        templateManager.disableByTemplateIds(disableIds);
        templateManager.enableByTemplateIds(enableIds);
    }
}
