package com.taotao.cloud.oss.ali;

import com.taotao.cloud.oss.StandardOssClientTest;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.ali.AliOssConfiguration;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 陈敏
 * @version AliOssClientTest.java, v 1.1 2021/11/16 21:19 chenmin Exp $
 * Created on 2021/11/16
 */
@SpringBootTest
class AliOssClientTest implements StandardOssClientTest {

    @Getter
    @Autowired
    @Qualifier(AliOssConfiguration.DEFAULT_BEAN_NAME)
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
