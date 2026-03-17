package com.taotao.cloud.message.biz.infrastructure.austin.handler.receipt.stater.impl;

import com.taotao.cloud.message.biz.infrastructure.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.infrastructure.austin.handler.handler.impl.DingDingWorkNoticeHandler;
import com.taotao.cloud.message.biz.infrastructure.austin.handler.receipt.stater.ReceiptMessageStater;
import com.taotao.cloud.message.biz.infrastructure.austin.support.dao.ChannelAccountDao;
import com.taotao.cloud.message.biz.infrastructure.austin.support.domain.ChannelAccount;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 拉取 钉钉工作消息的回执 内容 [未完成]
 *
 * @author shuigedeng
 */
public class DingDingWorkReceiptStaterImpl implements ReceiptMessageStater {

    @Autowired
    private DingDingWorkNoticeHandler workNoticeHandler;

    @Autowired
    private ChannelAccountDao channelAccountDao;

    @Override
    public void start() {
        List<ChannelAccount> accountList = channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstants.FALSE, ChannelType.DING_DING_WORK_NOTICE.getCode());
        for (ChannelAccount channelAccount : accountList) {
            workNoticeHandler.pull(channelAccount.getId());
        }
    }
}
