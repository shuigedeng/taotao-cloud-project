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

package com.taotao.cloud.workflow.biz.common.constant;

import com.taotao.cloud.workflow.biz.common.constant.model.MCode;

/** 常用提示信息 */
public interface MsgCode {

    /** 执行成功：SU（success） */
    MCode SU000 = MSG("Success");

    MCode SU001 = MSG("新建成功");
    MCode SU002 = MSG("保存成功");
    MCode SU003 = MSG("删除成功");
    MCode SU004 = MSG("更新成功");
    MCode SU005 = MSG("操作成功");
    MCode SU006 = MSG("提交成功，请耐心等待");
    MCode SU007 = MSG("复制成功");
    MCode SU008 = MSG("停止成功");
    MCode SU009 = MSG("终止成功");
    MCode SU010 = MSG("还原成功");
    MCode SU011 = MSG("发布成功");
    MCode SU012 = MSG("发送成功");
    MCode SU013 = MSG("接口修改成功");
    MCode SU014 = MSG("更新接口状态成功");
    MCode SU015 = MSG("上传成功");
    MCode SU016 = MSG("设置成功");
    MCode SU017 = MSG("验证成功");

    /** 执行失败：FA（fail） */
    MCode FA001 = MSG("此条数据不存在");

    MCode FA002 = MSG("更新失败，数据不存在");
    MCode FA003 = MSG("删除失败，数据不存在");
    MCode FA004 = MSG("复制失败，数据不存在");
    MCode FA005 = MSG("发送失败，数据不存在");
    MCode FA006 = MSG("下载失败，数据不存在");
    MCode FA007 = MSG("操作失败，数据不存在");
    MCode FA008 = MSG("停止失败，数据不存在");
    MCode FA009 = MSG("终止失败，数据不存在");
    MCode FA010 = MSG("还原失败，数据不存在");
    MCode FA011 = MSG("发布失败，数据不存在");
    MCode FA012 = MSG("获取失败，数据不存在");
    MCode FA013 = MSG("接口修改失败，数据不存在");
    MCode FA014 = MSG("更新接口状态失败，数据不存在");
    MCode FA015 = MSG("预览失败，数据不存在");
    MCode FA016 = MSG("删除失败，该文件夹存在数据");
    MCode FA017 = MSG("上传失败，文件格式不允许上传");
    MCode FA018 = MSG("文件不存在");
    MCode FA019 = MSG("已失效");
    MCode FA020 = MSG("未查到信息");
    MCode FA021 = MSG("更新失败！您没有权限操作");
    MCode FA022 = MSG("更新失败！您没有权限操作 (角色只有超级管理员才能够操作)");
    MCode FA023 = MSG("更新失败！无法切换组织。");
    MCode FA024 = MSG("删除失败！已绑定用户");
    MCode FA025 = MSG("该组织内无角色或角色权限为空，组织切换失败");
    MCode FA026 = MSG("更新失败，关联组织不存在，请重新登录，或者刷新页面");

    /*======1 短语======*/
    MCode FA101 = MSG("保存失败");
    MCode FA102 = MSG("更新失败");

    /** 重名判断 */
    MCode EXIST001 = MSG("名称不能重复");

    MCode EXIST002 = MSG("编码不能重复");
    MCode EXIST003 = MSG("模板已存在");
    MCode EXIST004 = MSG("文件夹名称不能重复");

    /** 导入导出：IMP（import/export） */
    MCode IMP001 = MSG("导入成功");

    MCode IMP002 = MSG("导入失败，文件格式错误");
    MCode IMP003 = MSG("导入失败，数据已存在");
    MCode IMP004 = MSG("导入失败，数据有误");

    /** 其他 */
    MCode PRI001 = MSG("打印模板不存在");

    MCode PRI002 = MSG("数字字典不存在printDev的字典分类");
    MCode PRI003 = MSG("第1条SQL语句：查询出多条表头信息");
    MCode PRI004 = MSG("第1条SQL语句：未查出表头信息");
    MCode PRI005 = MSG("第{index}条SQL语句：");
    MCode COD001 = MSG("集合条件过滤获得目标为空");

