package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.sys.biz.controller.tools.jvm.service.RefreshModel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MonitorDataHistoryService {

    @Autowired(required = false)
    private List<RefreshModel> refreshModels = new ArrayList<>();

    /**
     * 定时刷新监控数据, 启动后 10 秒刷新第一次, 然后每一秒刷新一次
     */
    @Scheduled(fixedRate = 1000,initialDelay = 10000)
    public void refresh(){

    }
}
