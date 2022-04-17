package com.taotao.cloud.oss.jd;

import io.github.artislong.core.StandardOssClient;
import io.github.artislong.core.StandardOssClientTest;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 陈敏
 * @version JdOssClientTest.java, v 1.1 2022/1/1 16:24 chenmin Exp $
 * Created on 2022/1/1
 */
@SpringBootTest
public class JdOssClientTest implements StandardOssClientTest {

    @Getter
    @Autowired
    @Qualifier(JdOssConfiguration.DEFAULT_BEAN_NAME)
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
