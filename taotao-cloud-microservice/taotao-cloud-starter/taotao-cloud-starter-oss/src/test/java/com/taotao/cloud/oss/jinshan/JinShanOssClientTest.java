package com.taotao.cloud.oss.jinshan;

import com.taotao.cloud.oss.artislong.core.jinshan.JinShanOssConfiguration;
import io.github.artislong.core.StandardOssClient;
import io.github.artislong.core.StandardOssClientTest;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 陈敏
 * @version JinShanOssClientTest.java, v 1.1 2022/3/3 23:09 chenmin Exp $
 * Created on 2022/3/3
 */
@SpringBootTest
public class JinShanOssClientTest implements StandardOssClientTest {

    @Getter
    @Autowired
    @Qualifier(JinShanOssConfiguration.DEFAULT_BEAN_NAME)
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
