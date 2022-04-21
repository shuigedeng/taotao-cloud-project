package com.taotao.cloud.oss.minio;

import com.taotao.cloud.oss.artislong.core.minio.MinioOssConfiguration;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class MinioOssClientTest implements StandardOssClientTest {

    @Getter
    @Autowired
    @Qualifier(MinioOssConfiguration.DEFAULT_BEAN_NAME)
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
