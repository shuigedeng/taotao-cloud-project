package com.taotao.cloud.monitor.alarm.core.execut;


import com.taotao.cloud.monitor.alarm.core.entity.AlarmConfig;
import com.taotao.cloud.monitor.alarm.core.entity.AlarmThreshold;
import com.taotao.cloud.monitor.alarm.core.execut.spi.NoneExecute;
import com.taotao.cloud.monitor.alarm.core.helper.ExecuteHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 报警选择器, 选择具体的 IExecut 执行报警
 * <p/>
 */
public class AlarmExecuteSelector {


    public static ExecuteHelper getDefaultExecute() {
        return ExecuteHelper.DEFAULT_EXECUTE;
    }


    /**
     * 获取具体的报警执行器
     * <p>
     * 1. 未开启严重等级上升时, 直接返回
     * 2. 开启之后, 判断当前的计数 范围
     *
     * @param alarmConfig 报警配置项, 内部所有的参数都不可能为null
     */
    public static List<ExecuteHelper> getExecute(final AlarmConfig alarmConfig, int count) {
        // 未达到报警的下限 or 超过报警的上限时
        if (count < alarmConfig.getMinLimit() || count > alarmConfig.getMaxLimit()) {
            return Collections.singletonList(new ExecuteHelper(SimpleExecuteFactory.getExecute(
	            NoneExecute.NAME), alarmConfig.getUsers()));
        }


        // 未开启报警升级, 直接返回
        if (!alarmConfig.isAutoIncEmergency()) {
            return Collections.singletonList(new ExecuteHelper(alarmConfig.getExecutor(), alarmConfig.getUsers()));
        }

        if (count < alarmConfig.getAlarmThreshold().get(0).getMin()) {
            // 未达到报警的下限
            return Collections.singletonList(new ExecuteHelper(SimpleExecuteFactory.getExecute(NoneExecute.NAME), alarmConfig.getUsers()));
        }

        List<ExecuteHelper> list = new ArrayList<>();
        for(AlarmThreshold alarmThreshold: alarmConfig.getAlarmThreshold()) {
            if (alarmThreshold.getMin() <= count && count < alarmThreshold.getMax()) {
                list.add(new ExecuteHelper(alarmThreshold.getExecutor(), alarmThreshold.getUsers()));
            }

            if(alarmThreshold.getMin() > count) {
                break;
            }
        }
        return list;
    }
}
