package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qianfeng.openapi.web.master.mapper.AppInfoMapper;
import com.qianfeng.openapi.web.master.mapper.CustomerMapper;
import com.qianfeng.openapi.web.master.pojo.AppInfo;
import com.qianfeng.openapi.web.master.pojo.Customer;
import com.qianfeng.openapi.web.master.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppInfoServiceImpl implements AppInfoService {
    @Autowired
    private AppInfoMapper appInfoMapper;
    @Autowired
    private CustomerMapper customerMapper;

//    public final String CACHE_API = "APPKEY:";



    @Override
    public List<AppInfo> getSimpleInfoList() {
        return appInfoMapper.getSimpleInfoList();
    }

    @Override
    public void updateAppInfo(AppInfo info) {
        Customer customer = customerMapper.getCustomerById(info.getCusId());
        info.setCorpName(customer == null ? null : customer.getNickname());
        appInfoMapper.updateAppInfo(info);
        //cacheService.hmset(CACHE_API + appInfo.get("appKey"),appInfo);
    }

    @Override
    public PageInfo<AppInfo> getInfoList(AppInfo info,Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<AppInfo> infoList = appInfoMapper.getInfoList(info);
        return new PageInfo<>(infoList);
    }

    @Override
    public AppInfo getInfoById(int id) {
        return appInfoMapper.getInfoById(id);
    }

    @Override
    public void add(AppInfo appInfo) {
        Customer customer = customerMapper.getCustomerById(appInfo.getCusId());
        appInfo.setCorpName(customer == null ? null : customer.getNickname());
        appInfoMapper.add(appInfo);
    }

    @Override
    public void deleteAppInfos(int[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        for (int id : ids) {
            AppInfo appInfo = appInfoMapper.getInfoById(id);
            if (appInfo != null) {
                appInfo.setState(0);
                appInfoMapper.updateAppInfo(appInfo);
            }
        }
    }
}
