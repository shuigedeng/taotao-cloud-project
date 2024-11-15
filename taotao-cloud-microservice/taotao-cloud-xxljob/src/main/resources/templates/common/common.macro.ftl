<#macro commonStyle>

	<#-- favicon -->
	<link rel="icon" href="${request.contextPath}/static/favicon.ico" />

	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/font-awesome/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/Ionicons/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/dist/css/skins/_all-skins.min.css">
      
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

	<!-- pace -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/PACE/themes/blue/pace-theme-flash.css">

	<#-- i18n -->
	<#global I18n = I18nUtil.getMultString()?eval />

</#macro>

<#macro commonScript>
	<!-- jQuery -->
	<script src="${request.contextPath}/static/adminlte/bower_components/jquery/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="${request.contextPath}/static/adminlte/bower_components/bootstrap/js/bootstrap.min.js"></script>
	<!-- FastClick -->
	<script src="${request.contextPath}/static/adminlte/bower_components/fastclick/fastclick.js"></script>
	<!-- AdminLTE App -->
	<script src="${request.contextPath}/static/adminlte/dist/js/adminlte.min.js"></script>
	<!-- jquery.slimscroll -->
	<script src="${request.contextPath}/static/adminlte/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>

    <!-- pace -->
    <script src="${request.contextPath}/static/adminlte/bower_components/PACE/pace.min.js"></script>
    <#-- jquery cookie -->
	<script src="${request.contextPath}/static/plugins/jquery/jquery.cookie.js"></script>
	<#-- jquery.validate -->
	<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>

	<#-- layer -->
	<script src="${request.contextPath}/static/plugins/layer/layer.js"></script>

	<#-- common -->
    <script src="${request.contextPath}/static/js/common.1.js"></script>
    <script>
		var base_url = '${request.contextPath}';
        var I18n = {
          "dataTable_sSortAscending" : ": 以升序排列此列",
          "system_update_fail" : "更新失败",
          "jobconf_route_round" : "轮询",
          "jobconf_block_SERIAL_EXECUTION" : "单机串行",
          "jobconf_route_lfu" : "最不经常使用",
          "dataTable_sLast" : "末页",
          "jobinfo_shard_index" : "分片序号",
          "jobinfo_field_executorparam" : "任务参数",
          "user_add" : "新增用户",
          "jobinfo_field_gluetype" : "运行模式",
          "job_dashboard_job_num" : "任务数量",
          "system_api_error" : "接口异常",
          "jobconf_route_last" : "最后一个",
          "jobinfo_field_id" : "任务ID",
          "jobinfo_opt_log" : "查询日志",
          "jobinfo_conf_base" : "基础配置",
          "jobinfo_field_executorRouteStrategy" : "路由策略",
          "daterangepicker_ranges_last_month" : "上个月",
          "jobinfo_job" : "任务",
          "jobconf_trigger_exe_regaddress" : "执行器-地址列表",
          "system_not_found" : "不存在",
          "user_password_update_placeholder" : "请输入新密码，为空则不更新密码",
          "jobinfo_shard_total" : "分片总数",
          "jobgroup_empty" : "不存在有效执行器,请联系管理员",
          "system_update_suc" : "更新成功",
          "login_password_lt_4" : "登录密码不应低于4位",
          "jobgroup_field_appname_limit" : "限制以小写字母开头，由小写字母、数字和中划线组成",
          "daterangepicker_ranges_this_month" : "本月",
          "dataTable_sSearch" : "搜索",
          "job_dashboard_trigger_num_tip" : "调度中心触发的调度次数",
          "joblog_handleCode_500" : "失败",
          "user_role" : "角色",
          "user_role_admin" : "管理员",
          "daterangepicker_custom_starttime" : "起始时间",
          "joblog_status_suc" : "成功",
          "joblog_handleCode_502" : "失败(超时)",
          "jobgroup_field_order_digits" : "请输入整数",
          "jobinfo_name" : "任务管理",
          "jobinfo_field_alarmemail" : "报警邮件",
          "joblog_rolling_log_refresh" : "刷新",
          "daterangepicker_custom_endtime" : "结束时间",
          "logout_btn" : "注销",
          "schedule_type_fix_delay" : "固定延迟",
          "jobconf_trigger_type_cron" : "Cron触发",
          "change_pwd_field_newpwd" : "新密码",
          "daterangepicker_custom_monthnames" : "一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月",
          "jobinfo_field_executorFailRetryCount_placeholder" : "失败重试次数，大于零时生效",
          "system_welcome" : "欢迎",
          "joblog_clean_log" : "日志清理",
          "user_permission" : "权限",
          "admin_name" : "任务调度中心",
          "system_opt_edit" : "编辑",
          "dataTable_sLoadingRecords" : "载入中...",
          "job_dashboard_report_loaddata_fail" : "调度报表数据加载异常",
          "user_role_normal" : "普通用户",
          "login_param_unvalid" : "账号或密码错误",
          "schedule_type_none" : "无",
          "jobconf_trigger_type_retry" : "失败重试触发",
          "login_password_empty" : "请输入登录密码",
          "jobconf_beat" : "心跳检测",
          "job_dashboard_date_report" : "日期分布图",
          "joblog_clean_type_2" : "清理三个月之前日志数据",
          "joblog_clean_type_1" : "清理一个月之前日志数据",
          "joblog_kill_log" : "终止任务",
          "jobgroup_field_appname_length" : "AppName长度限制为4~64",
          "user_update_loginuser_limit" : "禁止操作当前登录账号",
          "joblog_clean_type_6" : "清理一万条以前日志数据",
          "joblog_clean_type_5" : "清理一千条以前日志数据",
          "joblog_clean_type_4" : "清理一年之前日志数据",
          "joblog_clean_type_3" : "清理六个月之前日志数据",
          "jobgroup_field_title_length" : "名称长度限制为4~12",
          "system_search" : "搜索",
          "dataTable_sFirst" : "首页",
          "joblog_status" : "状态",
          "jobconf_route_failover" : "故障转移",
          "jobgroup_del_limit_1" : "拒绝删除, 系统至少保留一个执行器",
          "jobconf_trigger_run" : "触发调度",
          "jobinfo_field_update" : "更新任务",
          "admin_version" : "2.4.2-SNAPSHOT",
          "jobgroup_del_limit_0" : "拒绝删除，该执行器使用中",
          "jobconf_route_consistenthash" : "一致性HASH",
          "system_ok" : "确定",
          "jobconf_trigger_admin_adress" : "调度机器",
          "user_username_valid" : "限制以小写字母开头，由小写字母、数字组成",
          "system_add_suc" : "新增成功",
          "login_username_placeholder" : "请输入登录账号",
          "jobinfo_opt_next_time" : "下次执行时间",
          "jobinfo_conf_job" : "任务配置",
          "jobconf_trigger_address_empty" : "调度失败：执行器地址为空",
          "jobconf_monitor_detail" : "监控告警明细",
          "jobinfo_field_childJobId" : "子任务ID",
          "jobconf_route_lru" : "最近最久未使用",
          "jobinfo_field_executorTimeout_placeholder" : "任务超时时间，单位秒，大于零时生效",
          "joblog_rolling_log" : "执行日志",
          "jobconf_route_busyover" : "忙碌转移",
          "system_please_choose" : "请选择",
          "job_help_document" : "官方文档",
          "jobconf_route_shard" : "分片广播",
          "joblog_clean" : "清理",
          "jobinfo_glue_jobid_unvalid" : "任务ID非法",
          "jobconf_route_first" : "第一个",
          "change_pwd_suc_to_logout" : "修改密码成功，即将注销登陆",
          "joblog_logid_unvalid" : "日志ID非法",
          "jobinfo_field_jobgroup" : "执行器",
          "dataTable_sSortDescending" : ": 以降序排列此列",
          "system_lengh_limit" : "长度限制",
          "dataTable_sInfoFiltered" : "(由  项结果过滤)",
          "logout_confirm" : "确认注销登录?",
          "change_pwd" : "修改密码",
          "daterangepicker_ranges_recent_week" : "最近一周",
          "user_username_repeat" : "账号重复",
          "daterangepicker_ranges_recent_hour" : "最近一小时",
          "jobgroup_field_registryList_unvalid" : "机器地址格式非法",
          "joblog_field_triggerTime" : "调度时间",
          "jobconf_block_COVER_EARLY" : "覆盖之前调度",
          "system_opt" : "操作",
          "joblog_field_handleCode" : "执行结果",
          "jobinfo_opt_run_tips" : "请输入本次执行的机器地址，为空则从执行器获取",
          "joblog_status_running" : "进行中",
          "user_password" : "密码",
          "job_dashboard_jobgroup_num" : "执行器数量",
          "login_btn" : "登录",
          "jobconf_trigger_type_manual" : "手动触发",
          "jobinfo_field_author" : "负责人",
          "joblog_field_executorAddress" : "执行器地址",
          "joblog_handleCode_200" : "成功",
          "dataTable_sPrevious" : "上页",
          "system_permission_limit" : "权限拦截",
          "misfire_strategy_do_nothing" : "忽略",
          "joblog_kill_log_byman" : "人为操作，主动终止",
          "job_dashboard_name" : "运行报表",
          "dataTable_sProcessing" : "处理中...",
          "schedule_type_cron" : "CRON",
          "jobconf_trigger_type" : "任务触发类型",
          "system_cancel" : "取消",
          "jobinfo_field_executorFailRetryCount" : "失败重试次数",
          "daterangepicker_ranges_recent_month" : "最近一月",
          "dataTable_sInfoEmpty" : "无记录",
          "login_success" : "登录成功",
          "system_digits" : "整数",
          "job_help" : "使用教程",
          "daterangepicker_custom_name" : "自定义",
          "system_tips" : "系统提示",
          "jobinfo_field_jobdesc" : "任务描述",
          "system_opt_del" : "删除",
          "jobconf_monitor_alarm_title" : "告警类型",
          "system_nav" : "导航",
          "jobgroup_field_addressType_limit" : "手动录入注册方式，机器地址不可为空",
          "job_dashboard_jobgroup_num_tip" : "调度中心在线的执行器机器数量",
          "jobconf_trigger_type_api" : "API触发",
          "jobgroup_field_title" : "名称",
          "system_fail" : "失败",
          "jobgroup_del" : "删除执行器",
          "dataTable_sEmptyTable" : "表中数据为空",
          "joblog_clean_type_unvalid" : "清理类型参数异常",
          "jobconf_monitor" : "任务调度中心监控报警",
          "system_opt_fail" : "操作失败",
          "job_dashboard_report" : "调度报表",
          "jobconf_trigger_child_run" : "触发子任务",
          "jobinfo_glue_rollback" : "版本回溯",
          "joblog_status_fail" : "失败",
          "jobinfo_conf_advanced" : "高级配置",
          "jobgroup_field_orderrange" : "取值范围为1~1000",
          "system_close" : "关闭",
          "joblog_clean_type_9" : "清理所有日志数据",
          "system_show" : "查看",
          "joblog_clean_type_8" : "清理十万条以前日志数据",
          "joblog_clean_type_7" : "清理三万条以前日志数据",
          "jobgroup_edit" : "编辑执行器",
          "jobinfo_opt_stop" : "停止",
          "jobinfo_script_location" : "脚本位置",
          "jobgroup_field_registryList_placeholder" : "请输入执行器地址列表，多地址逗号分隔",
          "jobinfo_field_childJobId_placeholder" : "请输入子任务的任务ID,如存在多个则逗号分隔",
          "joblog_clean_type" : "清理方式",
          "joblog_field_triggerMsg" : "调度备注",
          "user_update" : "更新用户",
          "login_remember_me" : "记住密码",
          "system_opt_suc" : "操作成功",
          "jobgroup_name" : "执行器管理",
          "daterangepicker_custom_daysofweek" : "日,一,二,三,四,五,六",
          "jobinfo_glue_remark" : "源码备注",
          "logout_success" : "注销成功",
          "jobinfo_field_timeout" : "任务超时时间",
          "jobgroup_list" : "执行器列表",
          "user_manage" : "用户管理",
          "login_password_placeholder" : "请输入登录密码",
          "jobconf_idleBeat" : "空闲检测",
          "system_empty" : "无",
          "joblog_rolling_log_failoften" : "终止请求Rolling日志,请求失败次数超上限,可刷新页面重新加载日志",
          "jobconf_callback_child_msg1" : "{0}/{1} [任务ID={2}], 触发{3}, 触发备注: {4}",
          "dataTable_sNext" : "下页",
          "joblog_name" : "调度日志",
          "admin_name_full" : "分布式任务调度平台XXL-JOB",
          "daterangepicker_ranges_yesterday" : "昨日",
          "logout_fail" : "注销失败",
          "system_success" : "成功",
          "login_param_empty" : "账号或密码为空",
          "jobgroup_add" : "新增执行器",
          "job_dashboard_trigger_num" : "调度次数",
          "jobinfo_opt_registryinfo" : "注册节点",
          "jobconf_monitor_alarm_content" : "告警内容",
          "joblog_status_all" : "全部",
          "jobconf_route_random" : "随机",
          "system_unvalid" : "非法",
          "jobinfo_opt_start" : "启动",
          "login_username_lt_4" : "登录账号不应低于4位",
          "joblog_kill_log_limit" : "调度失败，无法终止日志",
          "jobconf_callback_child_msg2" : "{0}/{1} [任务ID={2}], 触发失败, 触发备注: 任务ID格式错误 ",
          "dataTable_sZeroRecords" : "没有匹配结果",
          "jobconf_trigger_type_parent" : "父任务触发",
          "joblog_field_handleTime" : "执行时间",
          "jobconf_block_DISCARD_LATER" : "丢弃后续调度",
          "dataTable_sLengthMenu" : "每页 _MENU_ 条记录",
          "jobgroup_field_registryList" : "机器地址",
          "system_all" : "全部",
          "jobinfo_field_alarmemail_placeholder" : "请输入报警邮件，多个邮件地址则逗号分隔",
          "jobgroup_field_addressType" : "注册方式",
          "job_dashboard_job_num_tip" : "调度中心运行的任务数量",
          "joblog_lost_fail" : "任务结果丢失，标记失败",
          "system_opt_copy" : "复制",
          "schedule_type_fix_rate" : "固定速度",
          "jobconf_trigger_exe_regtype" : "执行器-注册方式",
          "misfire_strategy_fire_once_now" : "立即执行一次",
          "jobgroup_field_addressType_1" : "手动录入",
          "jobgroup_field_addressType_0" : "自动注册",
          "jobinfo_conf_schedule" : "调度配置",
          "joblog_rolling_log_triggerfail" : "任务发起调度失败，无法查看执行日志",
          "jobconf_monitor_alarm_type" : "调度失败",
          "dataTable_sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )",
          "schedule_type" : "调度类型",
          "jobinfo_opt_run" : "执行一次",
          "user_username" : "账号",
          "jobinfo_field_executorBlockStrategy" : "阻塞处理策略",
          "login_username_empty" : "请输入登录账号",
          "joblog_field_handleMsg" : "执行备注",
          "jobconf_trigger_type_misfire" : "调度过期补偿",
          "jobinfo_field_add" : "新增",
          "daterangepicker_ranges_today" : "今日",
          "system_save" : "保存",
          "admin_i18n" : "",
          "job_dashboard_rate_report" : "成功比例图",
          "joblog_field_triggerCode" : "调度结果",
          "system_status" : "状态",
          "system_please_input" : "请输入",
          "misfire_strategy" : "调度过期策略",
          "jobinfo_glue_gluetype_unvalid" : "该任务非GLUE模式",
          "schedule_type_none_limit_start" : "当前调度类型禁止启动",
          "system_add_fail" : "新增失败",
          "login_fail" : "登录失败",
          "jobinfo_glue_remark_limit" : "源码备注长度限制为4~100"
        };
	</script>

