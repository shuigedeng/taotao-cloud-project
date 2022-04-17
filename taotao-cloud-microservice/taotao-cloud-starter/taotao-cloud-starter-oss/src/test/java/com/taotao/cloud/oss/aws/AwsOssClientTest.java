package com.taotao.cloud.oss.aws;

import com.taotao.cloud.oss.artislong.core.aws.AwsOssConfiguration;
import io.github.artislong.core.StandardOssClient;
import io.github.artislong.core.StandardOssClientTest;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 陈敏
 * @version AwsOssClientTest.java, v 1.0 2022/4/2 20:58 chenmin Exp $
 * Created on 2022/4/2
 */
@SpringBootTest
public class AwsOssClientTest implements StandardOssClientTest {

    @Getter
    @Autowired
    @Qualifier(AwsOssConfiguration.DEFAULT_BEAN_NAME)
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
