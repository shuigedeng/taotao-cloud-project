package com.taotao.cloud.monitor.alarm.core.loader.parse;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.taotao.cloud.monitor.alarm.core.entity.AlarmConfig;
import com.taotao.cloud.monitor.alarm.core.entity.AlarmThreshold;
import com.taotao.cloud.monitor.alarm.core.entity.BasicAlarmConfig;
import com.taotao.cloud.monitor.alarm.core.entity.BasicAlarmThreshold;
import com.taotao.cloud.monitor.alarm.core.execut.SimpleExecuteFactory;
import com.taotao.cloud.monitor.alarm.core.execut.spi.LogExecute;
import com.taotao.cloud.monitor.alarm.core.execut.spi.NoneExecute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class AlarmConfParse {
    public static final String DEFAULT_ALARM_KEY = "default";

    private static final Logger logger = LoggerFactory.getLogger(AlarmConfParse.class);

    private static final BasicAlarmConfig DEFAULT_ALARM_CONFIG = getDefaultAlarmConfig();

    private static final TypeReference<Map<String, BasicAlarmConfig>> typeReference = new TypeReference<Map<String, BasicAlarmConfig>>() {
    };


    private static List<String> currentUsers;

    /**
     * 初始化配置信息
     *
     * @param configs
     */
    public static Map<String, AlarmConfig> parseConfig(String configs, List<String> defaultUsers) {
        currentUsers = defaultUsers;
        Map<String, BasicAlarmConfig> map = parseStrConfig2Map(configs);
        if (map == null) {
            return null;
        }


        // 尽量避免多线程获取配置参数时,出现并发修改的异常, 这里采用全覆盖方式
        ConcurrentHashMap<String, AlarmConfig> backConfigMap = new ConcurrentHashMap<>();
        AlarmConfig temp;
        for (Map.Entry<String, BasicAlarmConfig> entry : map.entrySet()) {
            temp = parse2BizConfig(entry.getValue());
            if (temp == null) {
                continue;
            }


            // 支持多个报警类型（以英文逗号进行分割） 对应同一个报警配置
            for (String key : StringUtils.split(entry.getKey(), ",")) {
                backConfigMap.put(key, temp);
            }
        }


        return backConfigMap;
    }


    /**
     * 将json串格式的报警规则配置，映射为对应实体类
     * <p>
     * 如果传如的是null, 则采用默认的兜底配置
     * 如果传入的是非法的配置，直接返回null， 这样做的目的如下
     * <p>
     * - 启动时，直接获知配置有问题，需要修改
     * - 启动中，修改配置，此时新配置有问题，依然使用旧的配置
     *
     * @param configs
     * @return
     */
    private static Map<String, BasicAlarmConfig> parseStrConfig2Map(String configs) {
        Map<String, BasicAlarmConfig> map = null;

        if (configs != null) {
            try {
                map = JSON.parseObject(configs, typeReference);
            } catch (Exception e) {
                logger.error("ConfigWrapper.parseStrConfig2Map() init config error! configs: {}, e:{}", configs, e);
                return null;
            }
        }

        if (map == null) {
            map = new HashMap<>(1);
        }


        if (!map.containsKey(DEFAULT_ALARM_KEY)) {
            map.put(DEFAULT_ALARM_KEY, DEFAULT_ALARM_CONFIG);
        }
        return map;
    }


    /**
     * 将配置项转换为业务DO对象, 会做一些兼容, 保证 level. min, max, users, thresholds 都不会为null
     *
     * @param basicAlarmConfig
     * @return
     */
    private static AlarmConfig parse2BizConfig(BasicAlarmConfig basicAlarmConfig) {
        if (basicAlarmConfig.getUsers() == null || basicAlarmConfig.getUsers().isEmpty()) { // 如果没有填写用户, 则直接抛弃
            return null;
        }

        AlarmConfig alarmConfig = new AlarmConfig();

        // 如果配置的报警类型是异常的, 则下面会兼容一把，设置为 NONE, 避免因为配置的原因导致系统异常
        alarmConfig.setExecutor(SimpleExecuteFactory.getExecute(basicAlarmConfig.getLevel()));
        alarmConfig.setAutoIncEmergency(basicAlarmConfig.isAutoIncEmergency());
        // 报警用户, 要求用户必须存在
        alarmConfig.setUsers(basicAlarmConfig.getUsers());
        // 报警上限, 如果用户没有填写，采用默认的（因为短信报警按条数要钱, 没必要一直无上限的报）
        alarmConfig.setMaxLimit(basicAlarmConfig.getMax() == null ? AlarmConfig.DEFAULT_MAX_NUM : basicAlarmConfig.getMax());
        // 报警下限, 如果用户没有填写, 采用默认的最小值0
        alarmConfig.setMinLimit(basicAlarmConfig.getMin() == null ? AlarmConfig.DEFAULT_MIN_NUM : basicAlarmConfig.getMin());


        /**
         *  如果配置的basicAlarmThresholdList列表中包含默认的报警方式
         *    - 则报警方式完全按照basicAlarmThresholdList的定义来
         *    - eg: 默认报警为 Log, min=5, max=30
         *    -       basicAlarmThresholdList 中定义为  : { Log, min=6 }, { Email, min=8 }, { WeiXin, min=10, max=16 }, { SMS, min=14, max=26 }
         *    - 则转换后的 alarmThresholdList为:
         *    -     { Log, min=6, max=8 }, { Email, min=8, max=10 }, { WeiXin, min=10, max=16 }, { SMS, min=14, max=26 }
         *    -       count : [6, 8)  Log
         *    -       count : [8, 10) Email
         *    -       count : [10, 16) WeiXin
         *    -       count : [14, 26) SMS
         *
         *  如果不包含默认报警方式
         *    - 则需要补全最外层定义的Min-Max区间中的空余位
         *    - eg:   默认报警为 Log, min=5, max=30
         *    -       basicAlarmThresholdList 中定义为  : { Email, min=8 }, { WeiXin, min=10, max=16 }, { SMS, min=14, max=26 }
         *    - 则转换后的 alarmThresholdList为:
         *    -       { Log, min=5, max=8 }, { Email, min=8, max=10 }, { WeiXin, min=10, max=16 }, { SMS, min=14, max=26 }, { Log, min=26, max=30 }
         *    -       count : [5, 8)  Log
         *    -       count : [8, 10) Email
         *    -       count : [10, 16) WeiXin
         *    -       count : [14, 26) SMS
         *    -       count : [26, 30) Log
         *
         *
         *    上面改造后，很容易得知，支持多重报警方式同时工作，即当技术为14，15 时，同时发起WeiXin和SMS报警
         */

        // 获取配置中的阀值列表，并排序
        List<BasicAlarmThreshold> basicAlarmThresholdList = basicAlarmConfig.getThreshold();
        if(basicAlarmThresholdList == null) {
            basicAlarmThresholdList = Collections.emptyList();
        }
        basicAlarmThresholdList.sort(Comparator.comparingInt(BasicAlarmThreshold::getThreshold));

        List<AlarmThreshold> alarmThresholdList = new ArrayList<>(basicAlarmThresholdList.size() + 2);
        AlarmThreshold tmpAlarmThreshold;
        BasicAlarmThreshold tmpBasicAlarmThreshold;
        boolean containDefaultExecute = false;
        for (int i = 0; i < basicAlarmThresholdList.size(); i++) {
            tmpBasicAlarmThreshold = basicAlarmThresholdList.get(i);
            tmpAlarmThreshold = new AlarmThreshold();
            tmpAlarmThreshold.setExecutor(SimpleExecuteFactory.getExecute(tmpBasicAlarmThreshold.getLevel()));
            tmpAlarmThreshold.setUsers(tmpBasicAlarmThreshold.getUsers());
            tmpAlarmThreshold.setMin(tmpBasicAlarmThreshold.getThreshold());
            if (tmpBasicAlarmThreshold.getMax() == null || tmpBasicAlarmThreshold.getMax() <= tmpBasicAlarmThreshold.getThreshold()) {
                if (i == basicAlarmThresholdList.size() - 1) { // 最后一个，则使用默认的上限阀值
                    tmpAlarmThreshold.setMax(alarmConfig.getMaxLimit());
                } else {
                    tmpAlarmThreshold.setMax(basicAlarmThresholdList.get(i + 1).getThreshold());
                }
            } else {
                tmpAlarmThreshold.setMax(tmpBasicAlarmThreshold.getMax());
            }

            if (!containDefaultExecute) {
                containDefaultExecute = tmpBasicAlarmThreshold.getLevel().equals(basicAlarmConfig.getLevel());
            }


            alarmThresholdList.add(tmpAlarmThreshold);
        }


        int thresholdSize = alarmThresholdList.size();
        if (thresholdSize == 0) {
            tmpAlarmThreshold = new AlarmThreshold();
            tmpAlarmThreshold.setExecutor(alarmConfig.getExecutor());
            tmpAlarmThreshold.setUsers(alarmConfig.getUsers());
            tmpAlarmThreshold.setMin(alarmConfig.getMinLimit());
            tmpAlarmThreshold.setMax(alarmConfig.getMaxLimit());
            alarmThresholdList.add(tmpAlarmThreshold);
        } else if (!containDefaultExecute) { // 不包含时，补全
            tmpAlarmThreshold = new AlarmThreshold();
            tmpAlarmThreshold.setExecutor(alarmConfig.getExecutor());
            tmpAlarmThreshold.setUsers(alarmConfig.getUsers());
            tmpAlarmThreshold.setMin(alarmConfig.getMinLimit());
            tmpAlarmThreshold.setMax(alarmThresholdList.get(0).getMin());
            alarmThresholdList.add(0, tmpAlarmThreshold);

            if (alarmThresholdList.get(thresholdSize).getMax() < alarmConfig.getMaxLimit()) {
                tmpAlarmThreshold = new AlarmThreshold();
                tmpAlarmThreshold.setExecutor(alarmConfig.getExecutor());
                tmpAlarmThreshold.setUsers(alarmConfig.getUsers());
                tmpAlarmThreshold.setMin(alarmThresholdList.get(thresholdSize).getMax());
                tmpAlarmThreshold.setMax(alarmConfig.getMaxLimit());
                alarmThresholdList.add(tmpAlarmThreshold);
            }
        }


        alarmConfig.setAlarmThreshold(alarmThresholdList);
        return alarmConfig;
    }


    /**
     * 一个保底的报警方案
     *
     * @return
     */
    private static BasicAlarmConfig getDefaultAlarmConfig() {
        BasicAlarmConfig defaultConfig = new BasicAlarmConfig();
        defaultConfig.setMin(5);
        defaultConfig.setMax(30);
        defaultConfig.setUsers(currentUsers);
        defaultConfig.setLevel(NoneExecute.NAME);
        defaultConfig.setAutoIncEmergency(true);

        BasicAlarmThreshold logThreshold = new BasicAlarmThreshold();
        logThreshold.setThreshold(10);
        logThreshold.setLevel(LogExecute.NAME);
        logThreshold.setUsers(currentUsers);

        defaultConfig.setThreshold(Collections.singletonList(logThreshold));

        return defaultConfig;
    }


}
