package com.taotao.cloud.health.alarm.core.loader.spi;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.health.alarm.core.entity.AlarmConfig;
import com.taotao.cloud.health.alarm.core.loader.api.IConfLoader;
import com.taotao.cloud.health.alarm.core.loader.entity.RegisterInfo;
import com.taotao.cloud.health.alarm.core.loader.helper.PropertiesConfListenerHelper;
import com.taotao.cloud.health.alarm.core.loader.helper.RegisterInfoLoaderHelper;
import com.taotao.cloud.health.alarm.core.loader.parse.AlarmConfParse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;

public class PropertiesConfLoader implements IConfLoader {

    private RegisterInfo registerInfo;

    private Map<String, AlarmConfig> cacheMap;

    private AlarmConfig defaultAlarmConfig;

    public boolean load() {
        // 获取注册信息
        registerInfo = RegisterInfoLoaderHelper.load();
        if (registerInfo == null) {
            return false;
        }


        // 获取报警的配置类
        File file;
        String path = registerInfo.getAlarmConfPath();
        if (path.startsWith("/")) {
            file = new File(path);
        } else {
            URL url = this.getClass().getClassLoader().getResource(path);
            file = new File(url.getFile());
        }


        // 加载成功，才替换 cacheMap的内容； 主要是为了防止修改配置出现问题
        Map<String, AlarmConfig> tmp = init(file);
        boolean ans = tmp != null;
        // 注册配置文件的变动
        ans = ans && PropertiesConfListenerHelper.registerConfChangeListener(file, this::init);
        if (ans) {
            LogUtil.info("PropertiesConfLoader registerConfChangeListener success!");
        }
        return ans;
    }


    private Map<String, AlarmConfig> init(File file) {
        Map<String, AlarmConfig> tmp;
        try {
            // 正常来讲，是一个完整的json串
            List<String> list = IOUtils.readLines(new FileInputStream(file), "utf-8");
            String config = Joiner.on("").join(list);
            tmp = AlarmConfParse.parseConfig(config, Splitter.on(",").splitToList(registerInfo.getDefaultAlarmUsers()));
        } catch (IOException e) {
            LogUtil.error("load config into cacheMap error! e: {}", e);
            return null;
        }

        if(tmp != null) {
            cacheMap = tmp;
            // 将默认的报警规则，缓存起来
            defaultAlarmConfig = cacheMap.get(AlarmConfParse.DEFAULT_ALARM_KEY);
        }
        return tmp;
    }


    @Override
    public RegisterInfo getRegisterInfo() {
        return registerInfo;
    }

    @Override
    public boolean alarmEnable(String alarmKey) {
        return true;
    }

    @Override
    public boolean containAlarmConfig(String alarmKey) {
        return cacheMap.containsKey(alarmKey);
    }

    @Override
    public AlarmConfig getAlarmConfigOrDefault(String alarmKey) {
        return cacheMap.getOrDefault(alarmKey, defaultAlarmConfig);
    }
}
