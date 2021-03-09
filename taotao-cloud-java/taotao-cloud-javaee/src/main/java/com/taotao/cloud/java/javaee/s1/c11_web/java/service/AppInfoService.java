package com.taotao.cloud.java.javaee.s1.c11_web.java.service;

import com.github.pagehelper.PageInfo;
import com.qianfeng.openapi.web.master.pojo.AppInfo;

import java.util.List;

public interface AppInfoService {
    List<AppInfo> getSimpleInfoList();

    void updateAppInfo(AppInfo info);

    PageInfo<AppInfo> getInfoList(AppInfo info,Integer page, Integer limit);

    AppInfo getInfoById(int id);

    void add(AppInfo appInfo);
    void deleteAppInfos(int[] ids);
}
