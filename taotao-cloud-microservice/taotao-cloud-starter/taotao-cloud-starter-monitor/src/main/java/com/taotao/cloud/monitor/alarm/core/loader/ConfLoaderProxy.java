package com.taotao.cloud.monitor.alarm.core.loader;


import com.taotao.cloud.monitor.alarm.core.entity.AlarmConfig;
import com.taotao.cloud.monitor.alarm.core.helper.ExecuteHelper;
import com.taotao.cloud.monitor.alarm.core.loader.api.IConfLoader;
import com.taotao.cloud.monitor.alarm.core.loader.entity.RegisterInfo;

import java.util.List;

/**
 * ConfLoader 代理类，在执行具体的ConfLoader的方法之前，先选择具体选中的ConfLoader，然后根据ConfLoader进行解析报警规则，实现报警
 * <p>
 * <p>
 */
public class ConfLoaderProxy implements IConfLoader {

    private List<IConfLoader> list;

    public ConfLoaderProxy(List<IConfLoader> list) {
        this.list = list;
    }

    @Override
    public RegisterInfo getRegisterInfo() {
        return list.get(0).getRegisterInfo();
    }

    @Override
    public boolean alarmEnable(String alarmKey) {
        return chooseConfLoader(alarmKey).alarmEnable(alarmKey);
    }


    @Override
    public AlarmConfig getAlarmConfigOrDefault(String alarmKey) {
        return chooseConfLoader(alarmKey).getAlarmConfigOrDefault(alarmKey);
    }

    @Override
    public List<ExecuteHelper> getExecuteHelper(String alarmKey, int count) {
        IConfLoader loader = chooseConfLoader(alarmKey);
        return loader.getExecuteHelper(alarmKey, count);
    }


    /**
     * 如果有一个 报警规则解析器定义了精确的报警类型的解析，则选择这个报警选择器(IConfLoader)
     * 如果都没有定义，则选择最高优先级的报警选择器中的默认报警方案执行
     * 如果多个IConfLoader都定义了 alarmKey 的报警规则，则根据优先级选择
     *
     * @param alarmKey
     * @return
     */
    private IConfLoader chooseConfLoader(String alarmKey) {
        for (IConfLoader loader : list) {
            if (loader.containAlarmConfig(alarmKey)) {
                return loader;
            }
        }

        return list.get(0);
    }
}
