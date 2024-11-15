package com.taotao.cloud.xxljob.core.util;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.xxljob.core.conf.XxlJobAdminConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * i18n util
 *
 * @author xuxueli 2018-01-17 20:39:06
 */
public class I18nUtil {
    private static Logger logger = LoggerFactory.getLogger(I18nUtil.class);

    private static Properties prop = null;
    public static Properties loadI18nProp(){
        if (prop != null) {
            return prop;
        }
        try {
            // build i18n prop
            String i18n = XxlJobAdminConfig.getAdminConfig().getI18n();
            String i18nFile = MessageFormat.format("i18n/message_{0}.properties", i18n);

            // load prop
            Resource resource = new ClassPathResource(i18nFile);
            EncodedResource encodedResource = new EncodedResource(resource,"UTF-8");
            prop = PropertiesLoaderUtils.loadProperties(encodedResource);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return prop;
    }

    /**
     * get val of i18n key
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return loadI18nProp().getProperty(key);
    }

    /**
     * get mult val of i18n mult key, as json
     *
     * @param keys
     * @return
     */
    public static String getMultString(String... keys) {
		JSONObject jsonObject = JSONObject.parseObject("{\n"
			+ "  \"dataTable_sSortAscending\" : \": 以升序排列此列\",\n"
			+ "  \"system_update_fail\" : \"更新失败\",\n"
			+ "  \"jobconf_route_round\" : \"轮询\",\n"
			+ "  \"jobconf_block_SERIAL_EXECUTION\" : \"单机串行\",\n"
			+ "  \"jobconf_route_lfu\" : \"最不经常使用\",\n"
			+ "  \"dataTable_sLast\" : \"末页\",\n"
			+ "  \"jobinfo_shard_index\" : \"分片序号\",\n"
			+ "  \"jobinfo_field_executorparam\" : \"任务参数\",\n"
			+ "  \"user_add\" : \"新增用户\",\n"
			+ "  \"jobinfo_field_gluetype\" : \"运行模式\",\n"
			+ "  \"job_dashboard_job_num\" : \"任务数量\",\n"
			+ "  \"system_api_error\" : \"接口异常\",\n"
			+ "  \"jobconf_route_last\" : \"最后一个\",\n"
			+ "  \"jobinfo_field_id\" : \"任务ID\",\n"
			+ "  \"jobinfo_opt_log\" : \"查询日志\",\n"
			+ "  \"jobinfo_conf_base\" : \"基础配置\",\n"
			+ "  \"jobinfo_field_executorRouteStrategy\" : \"路由策略\",\n"
			+ "  \"daterangepicker_ranges_last_month\" : \"上个月\",\n"
			+ "  \"jobinfo_job\" : \"任务\",\n"
			+ "  \"jobconf_trigger_exe_regaddress\" : \"执行器-地址列表\",\n"
			+ "  \"system_not_found\" : \"不存在\",\n"
			+ "  \"user_password_update_placeholder\" : \"请输入新密码，为空则不更新密码\",\n"
			+ "  \"jobinfo_shard_total\" : \"分片总数\",\n"
			+ "  \"jobgroup_empty\" : \"不存在有效执行器,请联系管理员\",\n"
			+ "  \"system_update_suc\" : \"更新成功\",\n"
			+ "  \"login_password_lt_4\" : \"登录密码不应低于4位\",\n"
			+ "  \"jobgroup_field_appname_limit\" : \"限制以小写字母开头，由小写字母、数字和中划线组成\",\n"
			+ "  \"daterangepicker_ranges_this_month\" : \"本月\",\n"
			+ "  \"dataTable_sSearch\" : \"搜索\",\n"
			+ "  \"job_dashboard_trigger_num_tip\" : \"调度中心触发的调度次数\",\n"
			+ "  \"joblog_handleCode_500\" : \"失败\",\n"
			+ "  \"user_role\" : \"角色\",\n"
			+ "  \"user_role_admin\" : \"管理员\",\n"
			+ "  \"daterangepicker_custom_starttime\" : \"起始时间\",\n"
			+ "  \"joblog_status_suc\" : \"成功\",\n"
			+ "  \"joblog_handleCode_502\" : \"失败(超时)\",\n"
			+ "  \"jobgroup_field_order_digits\" : \"请输入整数\",\n"
			+ "  \"jobinfo_name\" : \"任务管理\",\n"
			+ "  \"jobinfo_field_alarmemail\" : \"报警邮件\",\n"
			+ "  \"joblog_rolling_log_refresh\" : \"刷新\",\n"
			+ "  \"daterangepicker_custom_endtime\" : \"结束时间\",\n"
			+ "  \"logout_btn\" : \"注销\",\n"
			+ "  \"schedule_type_fix_delay\" : \"固定延迟\",\n"
			+ "  \"jobconf_trigger_type_cron\" : \"Cron触发\",\n"
			+ "  \"change_pwd_field_newpwd\" : \"新密码\",\n"
			+ "  \"daterangepicker_custom_monthnames\" : \"一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月\",\n"
			+ "  \"jobinfo_field_executorFailRetryCount_placeholder\" : \"失败重试次数，大于零时生效\",\n"
			+ "  \"system_welcome\" : \"欢迎\",\n"
			+ "  \"joblog_clean_log\" : \"日志清理\",\n"
			+ "  \"user_permission\" : \"权限\",\n"
			+ "  \"admin_name\" : \"任务调度中心\",\n"
			+ "  \"dataTable_sLoadingRecords\" : \"载入中...\",\n"
			+ "  \"job_dashboard_report_loaddata_fail\" : \"调度报表数据加载异常\",\n"
			+ "  \"user_role_normal\" : \"普通用户\",\n"
			+ "  \"login_param_unvalid\" : \"账号或密码错误\",\n"
			+ "  \"schedule_type_none\" : \"无\",\n"
			+ "  \"jobconf_trigger_type_retry\" : \"失败重试触发\",\n"
			+ "  \"login_password_empty\" : \"请输入登录密码\",\n"
			+ "  \"jobconf_beat\" : \"心跳检测\",\n"
			+ "  \"job_dashboard_date_report\" : \"日期分布图\",\n"
			+ "  \"joblog_clean_type_2\" : \"清理三个月之前日志数据\",\n"
			+ "  \"joblog_clean_type_1\" : \"清理一个月之前日志数据\",\n"
			+ "  \"joblog_kill_log\" : \"终止任务\",\n"
			+ "  \"jobgroup_field_appname_length\" : \"AppName长度限制为4~64\",\n"
			+ "  \"user_update_loginuser_limit\" : \"禁止操作当前登录账号\",\n"
			+ "  \"joblog_clean_type_6\" : \"清理一万条以前日志数据\",\n"
			+ "  \"joblog_clean_type_5\" : \"清理一千条以前日志数据\",\n"
			+ "  \"joblog_clean_type_4\" : \"清理一年之前日志数据\",\n"
			+ "  \"joblog_clean_type_3\" : \"清理六个月之前日志数据\",\n"
			+ "  \"jobgroup_field_title_length\" : \"名称长度限制为4~12\",\n"
			+ "  \"system_search\" : \"搜索\",\n"
			+ "  \"dataTable_sFirst\" : \"首页\",\n"
			+ "  \"joblog_status\" : \"状态\",\n"
			+ "  \"jobconf_route_failover\" : \"故障转移\",\n"
			+ "  \"jobgroup_del_limit_1\" : \"拒绝删除, 系统至少保留一个执行器\",\n"
			+ "  \"jobconf_trigger_run\" : \"触发调度\",\n"
			+ "  \"jobinfo_field_update\" : \"更新任务\",\n"
			+ "  \"admin_version\" : \"2.4.2-SNAPSHOT\",\n"
			+ "  \"jobgroup_del_limit_0\" : \"拒绝删除，该执行器使用中\",\n"
			+ "  \"jobconf_route_consistenthash\" : \"一致性HASH\",\n"
			+ "  \"system_ok\" : \"确定\",\n"
			+ "  \"jobconf_trigger_admin_adress\" : \"调度机器\",\n"
			+ "  \"user_username_valid\" : \"限制以小写字母开头，由小写字母、数字组成\",\n"
			+ "  \"system_add_suc\" : \"新增成功\",\n"
			+ "  \"login_username_placeholder\" : \"请输入登录账号\",\n"
			+ "  \"jobinfo_opt_next_time\" : \"下次执行时间\",\n"
			+ "  \"jobinfo_conf_job\" : \"任务配置\",\n"
			+ "  \"jobconf_trigger_address_empty\" : \"调度失败：执行器地址为空\",\n"
			+ "  \"jobconf_monitor_detail\" : \"监控告警明细\",\n"
			+ "  \"jobinfo_field_childJobId\" : \"子任务ID\",\n"
			+ "  \"jobconf_route_lru\" : \"最近最久未使用\",\n"
			+ "  \"jobinfo_field_executorTimeout_placeholder\" : \"任务超时时间，单位秒，大于零时生效\",\n"
			+ "  \"joblog_rolling_log\" : \"执行日志\",\n"
			+ "  \"jobconf_route_busyover\" : \"忙碌转移\",\n"
			+ "  \"system_please_choose\" : \"请选择\",\n"
			+ "  \"job_help_document\" : \"官方文档\",\n"
			+ "  \"jobconf_route_shard\" : \"分片广播\",\n"
			+ "  \"joblog_clean\" : \"清理\",\n"
			+ "  \"jobinfo_glue_jobid_unvalid\" : \"任务ID非法\",\n"
			+ "  \"jobconf_route_first\" : \"第一个\",\n"
			+ "  \"change_pwd_suc_to_logout\" : \"修改密码成功，即将注销登陆\",\n"
			+ "  \"joblog_logid_unvalid\" : \"日志ID非法\",\n"
			+ "  \"jobinfo_field_jobgroup\" : \"执行器\",\n"
			+ "  \"dataTable_sSortDescending\" : \": 以降序排列此列\",\n"
			+ "  \"system_lengh_limit\" : \"长度限制\",\n"
			+ "  \"dataTable_sInfoFiltered\" : \"(由 _MAX_ 项结果过滤)\",\n"
			+ "  \"logout_confirm\" : \"确认注销登录?\",\n"
			+ "  \"change_pwd\" : \"修改密码\",\n"
			+ "  \"daterangepicker_ranges_recent_week\" : \"最近一周\",\n"
			+ "  \"user_username_repeat\" : \"账号重复\",\n"
			+ "  \"daterangepicker_ranges_recent_hour\" : \"最近一小时\",\n"
			+ "  \"jobgroup_field_registryList_unvalid\" : \"机器地址格式非法\",\n"
			+ "  \"joblog_field_triggerTime\" : \"调度时间\",\n"
			+ "  \"jobconf_block_COVER_EARLY\" : \"覆盖之前调度\",\n"
			+ "  \"system_opt\" : \"操作\",\n"
			+ "  \"joblog_field_handleCode\" : \"执行结果\",\n"
			+ "  \"jobinfo_opt_run_tips\" : \"请输入本次执行的机器地址，为空则从执行器获取\",\n"
			+ "  \"joblog_status_running\" : \"进行中\",\n"
			+ "  \"user_password\" : \"密码\",\n"
			+ "  \"job_dashboard_jobgroup_num\" : \"执行器数量\",\n"
			+ "  \"login_btn\" : \"登录\",\n"
			+ "  \"jobconf_trigger_type_manual\" : \"手动触发\",\n"
			+ "  \"jobinfo_field_author\" : \"负责人\",\n"
			+ "  \"joblog_field_executorAddress\" : \"执行器地址\",\n"
			+ "  \"joblog_handleCode_200\" : \"成功\",\n"
			+ "  \"dataTable_sPrevious\" : \"上页\",\n"
			+ "  \"system_permission_limit\" : \"权限拦截\",\n"
			+ "  \"misfire_strategy_do_nothing\" : \"忽略\",\n"
			+ "  \"joblog_kill_log_byman\" : \"人为操作，主动终止\",\n"
			+ "  \"job_dashboard_name\" : \"运行报表\",\n"
			+ "  \"dataTable_sProcessing\" : \"处理中...\",\n"
			+ "  \"schedule_type_cron\" : \"CRON\",\n"
			+ "  \"jobconf_trigger_type\" : \"任务触发类型\",\n"
			+ "  \"system_cancel\" : \"取消\",\n"
			+ "  \"jobinfo_field_executorFailRetryCount\" : \"失败重试次数\",\n"
			+ "  \"daterangepicker_ranges_recent_month\" : \"最近一月\",\n"
			+ "  \"dataTable_sInfoEmpty\" : \"无记录\",\n"
			+ "  \"login_success\" : \"登录成功\",\n"
			+ "  \"system_digits\" : \"整数\",\n"
			+ "  \"job_help\" : \"使用教程\",\n"
			+ "  \"daterangepicker_custom_name\" : \"自定义\",\n"
			+ "  \"system_tips\" : \"系统提示\",\n"
			+ "  \"jobinfo_field_jobdesc\" : \"任务描述\",\n"
			+ "  \"system_opt_del\" : \"删除\",\n"
			+ "  \"jobconf_monitor_alarm_title\" : \"告警类型\",\n"
			+ "  \"system_nav\" : \"导航\",\n"
			+ "  \"jobgroup_field_addressType_limit\" : \"手动录入注册方式，机器地址不可为空\",\n"
			+ "  \"job_dashboard_jobgroup_num_tip\" : \"调度中心在线的执行器机器数量\",\n"
			+ "  \"jobconf_trigger_type_api\" : \"API触发\",\n"
			+ "  \"jobgroup_field_title\" : \"名称\",\n"
			+ "  \"system_fail\" : \"失败\",\n"
			+ "  \"jobgroup_del\" : \"删除执行器\",\n"
			+ "  \"dataTable_sEmptyTable\" : \"表中数据为空\",\n"
			+ "  \"joblog_clean_type_unvalid\" : \"清理类型参数异常\",\n"
			+ "  \"jobconf_monitor\" : \"任务调度中心监控报警\",\n"
			+ "  \"system_opt_fail\" : \"操作失败\",\n"
			+ "  \"job_dashboard_report\" : \"调度报表\",\n"
			+ "  \"jobconf_trigger_child_run\" : \"触发子任务\",\n"
			+ "  \"jobinfo_glue_rollback\" : \"版本回溯\",\n"
			+ "  \"joblog_status_fail\" : \"失败\",\n"
			+ "  \"jobinfo_conf_advanced\" : \"高级配置\",\n"
			+ "  \"jobgroup_field_orderrange\" : \"取值范围为1~1000\",\n"
			+ "  \"system_close\" : \"关闭\",\n"
			+ "  \"joblog_clean_type_9\" : \"清理所有日志数据\",\n"
			+ "  \"system_show\" : \"查看\",\n"
			+ "  \"joblog_clean_type_8\" : \"清理十万条以前日志数据\",\n"
			+ "  \"joblog_clean_type_7\" : \"清理三万条以前日志数据\",\n"
			+ "  \"jobgroup_edit\" : \"编辑执行器\",\n"
			+ "  \"jobinfo_opt_stop\" : \"停止\",\n"
			+ "  \"jobinfo_script_location\" : \"脚本位置\",\n"
			+ "  \"jobgroup_field_registryList_placeholder\" : \"请输入执行器地址列表，多地址逗号分隔\",\n"
			+ "  \"jobinfo_field_childJobId_placeholder\" : \"请输入子任务的任务ID,如存在多个则逗号分隔\",\n"
			+ "  \"joblog_clean_type\" : \"清理方式\",\n"
			+ "  \"joblog_field_triggerMsg\" : \"调度备注\",\n"
			+ "  \"user_update\" : \"更新用户\",\n"
			+ "  \"login_remember_me\" : \"记住密码\",\n"
			+ "  \"system_opt_suc\" : \"操作成功\",\n"
			+ "  \"jobgroup_name\" : \"执行器管理\",\n"
			+ "  \"daterangepicker_custom_daysofweek\" : \"日,一,二,三,四,五,六\",\n"
			+ "  \"jobinfo_glue_remark\" : \"源码备注\",\n"
			+ "  \"logout_success\" : \"注销成功\",\n"
			+ "  \"jobinfo_field_timeout\" : \"任务超时时间\",\n"
			+ "  \"jobgroup_list\" : \"执行器列表\",\n"
			+ "  \"user_manage\" : \"用户管理\",\n"
			+ "  \"login_password_placeholder\" : \"请输入登录密码\",\n"
			+ "  \"jobconf_idleBeat\" : \"空闲检测\",\n"
			+ "  \"system_empty\" : \"无\",\n"
			+ "  \"joblog_rolling_log_failoften\" : \"终止请求Rolling日志,请求失败次数超上限,可刷新页面重新加载日志\",\n"
			+ "  \"jobconf_callback_child_msg1\" : \"{0}/{1} [任务ID={2}], 触发{3}, 触发备注: {4} <br>\",\n"
			+ "  \"dataTable_sNext\" : \"下页\",\n"
			+ "  \"joblog_name\" : \"调度日志\",\n"
			+ "  \"admin_name_full\" : \"分布式任务调度平台XXL-JOB\",\n"
			+ "  \"daterangepicker_ranges_yesterday\" : \"昨日\",\n"
			+ "  \"logout_fail\" : \"注销失败\",\n"
			+ "  \"system_success\" : \"成功\",\n"
			+ "  \"login_param_empty\" : \"账号或密码为空\",\n"
			+ "  \"jobgroup_add\" : \"新增执行器\",\n"
			+ "  \"job_dashboard_trigger_num\" : \"调度次数\",\n"
			+ "  \"jobinfo_opt_registryinfo\" : \"注册节点\",\n"
			+ "  \"jobconf_monitor_alarm_content\" : \"告警内容\",\n"
			+ "  \"joblog_status_all\" : \"全部\",\n"
			+ "  \"jobconf_route_random\" : \"随机\",\n"
			+ "  \"system_unvalid\" : \"非法\",\n"
			+ "  \"jobinfo_opt_start\" : \"启动\",\n"
			+ "  \"login_username_lt_4\" : \"登录账号不应低于4位\",\n"
			+ "  \"joblog_kill_log_limit\" : \"调度失败，无法终止日志\",\n"
			+ "  \"jobconf_callback_child_msg2\" : \"{0}/{1} [任务ID={2}], 触发失败, 触发备注: 任务ID格式错误 <br>\",\n"
			+ "  \"dataTable_sZeroRecords\" : \"没有匹配结果\",\n"
			+ "  \"jobconf_trigger_type_parent\" : \"父任务触发\",\n"
			+ "  \"joblog_field_handleTime\" : \"执行时间\",\n"
			+ "  \"jobconf_block_DISCARD_LATER\" : \"丢弃后续调度\",\n"
			+ "  \"dataTable_sLengthMenu\" : \"每页 _MENU_ 条记录\",\n"
			+ "  \"jobgroup_field_registryList\" : \"机器地址\",\n"
			+ "  \"system_all\" : \"全部\",\n"
			+ "  \"jobinfo_field_alarmemail_placeholder\" : \"请输入报警邮件，多个邮件地址则逗号分隔\",\n"
			+ "  \"jobgroup_field_addressType\" : \"注册方式\",\n"
			+ "  \"job_dashboard_job_num_tip\" : \"调度中心运行的任务数量\",\n"
			+ "  \"joblog_lost_fail\" : \"任务结果丢失，标记失败\",\n"
			+ "  \"system_opt_copy\" : \"复制\",\n"
			+ "  \"schedule_type_fix_rate\" : \"固定速度\",\n"
			+ "  \"jobconf_trigger_exe_regtype\" : \"执行器-注册方式\",\n"
			+ "  \"misfire_strategy_fire_once_now\" : \"立即执行一次\",\n"
			+ "  \"jobgroup_field_addressType_1\" : \"手动录入\",\n"
			+ "  \"jobgroup_field_addressType_0\" : \"自动注册\",\n"
			+ "  \"jobinfo_conf_schedule\" : \"调度配置\",\n"
			+ "  \"joblog_rolling_log_triggerfail\" : \"任务发起调度失败，无法查看执行日志\",\n"
			+ "  \"jobconf_monitor_alarm_type\" : \"调度失败\",\n"
			+ "  \"dataTable_sInfo\" : \"第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )\",\n"
			+ "  \"schedule_type\" : \"调度类型\",\n"
			+ "  \"jobinfo_opt_run\" : \"执行一次\",\n"
			+ "  \"user_username\" : \"账号\",\n"
			+ "  \"jobinfo_field_executorBlockStrategy\" : \"阻塞处理策略\",\n"
			+ "  \"login_username_empty\" : \"请输入登录账号\",\n"
			+ "  \"joblog_field_handleMsg\" : \"执行备注\",\n"
			+ "  \"jobconf_trigger_type_misfire\" : \"调度过期补偿\",\n"
			+ "  \"jobinfo_field_add\" : \"新增\",\n"
			+ "  \"daterangepicker_ranges_today\" : \"今日\",\n"
			+ "  \"system_save\" : \"保存\",\n"
			+ "  \"admin_i18n\" : \"\",\n"
			+ "  \"job_dashboard_rate_report\" : \"成功比例图\",\n"
			+ "  \"joblog_field_triggerCode\" : \"调度结果\",\n"
			+ "  \"system_status\" : \"状态\",\n"
			+ "  \"system_please_input\" : \"请输入\",\n"
			+ "  \"misfire_strategy\" : \"调度过期策略\",\n"
			+ "  \"jobinfo_glue_gluetype_unvalid\" : \"该任务非GLUE模式\",\n"
			+ "  \"schedule_type_none_limit_start\" : \"当前调度类型禁止启动\",\n"
			+ "  \"system_add_fail\" : \"新增失败\",\n"
			+ "  \"login_fail\" : \"登录失败\",\n"
			+ "  \"jobinfo_glue_remark_limit\" : \"源码备注长度限制为4~100\"\n"
			+ "}");
		return JacksonUtil.writeValueAsString(jsonObject.getInnerMap());
//        Map<String, String> map = new HashMap<String, String>();
//
//        Properties prop = loadI18nProp();
//        if (keys!=null && keys.length>0) {
//            for (String key: keys) {
//                map.put(key, prop.getProperty(key));
//            }
//        } else {
//            for (String key: prop.stringPropertyNames()) {
//                map.put(key, prop.getProperty(key));
//            }
//        }
//
//        String json = JacksonUtil.writeValueAsString(map);
//        return json;
    }

}
