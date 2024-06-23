package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.LogTypeEnum;
import net.xdclass.model.LogRecord;
import net.xdclass.service.LogService;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 刘森飚
 * @since 2023-02-17
 */

@Service
@Slf4j
public class LogServiceImpl implements LogService {



    private static final String TOPIC_NAME = "ods_link_visit_topic";

    @Autowired
    private KafkaTemplate kafkaTemplate;


    /**
     * ==========用于测试==========
     */
    private static List<String> ipList = new ArrayList<>();
    static {
        //深圳
        ipList.add("14.197.9.110");
        //广州
        ipList.add("113.68.152.139");
        //江苏
        ipList.add("223.107.195.118");
    }

    private static List<String> refererList = new ArrayList<>();
    static {
        refererList.add("https://taobao.com");
        refererList.add("https://douyin.com");
    }
    private Random random = new Random();


    @Override
    public void recordShortLinkLog(HttpServletRequest request, String shortLinkCode, Long accountNo) {
        //ip、浏览器信息
        String ip = CommonUtil.getIpAddr(request);
        //String ip = ipList.get(random.nextInt(ipList.size()));
        //全部请求头
        Map<String,String> headerMap = CommonUtil.getAllRequestHeader(request);
        Map<String,String> availableMap = new HashMap<>();
        availableMap.put("user-agent",headerMap.get("user-agent"));
        availableMap.put("referer",headerMap.get("referer"));
       // availableMap.put("referer",refererList.get(random.nextInt(refererList.size())));
        availableMap.put("accountNo",accountNo.toString());
        LogRecord logRecord = LogRecord.builder()
                //日志类型
                .event(LogTypeEnum.SHORT_LINK_TYPE.name())
                //日志内容
                .data(availableMap)
                //客户端ip
                .ip(ip)
                //产生时间
                .ts(CommonUtil.getCurrentTimestamp())
                //业务唯一标识
                .bizId(shortLinkCode).build();
        String jsonLog = JsonUtil.obj2Json(logRecord);
        //打印控制台
        log.info(jsonLog);
        //发送kafka
        kafkaTemplate.send(TOPIC_NAME,jsonLog);
    }
}
