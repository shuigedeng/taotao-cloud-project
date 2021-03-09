package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.mapper;

import com.qianfeng.openapi.web.master.pojo.AppInfo;

import java.util.List;

public interface AppInfoMapper {
    List<AppInfo> getSimpleInfoList();

    void updateAppInfo(AppInfo info);

    List<AppInfo> getInfoList(AppInfo appInfo);

    AppInfo getInfoById(int id);

    void add(AppInfo appInfo);
}
