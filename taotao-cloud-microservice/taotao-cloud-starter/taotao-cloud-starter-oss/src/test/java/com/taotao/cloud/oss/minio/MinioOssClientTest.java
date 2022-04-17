package com.taotao.cloud.oss.minio;

import com.taotao.cloud.oss.artislong.core.minio.MinioOssConfiguration;
import io.github.artislong.core.StandardOssClient;
import io.github.artislong.core.StandardOssClientTest;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 陈敏
 * @version MinioOssClientTest.java, v 1.1 2021/11/25 10:15 chenmin Exp $
 * Created on 2021/11/25
 */
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
