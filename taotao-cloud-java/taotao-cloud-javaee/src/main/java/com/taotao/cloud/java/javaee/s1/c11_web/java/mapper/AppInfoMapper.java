package com.taotao.cloud.java.javaee.s1.c11_web.java.mapper;


import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.AppInfo;
import java.util.List;

public interface AppInfoMapper {
    List<AppInfo> getSimpleInfoList();

    void updateAppInfo(AppInfo info);

    List<AppInfo> getInfoList(AppInfo appInfo);

    AppInfo getInfoById(int id);

    void add(AppInfo appInfo);
}
