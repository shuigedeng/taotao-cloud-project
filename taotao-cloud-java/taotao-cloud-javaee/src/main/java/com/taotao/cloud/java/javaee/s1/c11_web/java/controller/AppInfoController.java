package com.taotao.cloud.java.javaee.s1.c11_web.java.controller;

import com.github.pagehelper.PageInfo;
import com.qianfeng.openapi.web.master.pojo.AppInfo;
import com.qianfeng.openapi.web.master.bean.TableData;
import com.qianfeng.openapi.web.master.service.AppInfoService;
import com.qianfeng.openapi.web.master.bean.AjaxMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用管理
 */
@RestController
@RequestMapping("/sys/app_info")
public class AppInfoController {

    @Autowired
    private AppInfoService appInfoService;


    @RequestMapping( "/table")
    public TableData table(AppInfo info,Integer page, Integer limit) {
        PageInfo<AppInfo> list = appInfoService.getInfoList(info, page, limit);
        return new TableData(list.getTotal(), list.getList());
    }

    @RequestMapping("/add")
    public AjaxMessage add(AppInfo appInfo) {

        try {
            appInfoService.add(appInfo);
            return new AjaxMessage(true, "添加成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new AjaxMessage(true, "添加失败");
    }

    @RequestMapping( "/update")
    public AjaxMessage update(AppInfo info) {
        try {
            appInfoService.updateAppInfo(info);
            return new AjaxMessage(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "修改失败");
    }

    @RequestMapping( "/info")
    public AppInfo info(Integer id) {
        return appInfoService.getInfoById(id);
    }

}
