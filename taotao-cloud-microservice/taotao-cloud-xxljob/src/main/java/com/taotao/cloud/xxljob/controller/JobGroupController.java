/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.xxljob.controller;

import com.taotao.cloud.xxljob.controller.annotation.PermissionLimit;
import com.taotao.cloud.xxljob.core.model.XxlJobGroup;
import com.taotao.cloud.xxljob.core.model.XxlJobRegistry;
import com.taotao.cloud.xxljob.core.util.I18nUtil;
import com.taotao.cloud.xxljob.dao.XxlJobGroupDao;
import com.taotao.cloud.xxljob.dao.XxlJobInfoDao;
import com.taotao.cloud.xxljob.dao.XxlJobRegistryDao;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.RegistryConfig;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * job group controller
 * @author xuxueli 2016-10-02 20:52:56
 */
@Controller
@RequestMapping("/jobgroup")
public class JobGroupController {

    @Resource public XxlJobInfoDao xxlJobInfoDao;
    @Resource public XxlJobGroupDao xxlJobGroupDao;
    @Resource private XxlJobRegistryDao xxlJobRegistryDao;

    @RequestMapping
    @PermissionLimit(adminuser = true)
    public String index(Model model) {
        return "jobgroup/jobgroup.index";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public Map<String, Object> pageList(
            HttpServletRequest request,
            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
            @RequestParam(value = "length", required = false, defaultValue = "10") int length,
            @RequestParam("appname") String appname,
            @RequestParam("title") String title) {

        // page query
        List<XxlJobGroup> list = xxlJobGroupDao.pageList(start, length, appname, title);
        int list_count = xxlJobGroupDao.pageListCount(start, length, appname, title);

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", list_count); // 总记录数
        maps.put("recordsFiltered", list_count); // 过滤后的总记录数
        maps.put("data", list); // 分页列表
        return maps;
    }

    @RequestMapping("/save")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public ReturnT<String> save(XxlJobGroup xxlJobGroup) {

        // valid
        if (xxlJobGroup.getAppname() == null || xxlJobGroup.getAppname().trim().length() == 0) {
            return new ReturnT<String>(
                    500, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (xxlJobGroup.getAppname().length() < 4 || xxlJobGroup.getAppname().length() > 64) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appname_length"));
        }
        if (xxlJobGroup.getAppname().contains(">") || xxlJobGroup.getAppname().contains("<")) {
            return new ReturnT<String>(500, "AppName" + I18nUtil.getString("system_unvalid"));
        }
        if (xxlJobGroup.getTitle() == null || xxlJobGroup.getTitle().trim().length() == 0) {
            return new ReturnT<String>(
                    500,
                    (I18nUtil.getString("system_please_input")
                            + I18nUtil.getString("jobgroup_field_title")));
        }
        if (xxlJobGroup.getTitle().contains(">") || xxlJobGroup.getTitle().contains("<")) {
            return new ReturnT<String>(
                    500,
                    I18nUtil.getString("jobgroup_field_title")
                            + I18nUtil.getString("system_unvalid"));
        }
        if (xxlJobGroup.getAddressType() != 0) {
            if (xxlJobGroup.getAddressList() == null
                    || xxlJobGroup.getAddressList().trim().length() == 0) {
                return new ReturnT<String>(
                        500, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            if (xxlJobGroup.getAddressList().contains(">")
                    || xxlJobGroup.getAddressList().contains("<")) {
                return new ReturnT<String>(
                        500,
                        I18nUtil.getString("jobgroup_field_registryList")
                                + I18nUtil.getString("system_unvalid"));
            }

            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (item == null || item.trim().length() == 0) {
                    return new ReturnT<String>(
                            500, I18nUtil.getString("jobgroup_field_registryList_unvalid"));
                }
            }
        }

        // process
        xxlJobGroup.setUpdateTime(new Date());

        int ret = xxlJobGroupDao.save(xxlJobGroup);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/update")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public ReturnT<String> update(XxlJobGroup xxlJobGroup) {
        // valid
        if (xxlJobGroup.getAppname() == null || xxlJobGroup.getAppname().trim().length() == 0) {
            return new ReturnT<String>(
                    500, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (xxlJobGroup.getAppname().length() < 4 || xxlJobGroup.getAppname().length() > 64) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appname_length"));
        }
        if (xxlJobGroup.getTitle() == null || xxlJobGroup.getTitle().trim().length() == 0) {
            return new ReturnT<String>(
                    500,
                    (I18nUtil.getString("system_please_input")
                            + I18nUtil.getString("jobgroup_field_title")));
        }
        if (xxlJobGroup.getAddressType() == 0) {
            // 0=自动注册
            List<String> registryList = findRegistryByAppName(xxlJobGroup.getAppname());
            String addressListStr = null;
            if (registryList != null && !registryList.isEmpty()) {
                Collections.sort(registryList);
                addressListStr = "";
                for (String item : registryList) {
                    addressListStr += item + ",";
                }
                addressListStr = addressListStr.substring(0, addressListStr.length() - 1);
            }
            xxlJobGroup.setAddressList(addressListStr);
        } else {
            // 1=手动录入
            if (xxlJobGroup.getAddressList() == null
                    || xxlJobGroup.getAddressList().trim().length() == 0) {
                return new ReturnT<String>(
                        500, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (item == null || item.trim().length() == 0) {
                    return new ReturnT<String>(
                            500, I18nUtil.getString("jobgroup_field_registryList_unvalid"));
                }
            }
        }

        // process
        xxlJobGroup.setUpdateTime(new Date());

        int ret = xxlJobGroupDao.update(xxlJobGroup);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    private List<String> findRegistryByAppName(String appnameParam) {
        HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
        List<XxlJobRegistry> list =
                xxlJobRegistryDao.findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
        if (list != null) {
            for (XxlJobRegistry item : list) {
                if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
                    String appname = item.getRegistryKey();
                    List<String> registryList = appAddressMap.get(appname);
                    if (registryList == null) {
                        registryList = new ArrayList<String>();
                    }

                    if (!registryList.contains(item.getRegistryValue())) {
                        registryList.add(item.getRegistryValue());
                    }
                    appAddressMap.put(appname, registryList);
                }
            }
        }
        return appAddressMap.get(appnameParam);
    }

    @RequestMapping("/remove")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public ReturnT<String> remove(@RequestParam("id") int id) {

        // valid
        int count = xxlJobInfoDao.pageListCount(0, 10, id, -1, null, null, null);
        if (count > 0) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_0"));
        }

        List<XxlJobGroup> allList = xxlJobGroupDao.findAll();
        if (allList.size() == 1) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_1"));
        }

        int ret = xxlJobGroupDao.remove(id);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/loadById")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public ReturnT<XxlJobGroup> loadById(@RequestParam("id") int id) {
        XxlJobGroup jobGroup = xxlJobGroupDao.load(id);
        return jobGroup != null
                ? new ReturnT<XxlJobGroup>(jobGroup)
                : new ReturnT<XxlJobGroup>(ReturnT.FAIL_CODE, null);
    }
}
