package com.taotao.cloud.oss.huawei;

import io.github.artislong.core.StandardOssClient;
import io.github.artislong.core.StandardOssClientTest;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 陈敏
 * @version HuaWeiOssClientTest.java, v 1.1 2021/11/25 10:15 chenmin Exp $
 * Created on 2021/11/25
 */
@SpringBootTest
public class HuaWeiOssClientTest implements StandardOssClientTest {

    @Getter
    @Autowired
    @Qualifier(HuaWeiOssConfiguration.DEFAULT_BEAN_NAME)
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
