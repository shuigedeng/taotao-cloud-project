package com.taotao.cloud.message.biz.austin.handler.handler.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayOpenAppMiniTemplatemessageSendModel;
import com.alipay.api.request.AlipayOpenAppMiniTemplatemessageSendRequest;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.domain.RecallTaskInfo;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.dto.account.AlipayMiniProgramAccount;
import com.taotao.cloud.message.biz.austin.common.dto.model.AlipayMiniProgramContentModel;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.handler.config.AlipayClientSingleton;
import com.taotao.cloud.message.biz.austin.handler.handler.BaseHandler;
import com.taotao.cloud.message.biz.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jwq
 * 支付宝小程序发送订阅消息
 */
@Component
@Slf4j
public class AlipayMiniProgramAccountHandler extends BaseHandler{

    @Autowired
    private AccountUtils accountUtils;

    public AlipayMiniProgramAccountHandler() {
        channelCode = ChannelType.ALIPAY_MINI_PROGRAM.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        AlipayMiniProgramContentModel contentModel= (AlipayMiniProgramContentModel) taskInfo.getContentModel();

        try {
            AlipayMiniProgramAccount miniProgramAccount = accountUtils.getAccountById(taskInfo.getSendAccount(), AlipayMiniProgramAccount.class);
            AlipayClient client = AlipayClientSingleton.getSingleton(miniProgramAccount);
            List<AlipayOpenAppMiniTemplatemessageSendRequest> request = assembleReq(taskInfo.getReceiver(), contentModel);
            for (AlipayOpenAppMiniTemplatemessageSendRequest req : request) {
                client.execute(req);
            }
        } catch (Exception e) {
            log.error("AlipayMiniProgramAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
            return false;
        }
        return true;
    }

    /**
     * 通过taskInfo构建小程序订阅消息
     *
     * @param
     * @return AlipayMiniProgramParam
     */
    private String getAlipayMiniProgramParam(Map<String, String> data) {

        Map<String, Map<String, String>> newMap = data.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Map<String, String> valueMap = new HashMap<>(1);
                            valueMap.put("value", entry.getValue());
                            return valueMap;
                        }
                ));
        return JSONUtil.toJsonStr(newMap);

    }

    /**
     * 组装模板消息的参数
     */
    private List<AlipayOpenAppMiniTemplatemessageSendRequest> assembleReq(Set<String> receiver, AlipayMiniProgramContentModel alipayMiniProgramContentModel) {
        List<AlipayOpenAppMiniTemplatemessageSendRequest> requestList = new ArrayList<>(receiver.size());

        for (String toUserId : receiver) {
            AlipayOpenAppMiniTemplatemessageSendRequest request = new AlipayOpenAppMiniTemplatemessageSendRequest();
            AlipayOpenAppMiniTemplatemessageSendModel model = new AlipayOpenAppMiniTemplatemessageSendModel();
            //兼容新旧用户ID
            if(toUserId.startsWith("2088")) {
                model.setToUserId(toUserId);
            } else {
                model.setToOpenId(toUserId);
            }
            model.setToUserId(toUserId);
            model.setUserTemplateId(alipayMiniProgramContentModel.getTemplateId());
            model.setPage(alipayMiniProgramContentModel.getPage());
            model.setData(getAlipayMiniProgramParam(alipayMiniProgramContentModel.getMiniProgramParam()));
            request.setBizModel(model);
            requestList.add(request);
        }
        return requestList;
    }

    /**
     * @param recallTaskInfo
     */
    @Override
    public void recall(RecallTaskInfo recallTaskInfo) {
        // 未实现 or 渠道不支持
    }
}
