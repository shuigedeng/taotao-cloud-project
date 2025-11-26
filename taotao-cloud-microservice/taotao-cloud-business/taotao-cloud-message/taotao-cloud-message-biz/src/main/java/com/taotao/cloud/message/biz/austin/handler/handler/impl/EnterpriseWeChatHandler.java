package com.taotao.cloud.message.biz.austin.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.constant.AustinConstant;
import com.taotao.cloud.message.biz.austin.common.constant.CommonConstant;
import com.taotao.cloud.message.biz.austin.common.domain.AnchorInfo;
import com.taotao.cloud.message.biz.austin.common.domain.RecallTaskInfo;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.dto.model.EnterpriseWeChatContentModel;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.common.enums.SendMessageType;
import com.taotao.cloud.message.biz.austin.handler.handler.BaseHandler;
import com.taotao.cloud.message.biz.austin.support.config.SupportThreadPoolConfig;
import com.taotao.cloud.message.biz.austin.support.utils.AccountUtils;
import com.taotao.cloud.message.biz.austin.support.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxCpErrorMsgEnum;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.article.MpnewsArticle;
import me.chanjar.weixin.cp.bean.article.NewArticle;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author shuigedeng
 * 企业微信推送处理
 * https://developer.work.weixin.qq.com/document/path/90235
 */
@Component
@Slf4j
public class EnterpriseWeChatHandler extends BaseHandler{

