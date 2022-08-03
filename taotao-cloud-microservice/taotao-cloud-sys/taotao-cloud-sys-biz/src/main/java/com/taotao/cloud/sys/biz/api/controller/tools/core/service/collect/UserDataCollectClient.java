package com.taotao.cloud.sys.biz.api.controller.tools.core.service.collect;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用数据收集
 */
@Component
@Slf4j
public class UserDataCollectClient {

    @Autowired(required = false)
    private List<DataCollect> dataCollects = new ArrayList<>();

    private int collectCount;

    @Scheduled(fixedDelay = 1800000,initialDelay = 10000)
    public void schedule(){
//        log.info("数据采集第: {} 次",++collectCount);
        for (DataCollect dataCollect : dataCollects) {
            try {
                dataCollect.collect();
            }catch (Exception e){
                // 出错不影响下次采集和后面数据采集
            }
        }

    }

    public static final class CollectData{
        private String userIp;
        private String module;
        private String data;

        public CollectData() {
        }

        public CollectData(String userIp, String module, String data) {
            this.userIp = userIp;
            this.module = module;
            this.data = data;
        }

        public String getUserIp() {
            return userIp;
        }

        public void setUserIp(String userIp) {
            this.userIp = userIp;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
