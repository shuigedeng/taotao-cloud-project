package com.taotao.cloud.monitor.alarm.core.execut.api;


import com.taotao.cloud.monitor.alarm.core.execut.gen.ExecuteNameGenerator;

import java.util.List;

public interface IExecute {

    /**
     * 报警的具体实现
     *
     * @param users 报警用户，支持批量
     * @param title 报警信息的title
     * @param msg   报警的主题信息
     */
    void sendMsg(List<String> users, String title, String msg);


    /**
     * 获取报警单元唯一标识
     *
     * @return name  要求全局唯一
     */
    default String getName() {
        return ExecuteNameGenerator.genExecuteName(this.getClass());
    }


}
