package com.taotao.cloud.oss.up;

import io.github.artislong.core.StandardOssClient;
import io.github.artislong.core.StandardOssClientTest;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 陈敏
 * @version UpOssClientTest.java, v 1.1 2021/12/3 20:31 chenmin Exp $
 * Created on 2021/12/3
 */
@SpringBootTest
public class UpOssClientTest implements StandardOssClientTest {

    @Getter
    @Autowired
    @Qualifier(UpOssConfiguration.DEFAULT_BEAN_NAME)
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
