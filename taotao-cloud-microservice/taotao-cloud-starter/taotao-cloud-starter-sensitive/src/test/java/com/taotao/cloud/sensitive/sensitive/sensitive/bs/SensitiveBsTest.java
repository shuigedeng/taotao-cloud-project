package com.taotao.cloud.sensitive.sensitive.sensitive.bs;

import com.taotao.cloud.common.support.deepcopy.FastJsonDeepCopy;
import com.taotao.cloud.sensitive.sensitive.sensitive.core.DataPrepareTest;
import com.taotao.cloud.sensitive.sensitive.sensitive.core.bs.SensitiveBs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SensitiveBsTest {

    @Test
    public void desCopyTest() {
        final String originalStr = "User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}";
        final String sensitiveStr = "User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}";

        User user = DataPrepareTest.buildUser();
        Assertions.assertEquals(originalStr, user.toString());

        User sensitiveUser = SensitiveBs.newInstance().desCopy(user);
        Assertions.assertEquals(sensitiveStr, sensitiveUser.toString());
        Assertions.assertEquals(originalStr, user.toString());
    }

    @Test
    public void desJsonTest() {
        final String originalStr = "User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}";
        final String sensitiveStr = "{\"email\":\"123**@qq.com\",\"idCard\":\"123456**********34\",\"phone\":\"188****8888\",\"username\":\"脱*君\"}";

        User user = DataPrepareTest.buildUser();
        Assertions.assertEquals(originalStr, user.toString());

        String sensitiveUserJson = SensitiveBs.newInstance().desJson(user);
        Assertions.assertEquals(sensitiveStr, sensitiveUserJson);
        Assertions.assertEquals(originalStr, user.toString());
    }

    @Test
    public void configTest() {
        User user = DataPrepareTest.buildUser();

        SensitiveBs.newInstance()
                .deepCopy(FastJsonDeepCopy.getInstance())
                .desJson(user);
    }

}
