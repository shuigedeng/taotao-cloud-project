package com.taotao.cloud.oss.sftp;

import io.github.artislong.core.StandardOssClient;
import io.github.artislong.core.StandardOssClientTest;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 陈敏
 * @version SftpOssClientTest.java, v 1.1 2021/11/23 4:53 chenmin Exp $
 * Created on 2021/11/23
 */
@SpringBootTest
public class SftpOssClientTest implements StandardOssClientTest {

    @Getter
    @Autowired
    @Qualifier(SftpOssConfiguration.DEFAULT_BEAN_NAME)
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
