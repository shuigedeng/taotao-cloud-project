package com.taotao.cloud.monitor.alarm.core.loader.api;


import com.taotao.cloud.monitor.alarm.core.entity.AlarmConfig;
import com.taotao.cloud.monitor.alarm.core.execut.AlarmExecuteSelector;
import com.taotao.cloud.monitor.alarm.core.helper.ExecuteHelper;
import com.taotao.cloud.monitor.alarm.core.loader.entity.RegisterInfo;

import java.util.Collections;
import java.util.List;

public interface IConfLoader {

    /**
     * 加载配置到内存的操作，启动时，被调用
     *
     * @return true 表示加载成功; false 表示加载失败
     */
    default boolean load() {
        return true;
    }


    /**
     * 排序，越小优先级越高
     * <p>
     * 说明： 当系统中多个Loader存在时，会根据优先级来选择order最小的一个作为默认的Loader
     *
     * @return
     */
    default int order() {
        return 10;
    }


    /**
     * 获取注册信息
     *
     * @return
     */
    RegisterInfo getRegisterInfo();


    /**
     * 是否开启报警
     *
     * @return
     */
    boolean alarmEnable(String alarmKey);


    /**
     * 判断alarmKey是否有明确的报警规则
     *
     * @param alarmKey
     * @return
     */
    default boolean containAlarmConfig(String alarmKey) {
        return true;
    }


    /**
     * 根据报警类型，获取对应的报警规则
     * <p>
     * 如果 alarmKey 没有明确的配置， 则返回默认的兜底; 否则返回明确的配置
     *
     * @param alarmKey 报警类型
     * @return
     */
    AlarmConfig getAlarmConfigOrDefault(String alarmKey);


    /**
     * 获取报警执行器
     *
     * @param alarmKey
     * @param count
     * @return
     */
    default List<ExecuteHelper> getExecuteHelper(String alarmKey, int count) {
        if (alarmEnable(alarmKey)) { // get alarm executor
            return AlarmExecuteSelector.getExecute(getAlarmConfigOrDefault(alarmKey), count);
        } else {  // 报警关闭, 则走空报警流程, 将报警信息写入日志文件
            return Collections.singletonList(AlarmExecuteSelector.getDefaultExecute());
        }
    }
}