</#macro>

<#macro commonHeader>
	<header class="main-header">
		<a href="${request.contextPath}/" class="logo">
			<span class="logo-mini"><b>XXL</b></span>
			<span class="logo-lg"><b>${I18n.admin_name}</b></span>
		</a>
		<nav class="navbar navbar-static-top" role="navigation">

			<a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>

          	<div class="navbar-custom-menu">
				<ul class="nav navbar-nav">
					<#-- login user -->
                    <li class="dropdown">
                        <a href="javascript:" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                            ${I18n.system_welcome} ${Request["XXL_JOB_LOGIN_IDENTITY"].username}
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu" role="menu">
                            <li id="updatePwd" ><a href="javascript:">${I18n.change_pwd}</a></li>
                            <li id="logoutBtn" ><a href="javascript:">${I18n.logout_btn}</a></li>
                        </ul>
                    </li>
				</ul>
			</div>

		</nav>
	</header>

	<!-- 修改密码.模态框 -->
	<div class="modal fade" id="updatePwdModal" tabindex="-1" role="dialog"  aria-hidden="true">
		<div class="modal-dialog ">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" >${I18n.change_pwd}</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal form" role="form" >
						<div class="form-group">
							<label for="lastname" class="col-sm-2 control-label">${I18n.change_pwd_field_newpwd}<font color="red">*</font></label>
							<div class="col-sm-10"><input type="text" class="form-control" name="password" placeholder="${I18n.system_please_input} ${I18n.change_pwd_field_newpwd}" maxlength="18" ></div>
						</div>
						<hr>
						<div class="form-group">
							<div class="col-sm-offset-3 col-sm-6">
								<button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
								<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

