package com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.test;

import com.qf.config.RabbitMQClient;
import com.rabbitmq.client.Connection;
import org.junit.Test;

import java.io.IOException;

public class Demo1 {

    @Test
    public void getConnection() throws IOException {
        Connection connection = RabbitMQClient.getConnection();

        connection.close();
    }
}
