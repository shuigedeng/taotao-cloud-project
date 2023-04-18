package com.taotao.cloud.bigdata.azkaban.mq.rocketmq;

import lombok.Data;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.remoting.RPCHook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: chejiangyi
 * @version: 2019-08-12 15:17
 **/
@ConfigurationProperties
@Data
public class RocketMQProperties {
    @Value("${bsf.rocketmq.aliyun.enabled:false}")
    /**
     * 阿里云是否开启支持
     */
    private Boolean aliyunEnabled;
    @Value("${bsf.rocketmq.aliyun.accesskey:}")
    /**
     * 阿里云访问key
     */
    private String aliyunAccessKey;
    @Value("${bsf.rocketmq.aliyun.secret:}")
    /**
     * 阿里云秘钥
     */
    private String aliyunSecret;
    @Value("${bsf.rocketmq.namesrvaddr:}")
    /**
     * namesrv地址 可以有多个，;分割
     */
    private String namesrvaddr;
    @Value("${bsf.rocketmq.reConsumeTimes:3}")
    /**
     * 重试消费次数
     **/
    private Integer reconsumeTimes;
    /**
     * 是否启用VIP通道
     * */
    @Value("${bsf.rocketmq.isUseVIPChannel:false}")
    private Boolean isUseVIPChannel;
    /**
     * 	消费线程数量
     * */
    @Value("${bsf.rocketmq.consumeThreadMax:64}")
    private Integer consumeThreadMax;
    /**
     * 	消费线程数量
     * */
    @Value("${bsf.rocketmq.consumeThreadMin:20}")
    private Integer consumeThreadMin;
//    /**
//     *	批量消费Size
//     * */
//    @Value("${bsf.rocketmq.consumeMessageBatchMaxSize:100}")
//    private Integer consumeMessageBatchMaxSize;
    
    public static String Project="RocketMQ";
    public static String BSfRocketMQNameSrvaddr="bsf.rocketmq.namesrvaddr";

    public RPCHook getAclRPCHook() {
        return new AclClientRPCHook(new SessionCredentials(aliyunAccessKey, aliyunSecret));
    }
}