</#macro>

<#macro commonLeft pageName >
	<!-- Left side column. contains the logo and sidebar -->
	<aside class="main-sidebar">
		<!-- sidebar: style can be found in sidebar.less -->
		<section class="sidebar">
			<!-- sidebar menu: : style can be found in sidebar.less -->
			<ul class="sidebar-menu">
                <li class="header">${I18n.system_nav}</li>
                <li class="nav-click <#if pageName == "index">active</#if>" ><a href="${request.contextPath}/"><i class="fa fa-circle-o text-aqua"></i><span>${I18n.job_dashboard_name}</span></a></li>
				<li class="nav-click <#if pageName == "jobinfo">active</#if>" ><a href="${request.contextPath}/jobinfo"><i class="fa fa-circle-o text-yellow"></i><span>${I18n.jobinfo_name}</span></a></li>
				<li class="nav-click <#if pageName == "joblog">active</#if>" ><a href="${request.contextPath}/joblog"><i class="fa fa-circle-o text-green"></i><span>${I18n.joblog_name}</span></a></li>
				<#if Request["XXL_JOB_LOGIN_IDENTITY"].role == 1>
                    <li class="nav-click <#if pageName == "jobgroup">active</#if>" ><a href="${request.contextPath}/jobgroup"><i class="fa fa-circle-o text-red"></i><span>${I18n.jobgroup_name}</span></a></li>
                    <li class="nav-click <#if pageName == "user">active</#if>" ><a href="${request.contextPath}/user"><i class="fa fa-circle-o text-purple"></i><span>${I18n.user_manage}</span></a></li>
				</#if>
				<li class="nav-click <#if pageName == "help">active</#if>" ><a href="${request.contextPath}/help"><i class="fa fa-circle-o text-gray"></i><span>${I18n.job_help}</span></a></li>
			</ul>
		</section>
		<!-- /.sidebar -->
	</aside>
