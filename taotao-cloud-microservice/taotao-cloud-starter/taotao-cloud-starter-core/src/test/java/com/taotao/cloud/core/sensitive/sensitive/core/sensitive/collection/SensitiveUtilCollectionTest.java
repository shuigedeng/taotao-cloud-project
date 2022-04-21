package com.taotao.cloud.core.sensitive.sensitive.core.sensitive.collection;

import com.taotao.cloud.core.sensitive.sensitive.core.DataPrepareTest;
import com.taotao.cloud.core.sensitive.sensitive.core.api.SensitiveUtil;
import com.taotao.cloud.core.sensitive.sensitive.model.sensitive.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 脱敏集合相关测试
 */
public class SensitiveUtilCollectionTest {

    /**
     * 脱敏集合测试
     */
    @Test
    public void desCopyCollectionTest() {
        List<User> userList = DataPrepareTest.buildUserList();

        List<User> sensitiveList = SensitiveUtil.desCopyCollection(userList);
        Assert.assertEquals("[User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}, User{username='集**试', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}]", sensitiveList.toString());
    }

    /**
     * 脱敏集合空测试
     */
    @Test
    public void desCopyCollectionEmptyTest() {
        List<User> sensitiveList = SensitiveUtil.desCopyCollection(null);
        Assert.assertEquals("[]", sensitiveList.toString());

        List<User> sensitiveList2 = SensitiveUtil.desCopyCollection(new ArrayList<User>());
        Assert.assertEquals("[]", sensitiveList2.toString());
    }

    /**
     * 脱敏集合测试
     */
    @Test
    public void desJsonCollectionTest() {
        List<User> userList = DataPrepareTest.buildUserList();

        List<String> sensitiveJsonList = SensitiveUtil.desJsonCollection(userList);
        Assert.assertEquals("[{\"email\":\"123**@qq.com\",\"idCard\":\"123456**********34\",\"phone\":\"188****8888\",\"username\":\"脱*君\"}, {\"email\":\"123**@qq.com\",\"idCard\":\"123456**********34\",\"phone\":\"188****8888\",\"username\":\"集**试\"}]", sensitiveJsonList.toString());
    }

    /**
     * 脱敏集合 json 空场景测试
     */
    @Test
    public void desJsonCollectionEmptyTest() {
        List<String> sensitiveList = SensitiveUtil.desJsonCollection(null);
        Assert.assertEquals("[]", sensitiveList.toString());

        List<String> sensitiveList2 = SensitiveUtil.desJsonCollection(new ArrayList<User>());
        Assert.assertEquals("[]", sensitiveList2.toString());
    }

}
