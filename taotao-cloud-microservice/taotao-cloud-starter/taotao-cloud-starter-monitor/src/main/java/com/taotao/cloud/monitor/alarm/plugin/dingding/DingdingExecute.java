package com.taotao.cloud.monitor.alarm.plugin.dingding;


import com.taotao.cloud.monitor.alarm.core.execut.api.IExecute;
import com.taotao.cloud.monitor.alarm.plugin.dingding.util.DingdingPublisher;

import java.util.List;

/**
 * 钉钉报警
 */
public class DingdingExecute implements IExecute {

    @Override
    public void sendMsg(List<String> users, String title, String msg) {
        for (String user : users) {
            DingdingPublisher.sendMessage(title, msg, user);
        }
    }

}
