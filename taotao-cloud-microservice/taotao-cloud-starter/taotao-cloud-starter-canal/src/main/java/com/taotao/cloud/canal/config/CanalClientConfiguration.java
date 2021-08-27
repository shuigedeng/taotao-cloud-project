package com.taotao.cloud.canal.config;


import com.wwjd.starter.canal.client.core.SimpleCanalClient;
import com.wwjd.starter.canal.client.interfaces.CanalClient;
import com.wwjd.starter.canal.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


public class CanalClientConfiguration {
    /**
     * 记录日志
     */
    private final static Logger logger = LoggerFactory.getLogger(CanalClientConfiguration.class);

    /**
     * canal 配置
     */
    @Autowired
    private CanalConfig canalConfig;
    
    /**
     * 返回 bean 工具类
     *
     * @author 阿导
     * @time 2018/5/28 14:14
     * @CopyRight 万物皆导
     * @param
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public BeanUtil beanUtil() {
        return new BeanUtil();
    }
    
    /**
     * 返回 canal 的客户端
     *
     * @author 阿导
     * @time 2018/5/28 14:15
     * @CopyRight 万物皆导
     * @param
     * @return
     */
    @Bean
    private CanalClient canalClient() {
        logger.info("正在尝试连接 canal 客户端....");
        //连接 canal 客户端
//        CanalClient canalClient = new SimpleCanalClient(canalConfig, MessageTransponders.defaultMessageTransponder());
        CanalClient canalClient = new SimpleCanalClient(canalConfig);
        logger.info("正在尝试开启 canal 客户端....");
        //开启 canal 客户端
        canalClient.start();
        logger.info("启动 canal 客户端成功....");
        //返回结果
        return canalClient;
    }
}
