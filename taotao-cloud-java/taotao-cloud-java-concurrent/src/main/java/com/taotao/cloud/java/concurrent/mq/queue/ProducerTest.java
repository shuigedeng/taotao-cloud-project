package com.taotao.cloud.java.mq.queue;

import javax.jms.JMSException;

public class ProducerTest {

    /**
     * @param args
     * @throws Exception
     * @throws JMSException
     */
    public static void main(String[] args) throws JMSException, Exception {
        ProducerTool producer = new ProducerTool();
        producer.produceMessage("Index, world!");
        producer.close();
    }
}      

