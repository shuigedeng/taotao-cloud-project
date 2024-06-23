package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author 刘森飚
 **/

@Configuration
@Data
public class RabbitMQConfig {


    /**
     * 交换机
     */
    private String shortLinkEventExchange="short_link.event.exchange";

    /**
     * 创建交换机 Topic类型
     * 一般一个微服务一个交换机
     * @return
     */
    @Bean
    public Exchange shortLinkEventExchange(){

        return new TopicExchange(shortLinkEventExchange,true,false);
    }

    //新增短链相关配置====================================

    /**
     * 新增短链 队列
     */
    private String shortLinkAddLinkQueue="short_link.add.link.queue";

    /**
     * 新增短链映射 队列
     */
    private String shortLinkAddMappingQueue="short_link.add.mapping.queue";

    /**
     * 新增短链具体的routingKey,【发送消息使用】
     */
    private String shortLinkAddRoutingKey="short_link.add.link.mapping.routing.key";

    /**
     * topic类型的binding key，用于绑定队列和交换机，是用于 link 消费者
     */
    private String shortLinkAddLinkBindingKey="short_link.add.link.*.routing.key";

    /**
     * topic类型的binding key，用于绑定队列和交换机，是用于 mapping 消费者
     */
    private String shortLinkAddMappingBindingKey="short_link.add.*.mapping.routing.key";


    /**
     * 新增短链api队列和交换机的绑定关系建立
     */
    @Bean
    public Binding shortLinkAddApiBinding(){
        return new Binding(shortLinkAddLinkQueue,
                Binding.DestinationType.QUEUE,
                shortLinkEventExchange,shortLinkAddLinkBindingKey,
                null);
    }


    /**
     * 新增短链mapping队列和交换机的绑定关系建立
     */
    @Bean
    public Binding shortLinkAddMappingBinding(){
        return new Binding(shortLinkAddMappingQueue,
                Binding.DestinationType.QUEUE,
                shortLinkEventExchange,
                shortLinkAddMappingBindingKey,
                null);
    }


    /**
     * 新增短链api 普通队列，用于被监听
     */
    @Bean
    public Queue shortLinkAddLinkQueue(){

        return new Queue(shortLinkAddLinkQueue,true,false,false);

    }

    /**
     * 新增短链mapping 普通队列，用于被监听
     */
    @Bean
    public Queue shortLinkAddMappingQueue(){

        return new Queue(shortLinkAddMappingQueue,true,false,false);
    }




    //删除短链相关配置====================================
    /**
     * 删除短链 队列
     */
    private String shortLinkDelLinkQueue="short_link.del.link.queue";

    /**
     * 删除短链映射 队列
     */
    private String shortLinkDelMappingQueue="short_link.del.mapping.queue";

    /**
     * 删除短链具体的routingKey,【发送消息使用】
     */
    private String shortLinkDelRoutingKey="short_link.del.link.mapping.routing.key";

    /**
     * topic类型的binding key，用于绑定队列和交换机，是用于 link 消费者
     */
    private String shortLinkDelLinkBindingKey="short_link.del.link.*.routing.key";

    /**
     * topic类型的binding key，用于绑定队列和交换机，是用于 mapping 消费者
     */
    private String shortLinkDelMappingBindingKey="short_link.del.*.mapping.routing.key";


    /**
     * 删除操作  短链api队列 和交换机的绑定关系建立
     */
    @Bean
    public Binding shortLinkDelApiBinding(){
        return new Binding(shortLinkDelLinkQueue,Binding.DestinationType.QUEUE, shortLinkEventExchange,shortLinkDelLinkBindingKey,null);
    }


    /**
     * 删除操作 短链mapping队列和交换机的绑定关系建立
     */
    @Bean
    public Binding shortLinkDelMappingBinding(){
        return new Binding(shortLinkDelMappingQueue,Binding.DestinationType.QUEUE, shortLinkEventExchange,shortLinkDelMappingBindingKey,null);
    }


    /**
     * 删除操作 短链api 普通队列，用于被监听
     */
    @Bean
    public Queue shortLinkDelLinkQueue(){

        return new Queue(shortLinkDelLinkQueue,true,false,false);

    }

    /**
     * 删除操作 短链mapping 普通队列，用于被监听
     */
    @Bean
    public Queue shortLinkDelMappingQueue(){

        return new Queue(shortLinkDelMappingQueue,true,false,false);

    }



    //更新短链相关配置====================================
    /**
     * 更新短链 队列
     */
    private String shortLinkUpdateLinkQueue="short_link.update.link.queue";

    /**
     * 更新短链映射 队列
     */
    private String shortLinkUpdateMappingQueue="short_link.update.mapping.queue";

    /**
     * 更新 短链具体的routingKey,【发送消息使用】
     */
    private String shortLinkUpdateRoutingKey="short_link.update.link.mapping.routing.key";

    /**
     * topic类型的binding key，用于绑定队列和交换机，是用于 link 消费者
     */
    private String shortLinkUpdateLinkBindingKey="short_link.update.link.*.routing.key";

    /**
     * topic类型的binding key，用于绑定队列和交换机，是用于 mapping 消费者
     */
    private String shortLinkUpdateMappingBindingKey="short_link.update.*.mapping.routing.key";


    /**
     * 更新操作  短链api队列 和交换机的绑定关系建立
     */
    @Bean
    public Binding shortLinkUpdateApiBinding(){
        return new Binding(shortLinkUpdateLinkQueue,Binding.DestinationType.QUEUE, shortLinkEventExchange,shortLinkUpdateLinkBindingKey,null);
    }


    /**
     * 更新操作 短链mapping队列和交换机的绑定关系建立
     */
    @Bean
    public Binding shortLinkUpdateMappingBinding(){
        return new Binding(shortLinkUpdateMappingQueue,Binding.DestinationType.QUEUE, shortLinkEventExchange,shortLinkUpdateMappingBindingKey,null);
    }


    /**
     * 更新操作 短链api 普通队列，用于被监听
     */
    @Bean
    public Queue shortLinkUpdateLinkQueue(){

        return new Queue(shortLinkUpdateLinkQueue,true,false,false);

    }

    /**
     * 更新操作 短链mapping 普通队列，用于被监听
     */
    @Bean
    public Queue shortLinkUpdateMappingQueue(){

        return new Queue(shortLinkUpdateMappingQueue,true,false,false);

    }
}

