package com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.test;

import com.rabbitmq.client.Connection;
import com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.config.RabbitMQClient;
import org.junit.Test;

import java.io.IOException;

public class Demo1 {

    @Test
    public void getConnection() throws IOException {
        Connection connection = RabbitMQClient.getConnection();

        connection.close();
    }
}