    /** 登录相关 */
    /*=====0-账号相关====*/
    MCode LOG001 = MSG("账户异常");

    MCode LOG002 = MSG("注销成功");
    MCode LOG003 = MSG("无效的账号");
    MCode LOG004 = MSG("账号异常，请联系管理员修改所属组织信息");
    MCode LOG005 = MSG("账号未被激活");
    MCode LOG006 = MSG("账号被禁用");
    MCode LOG007 = MSG("账号已被删除");
    MCode LOG010 = MSG("此IP未在白名单中，请联系管理员");
    MCode LOG011 = MSG("登录失败，用户暂未绑定角色");
    MCode LOG012 = MSG("请联系管理员解除账号锁定！");
    MCode LOG013 = MSG("请等待{time}分钟后再进行登录，或联系管理员解除账号锁定！");

    /*======1-登录相关======*/
    MCode LOG101 = LOG("账户或密码错误，请重新输入。");
    MCode LOG102 = LOG("账号有误，请重新输入。");
    MCode LOG103 = LOG("请输入验证码");
    MCode LOG104 = LOG("验证码错误");
    MCode LOG105 = LOG("登陆繁忙，请稍后再试");

    /*======2-密码修改========*/
    MCode LOG201 = LOG("旧密码错误");
    MCode LOG202 = LOG("修改成功，请牢记新密码。");
    MCode LOG203 = LOG("修改失败，账号不存在。");

    /** 数据库 */
    MCode DB001 = DB("数据类型编码不符合标准（请注意大小写）。MySQL , SQLServer , Oracle , DM8 , KingbaseES , PostgreSQL");

    MCode DB002 = DB("请检查 1、连接信息 2、网络通信 3、数据库服务启动状态。 详情：");
    MCode DB003 = DB("通过url找不到对应数据库");
    MCode DB004 = DB("查询结果集为空。");

    /** 工作流相关错误码 */
    /*========0-状态、短提示==========*/
    MCode WF001 = WF("必填值");

    MCode WF002 = WF("【审核通过】");
    MCode WF003 = WF("【审核同意】");
    MCode WF004 = WF("【审核拒绝】");
    MCode WF005 = WF("审批已完成");
    MCode WF006 = WF("开始");
    MCode WF007 = WF("结束");
    MCode WF008 = WF("必须有表");

    /*=========1-提示语句=========*/
    MCode WF101 = WF("新增异常，需自主排查。");
    MCode WF102 = WF("修改异常，需自主排查。");
    MCode WF103 = WF("复制异常，需自主排查。");
    MCode WF104 = WF("当前流程被处理，无法撤回流程");
    MCode WF105 = WF("任务待审状态才能撤回");
    MCode WF106 = WF("撤回节点下一节点已操作");
    MCode WF107 = WF("包含子流程不能撤回");
    MCode WF108 = WF("当前流程正在运行不能重复提交");
    MCode WF109 = WF("单据规则不存在");
    MCode WF110 = WF("包含子流程不能操作");
    MCode WF111 = WF("当前流程未完成,不能修改工作流引擎");
    MCode WF112 = WF("已审核完成");
    MCode WF113 = WF("未找到流程引擎");
    MCode WF114 = WF("驳回节点不能是子流程");
    MCode WF115 = WF("该流程工单已删除");
    MCode WF116 = WF("当前流程正在运行不能删除");
    MCode WF117 = WF("功能流程不能删除");
    MCode WF118 = WF("子表数据不能删除");
    MCode WF119 = WF("系统表单反射失败");
    MCode WF120 = WF("该流程工单已撤回");
    MCode WF121 = WF("该流程工单已终止");
    MCode WF122 = WF("没有权限操作");
    MCode WF123 = WF("该流程待办已删除");

    static MCode MSG(String desc) {
        return new MCode("message", desc);
    }

    static MCode LOG(String desc) {
        return new MCode("login", desc);
    }

    static MCode DB(String desc) {
        return new MCode("database", desc);
    }

    static MCode WF(String desc) {
        return new MCode("workflow", desc);
    }
}
