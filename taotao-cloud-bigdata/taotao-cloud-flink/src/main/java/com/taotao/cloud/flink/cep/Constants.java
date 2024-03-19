package com.taotao.cloud.flink.cep;

public final class Constants {
    // Required configurations constants for connecting to Database
    public static final String JDBC_URL_ARG = "jdbcUrl";
    public static final String JDBC_DRIVE = "com.mysql.cj.jdbc.Driver";
    public static final String TABLE_NAME_ARG = "tableName";
    public static final String JDBC_INTERVAL_MILLIS_ARG = "jdbcIntervalMs";
    // Required configurations constants for connecting to Kafka
    public static final String KAFKA_BROKERS_ARG = "kafkaBrokers";
    public static final String INPUT_TOPIC_ARG = "inputTopic";
    public static final String INPUT_TOPIC_GROUP_ARG = "inputTopicGroup";
}
