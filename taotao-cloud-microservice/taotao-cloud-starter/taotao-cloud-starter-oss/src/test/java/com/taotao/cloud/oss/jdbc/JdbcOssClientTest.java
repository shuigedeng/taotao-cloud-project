package com.taotao.cloud.oss.jdbc;

import io.github.artislong.core.StandardOssClient;
import io.github.artislong.core.StandardOssClientTest;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 陈敏
 * @version JdbcOssClientTest.java, v 1.0 2022/3/14 0:04 chenmin Exp $
 * Created on 2022/3/14
 */
@SpringBootTest
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)  // 项目本身不使用数据库时，需增加此注解，移除数据源默认配置，仅需配置对象存储数据源即可
public class JdbcOssClientTest implements StandardOssClientTest {

    @Getter
    @Autowired
    @Qualifier(JdbcOssConfiguration.DEFAULT_BEAN_NAME)
    private StandardOssClient ossClient;

    @Test
    public void test() throws Exception {
        upLoad();
        downLoad();
        copy();
        rename();
        move();
        isExist();
        getInfo();
        delete();
    }

}
