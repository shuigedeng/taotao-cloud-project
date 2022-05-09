package com.taotao.cloud.health.alarm.plugin.feishu;


import com.taotao.cloud.health.alarm.core.execut.api.IExecute;
import com.taotao.cloud.health.alarm.plugin.feishu.util.FeishuPublisher;
import java.util.List;

public class FeishuExecute implements IExecute {

    @Override
    public void sendMsg(List<String> users, String title, String msg) {
        users.forEach(user -> FeishuPublisher.sendMessage(title, msg, user));
    }

}
