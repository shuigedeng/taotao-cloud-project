package com.taotao.cloud.bigdata.azkaban.mq.base;

import com.free.bsf.core.util.LogUtils;
import com.free.bsf.mq.MQProperties;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * @author: chejiangyi
 * @version: 2019-06-12 12:19
 * 生产者抽象
 **/
public class AbstractProducer  extends AbstractMQ {
    @Override
    public void close(){
        try
        {
            LogUtils.debug(AbstractProducer.class, MQProperties.Project,"MQ生产者准备释放资源...");
            Object obj = getObject();
            if(obj!=null) {
                if (obj instanceof DefaultMQProducer) {
                    DefaultMQProducer mqProducer = ((DefaultMQProducer) obj);
                    mqProducer.shutdown();
                    LogUtils.info(AbstractProducer.class, MQProperties.Project, "rocketmq 生产者释放资源完毕");
                    obj = null;
                }
                innerClose(obj);
                setObject(null);
            }
        }catch (Exception exp)
        {
            LogUtils.warn(AbstractProducer.class,MQProperties.Project,"MQ生产者资源释放异常",exp);
        }
    }
}