</#macro>

<#macro commonControl >
	<!-- Control Sidebar -->
	<aside class="control-sidebar control-sidebar-dark">
		<!-- Create the tabs -->
		<ul class="nav nav-tabs nav-justified control-sidebar-tabs">
			<li class="active"><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
			<li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
		</ul>
		<!-- Tab panes -->
		<div class="tab-content">
			<!-- Home tab content -->
			<div class="tab-pane active" id="control-sidebar-home-tab">
				<h3 class="control-sidebar-heading">近期活动</h3>
				<ul class="control-sidebar-menu">
					<li>
						<a href="javascript::;">
							<i class="menu-icon fa fa-birthday-cake bg-red"></i>
							<div class="menu-info">
								<h4 class="control-sidebar-subheading">张三今天过生日</h4>
								<p>2015-09-10</p>
							</div>
						</a>
					</li>
					<li>
						<a href="javascript::;"> 
							<i class="menu-icon fa fa-user bg-yellow"></i>
							<div class="menu-info">
								<h4 class="control-sidebar-subheading">Frodo 更新了资料</h4>
								<p>更新手机号码 +1(800)555-1234</p>
							</div>
						</a>
					</li>
					<li>
						<a href="javascript::;"> 
							<i class="menu-icon fa fa-envelope-o bg-light-blue"></i>
							<div class="menu-info">
								<h4 class="control-sidebar-subheading">Nora 加入邮件列表</h4>
								<p>nora@example.com</p>
							</div>
						</a>
					</li>
					<li>
						<a href="javascript::;">
						<i class="menu-icon fa fa-file-code-o bg-green"></i>
						<div class="menu-info">
							<h4 class="control-sidebar-subheading">001号定时作业调度</h4>
							<p>5秒前执行</p>
						</div>
						</a>
					</li>
				</ul>
				<!-- /.control-sidebar-menu -->
			</div>
			<!-- /.tab-pane -->

			<!-- Settings tab content -->
			<div class="tab-pane" id="control-sidebar-settings-tab">
				<form method="post">
					<h3 class="control-sidebar-heading">个人设置</h3>
					<div class="form-group">
						<label class="control-sidebar-subheading"> 左侧菜单自适应
							<input type="checkbox" class="pull-right" checked>
						</label>
						<p>左侧菜单栏样式自适应</p>
					</div>
					<!-- /.form-group -->

				</form>
			</div>
			<!-- /.tab-pane -->
		</div>
	</aside>
	<!-- /.control-sidebar -->
	<!-- Add the sidebar's background. This div must be placed immediately after the control sidebar -->
	<div class="control-sidebar-bg"></div>
</#macro>

<#macro commonFooter >
	<footer class="main-footer">
        Powered by <b>XXL-JOB</b> ${I18n.admin_version}
		<div class="pull-right hidden-xs">
            <strong>Copyright &copy; 2015-${.now?string('yyyy')} &nbsp;
                <a href="https://www.xuxueli.com/" target="_blank" >xuxueli</a>
				&nbsp;
                <a href="https://github.com/xuxueli/xxl-job" target="_blank" >github</a>
            </strong><!-- All rights reserved. -->
		</div>
	</footer>
</#macro>
