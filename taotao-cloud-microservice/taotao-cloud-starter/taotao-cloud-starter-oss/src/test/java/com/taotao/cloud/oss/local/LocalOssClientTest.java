package com.taotao.cloud.oss.local;


import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class LocalOssClientTest implements StandardOssClientTest {

    @Getter
    @Autowired
    @Qualifier(LocalOssConfiguration.DEFAULT_BEAN_NAME)
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

        upLoadCheckPoint();
        downloadCheckPoint();
    }

}