    private static final String WE_CHAT_RECALL_KEY_PREFIX = "WECHAT_RECALL_";
    private static final String WE_CHAT_RECALL_BIZ_TYPE = "EnterpriseWeChatHandler#recall";
    @Autowired
    private AccountUtils accountUtils;
    @Autowired
    private LogUtils logUtils;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public EnterpriseWeChatHandler() {
        channelCode = ChannelType.ENTERPRISE_WE_CHAT.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            WxCpDefaultConfigImpl accountConfig = accountUtils.getAccountById(taskInfo.getSendAccount(), WxCpDefaultConfigImpl.class);
            WxCpMessageServiceImpl messageService = new WxCpMessageServiceImpl(initService(accountConfig));
            WxCpMessageSendResult result = messageService.send(buildWxCpMessage(taskInfo, accountConfig.getAgentId()));

            // 发送成功后记录TaskId，用于消息撤回(支持24小时之内)
            if (Integer.valueOf(WxCpErrorMsgEnum.CODE_0.getCode()).equals(result.getErrCode())) {
                saveRecallInfo(WE_CHAT_RECALL_KEY_PREFIX, taskInfo.getMessageTemplateId(), String.valueOf(result.getMsgId()), CommonConstants.ONE_DAY_SECOND);
                return true;
            }
            logUtils.print(AnchorInfo.builder().bizId(taskInfo.getBizId()).messageId(taskInfo.getMessageId()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).state(result.getErrCode()).build());
        } catch (Exception e) {
            log.error("EnterpriseWeChatHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }


    /**
     * 初始化 WxCpServiceImpl 服务接口
     *
     * @param config
     * @return
     */
    private WxCpService initService(WxCpDefaultConfigImpl config) {
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
        return wxCpService;
    }

    /**
     * 构建企业微信下发消息的对象
     *
     * @param taskInfo
     * @param agentId  应用ID
     * @return
     */
    private WxCpMessage buildWxCpMessage(TaskInfo taskInfo, Integer agentId) {
        String userId;
        if (AustinConstant.SEND_ALL.equals(CollUtil.getFirst(taskInfo.getReceiver()))) {
            userId = CollUtil.getFirst(taskInfo.getReceiver());
        } else {
            userId = StringUtils.join(taskInfo.getReceiver(), CommonConstants.RADICAL);
        }
        EnterpriseWeChatContentModel contentModel = (EnterpriseWeChatContentModel) taskInfo.getContentModel();

        // 通用配置
        WxCpMessage wxCpMessage = new WxCpMessage();
        if (SendMessageType.TEXT.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.TEXT().content(contentModel.getContent()).build();
        } else if (SendMessageType.IMAGE.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.IMAGE().mediaId(contentModel.getMediaId()).build();
        } else if (SendMessageType.VOICE.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.VOICE().mediaId(contentModel.getMediaId()).build();
        } else if (SendMessageType.VIDEO.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.VIDEO().mediaId(contentModel.getMediaId()).description(contentModel.getDescription()).title(contentModel.getTitle()).build();
        } else if (SendMessageType.FILE.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.FILE().mediaId(contentModel.getMediaId()).build();
        } else if (SendMessageType.TEXT_CARD.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.TEXTCARD().url(contentModel.getUrl()).title(contentModel.getTitle()).description(contentModel.getDescription()).btnTxt(contentModel.getBtnTxt()).build();
        } else if (SendMessageType.NEWS.getCode().equals(contentModel.getSendType())) {
            List<NewArticle> newArticles = JSON.parseArray(contentModel.getArticles(), NewArticle.class);
            wxCpMessage = WxCpMessage.NEWS().articles(newArticles).build();
        } else if (SendMessageType.MP_NEWS.getCode().equals(contentModel.getSendType())) {
            List<MpnewsArticle> mpNewsArticles = JSON.parseArray(contentModel.getMpNewsArticle(), MpnewsArticle.class);
            wxCpMessage = WxCpMessage.MPNEWS().articles(mpNewsArticles).build();
        } else if (SendMessageType.MARKDOWN.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.MARKDOWN().content(contentModel.getContent()).build();
        } else if (SendMessageType.MINI_PROGRAM_NOTICE.getCode().equals(contentModel.getSendType())) {
            Map contentItems = JSON.parseObject(contentModel.getContentItems(), Map.class);
            wxCpMessage = WxCpMessage.newMiniProgramNoticeBuilder().appId(contentModel.getAppId()).page(contentModel.getPage()).emphasisFirstItem(contentModel.getEmphasisFirstItem()).contentItems(contentItems).title(contentModel.getTitle()).description(contentModel.getDescription()).build();
        } else if (SendMessageType.TEMPLATE_CARD.getCode().equals(contentModel.getSendType())) {

        }

        wxCpMessage.setAgentId(agentId);
        wxCpMessage.setToUser(userId);
        return wxCpMessage;
    }


    /**
     * 撤回企业微信应用消息；
     * https://developer.work.weixin.qq.com/document/path/94867
     *
     * @param recallTaskInfo
     */
    @Override
    public void recall(RecallTaskInfo recallTaskInfo) {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            try {
                WxCpDefaultConfigImpl accountConfig = accountUtils.getAccountById(recallTaskInfo.getSendAccount(), WxCpDefaultConfigImpl.class);
                WxCpMessageServiceImpl messageService = new WxCpMessageServiceImpl(initService(accountConfig));

                // 优先撤回messageId，如果未传入messageId，则按照模板id撤回
                if (CollUtil.isNotEmpty(recallTaskInfo.getRecallMessageId())) {
                    for (String messageId : recallTaskInfo.getRecallMessageId()) {
                        String msgId = redisTemplate.opsForValue().get(WE_CHAT_RECALL_KEY_PREFIX + messageId);
                        messageService.recall(msgId);
                    }
                } else {
                    while (redisTemplate.opsForList().size(WE_CHAT_RECALL_KEY_PREFIX + recallTaskInfo.getMessageTemplateId()) > 0) {
                        String msgId = redisTemplate.opsForList().leftPop(WE_CHAT_RECALL_KEY_PREFIX + recallTaskInfo.getMessageTemplateId());
                        messageService.recall(msgId);
                    }
                }
            } catch (Exception e) {
                log.error("EnterpriseWeChatHandler#recall fail:{}", Throwables.getStackTraceAsString(e));
            }
        });
    }
}

