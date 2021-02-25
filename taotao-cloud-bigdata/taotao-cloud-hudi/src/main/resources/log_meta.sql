-- 系统访问日志
CREATE TABLE log_meta
(
    id bigint(20)   NOT NULL AUTO_INCREMENT,
    meta_type    int    not null default 0 comment '元数据类型-0-行为数据日志 1-系统访问日志 2-订单操作日志
   3-支付操作日志 4-商品操作日志 5-用户操作日志
   6-订单表信息 7-商品表信息 8-用户表详细 9-支付表信息 ',
    field  varchar(128) not null comment '字段',
    field_type   varchar(128) not null comment '字段类型-string|int|bigint|double|array|datetime',
    field_desc   varchar(256) not null comment '字段描述',
    meta_version varchar(128) not null default '1.0' comment '元数据版本',
    status int    not null default 0 comment '0-可用 1-不可用',
    create_time  timestamp    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time  timestamp    null comment '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (1, 0, 'distinct_id', 'string', '用户ID-登录前为匿名 ID，一般为设备 ID，登录后为登录 ID', 1.0, 0, '2020-11-04 06:42:33', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (2, 0, 'source_type', 'string', '日志来源-web app mini', 1.0, 0, '2020-11-04 06:50:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (3, 0, 'type', 'string', '类型-track 等等', 1.0, 0, '2020-11-04 06:50:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (4, 0, 'event', 'string', '事件-ViewProduct 等等', 1.0, 0, '2020-11-04 06:50:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (5, 0, 'project', 'string', '项目-taotao-cloud-mimin-app 等等', 1.0, 0, '2020-11-04 06:50:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (6, 0, 'time', 'datetime', '事件触发的时间（精确：毫秒）', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (7, 0, 'app_state', 'string', 'App状态-App当前的状态 仅$AppStartPassively 具有此属性 如:background', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (8, 0, 'app_id', 'string', '应用唯一标识-App的标识 Android、iOS、小程序具有此属性 如:com.sensorsdata.demo', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (9, 0, 'app_version', 'string', '应用版本-App 的版本号 如:1.2', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (10, 0, 'is_first_day', 'boolean', '是否首日访问-事件是否在首日触发', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (11, 0, 'is_first_time', 'boolean', '是否首次触发事件-是否当前用户第一次触发此事件', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (12, 0, 'timezone_offset', 'int', '时区偏移量-设备所在的时区偏移量的分钟数 * -1 如:-480', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (13, 0, 'event_duration', 'int', '事件时长-事件的持续时长（单位：秒）', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (14, 0, 'latitude', 'double', '纬度-纬度', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (15, 0, 'longitude', 'double', '经度-经度', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (16, 0, 'title', 'string', '页面标题-Android：Activity的android:label属性值,iOS：ViewController的title或titleView上的文本信息', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (17, 0, 'screen_name', 'string', '页面名称-Android：Activity或Fragment对应的包名.类名,iOS: ViewController的类名', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (18, 0, 'url', 'string', '页面地址-Android(v3.2.8+ 支持)、iOS( v1.11.5 )：值同 $screen_name Web JS：windows.location.href', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (19, 0, 'url_query', 'string', '页面参数-H5 页面 URL 中”?“之后内容', 1.0, 0, '2020-11-04 06:50:57', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (20, 0, 'url_path', 'string', '页面路径-H5 页面 URL 中 ”/“ 和 ”?“ 之间内容', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (21, 0, 'referrer', 'string', '前向地址-上一页面的 $url 信息,微信小程序为上一页面的 $url_path', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (22, 0, 'referrer_host', 'string', '前向域名	-$referrer 中 host 部分', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (23, 0, 'user_agent', 'string', 'UserAgent', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (24, 0, 'scene', 'string', '启动场景-打开小程序时的场景信息', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (25, 0, 'share_depth', 'int', '分享次数-当前用户在分享链上的层 如.原始访问者为 1，通过原始访问者分享打开页面的的访问者为 2', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (26, 0, 'share_distinct_id', 'string', '分享者-当前小程序分享者的用户 ID''', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (27, 0, 'share_url_path', 'string', '分享路径-小程序被分享时的路径信息', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (28, 0, 'share_method', 'string', '分享时途径-小程序被分享时的途径，朋友圈分享、转发消息卡片等', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (29, 0, 'source_package_name', 'string', '来源应用包名-来源应用的包名', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (30, 0, 'bot_name', 'string', '爬虫名称-SDK 判断为爬虫访问时，才会尝试解析此值', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (31, 0, 'viewport_height', 'int', '视区高度-浏览器实际视区高度（单位：px）', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (32, 0, 'viewport_position', 'int', '视区距顶部的位置-单位：px 若页面没有滚动条，则此属性为 0', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (33, 0, 'viewport_width', 'int', '视区宽度-浏览器实际视区宽度（单位：px）', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (34, 0, 'item_join', 'string', 'Item匹配模式-对应维度表的名称', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (35, 0, 'receive_time', 'datetime', '接收时间-服务端接收到该条事件的时间', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (36, 0, 'pp_crashed_reason', 'string', '崩溃原因-App 崩溃时的调用栈信息''', 1.0, 0, '2020-11-04 06:55:20', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (37, 0, 'brand', 'string', '设备品牌-Huawei，HONOR 等', 1.0, 0, '2020-11-04 06:58:30', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (38, 0, 'manufacturer', 'string', '设备制造商-Apple，Huawei 等', 1.0, 0, '2020-11-04 06:58:30', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (39, 0, 'model', 'string', '设备型号-iPhone8,2等', 1.0, 0, '2020-11-04 06:58:30', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (40, 0, 'os', 'string', '操作系统', 1.0, 0, '2020-11-04 06:58:30', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (41, 0, 'os_version', 'string', '操作系统版本', 1.0, 0, '2020-11-04 06:58:30', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (42, 0, 'screen_height', 'int', '屏幕高度', 1.0, 0, '2020-11-04 06:58:30', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (43, 0, 'screen_width', 'int', '屏幕宽度', 1.0, 0, '2020-11-04 06:58:30', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (44, 0, 'wifi', 'boolean', '是否WIFI', 1.0, 0, '2020-11-04 06:58:30', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (45, 0, 'carrier', 'string', '运营商-设备运营商信息', 1.0, 0, '2020-11-04 06:58:30', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (46, 0, 'network_type', 'string', '网络类型', 1.0, 0, '2020-11-04 06:58:30', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (47, 0, 'device_id', 'string', '设备ID-Android：AndroidID iOS：IDFA 或 IDF', 1.0, 0, '2020-11-04 06:58:30', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (48, 0, 'screen_orientation', 'string', '屏幕方向-设备当前方向（横屏或竖屏）', 1.0, 0, '2020-11-04 06:58:30', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (52, 0, 'browser', 'string', '浏览器', 1.0, 0, '2020-11-04 07:00:25', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (53, 0, 'browser_version', 'string', '浏览器版本', 1.0, 0, '2020-11-04 07:00:25', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (54, 0, 'element_id', 'string', '元素ID -Android：控件 android:id', 1.0, 0, '2020-11-04 07:00:25', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (55, 0, 'element_name', 'string', '元素名称', 1.0, 0, '2020-11-04 07:00:25', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (56, 0, 'element_type', 'string', '元素类型', 1.0, 0, '2020-11-04 07:00:25', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (57, 0, 'element_content', 'string', '元素内容', 1.0, 0, '2020-11-04 07:00:25', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (58, 0, 'element_position', 'string', '元素位置-被点击元素在列表控件内的位置', 1.0, 0, '2020-11-04 07:00:25', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (59, 0, 'element_selector', 'string', '元素选择器-元素的路径信息', 1.0, 0, '2020-11-04 07:00:25', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (60, 0, 'element_target_url', 'string', '元素链接地址', 1.0, 0, '2020-11-04 07:00:25', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (61, 0, 'element_class_name', 'string', '元素样式名', 1.0, 0, '2020-11-04 07:00:25', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (62, 0, 'latest_referrer', 'string', '最近一次站外地址', 1.0, 0, '2020-11-04 07:00:25', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (63, 0, 'latest_referrer_host', 'string', '最近一次站外域名', 1.0, 0, '2020-11-04 07:01:50', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (64, 0, 'latest_search_keyword', 'string', '最近一次搜索引擎关键词-由 $latest_referrer 解析出的搜索关键词', 1.0, 0, '2020-11-04 07:01:51', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (65, 0, 'latest_traffic_source_type', 'string', '最近一次流量来源类型', 1.0, 0, '2020-11-04 07:01:51', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (66, 0, 'latest_landing_page ', 'string', '最近一次落地页', 1.0, 0, '2020-11-04 07:01:51', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (67, 0, 'latest_utm_campaign ', 'string', '最近一次广告系列名称', 1.0, 0, '2020-11-04 07:01:51', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (68, 0, 'latest_utm_content', 'string', '最近一次广告系列内容', 1.0, 0, '2020-11-04 07:01:51', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (69, 0, 'latest_utm_medium', 'string', '最近一次广告系列媒介', 1.0, 0, '2020-11-04 07:01:51', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (70, 0, 'latest_utm_term', 'string', '最近一次广告系列字词', 1.0, 0, '2020-11-04 07:01:51', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (71, 0, 'latest_scene', 'string', '最近一次启动场景', 1.0, 0, '2020-11-04 07:01:51', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (72, 0, 'latest_share_method ', 'string', '最近一次分享时途径', 1.0, 0, '2020-11-04 07:01:51', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (73, 0, 'lib', 'string', 'SDK类型	', 1.0, 0, '2020-11-04 07:03:11', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (74, 0, 'lib_version', 'string', 'SDK版本', 1.0, 0, '2020-11-04 07:03:11', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (75, 0, 'lib_method ', 'string', '埋点方式-埋点的触发方式（全埋点、自定义埋点）', 1.0, 0, '2020-11-04 07:03:11', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (76, 0, 'lib_detail ', 'string', '埋点细节-触发埋点时的调用栈信息', 1.0, 0, '2020-11-04 07:03:12', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (77, 0, 'ios_install_source', 'string', 'App渠道匹配所需要的设备指纹信息-Android：IMEI 信息 iOS：IDFA 信息', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (78, 0, 'channel_device_info', 'string', 'App 渠道匹配所需要的设备指纹信息（与上方事件变量名不同）', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (79, 0, 'ios_install_disable_callback', 'boolean', '是否不进行追踪回调', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (80, 0, 'is_channel_callback_event', 'boolean', '是否进行渠道匹配回调-事件渠道匹配结果是否需要回调给渠道商', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (81, 0, 'channel_extra_information', 'string', '渠道额外信息-渠道监测链接中添加的额外信息 如 IMEI、OAID', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (82, 0, 'utm_matching_type', 'string', '渠道追踪匹配模式-模糊匹配、精准匹配', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (83, 0, 'utm_source', 'string', '广告系列来源', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (84, 0, 'utm_medium', 'string', '广告系列媒介', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (85, 0, 'utm_term', 'string', '广告系列字词', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (86, 0, 'utm_content', 'string', '广告系列内容', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (87, 0, 'utm_campaign', 'string', '广告系列名称', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (88, 0, 'matched_key', 'string', '渠道匹配关键字-渠道匹配成功后，用于匹配的关键字段信息 如 md5 后的 IMEI，IP_UA 等信息', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (89, 0, 'matching_key_list', 'string', '渠道匹配关键字列表-用于匹配的关键字段列表 元素为 md5 后的 IMEI，IP_UA 等信息 渠道追踪时会使用各字段在列表内的顺序作为匹配优先级', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (90, 0, 'short_url_key', 'string', '短链Key-短链 URL 中 path 的最后一个节点值', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (91, 0, 'short_url_target', 'string', '短链目标地址-短链所对应的完整 URL', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (92, 0, 'ip', 'string', 'IP', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (93, 0, 'city', 'string', '城市', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (94, 0, 'province', 'string', '省份', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (95, 0, 'country ', 'string', '国家', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (96, 0, 'ip_isp', 'string', 'IP运营商', 1.0, 0, '2020-11-04 07:05:42', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (97, 0, 'is_login_id ', 'boolean', '是否登录 ID-事件的 distinct_id 是否为登录 ID', 1.0, 0, '2020-11-04 07:06:49', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (98, 0, 'track_signup_original_id', 'string', '关联原始 ID-用户登录时，用于关联的匿名 ID', 1.0, 0, '2020-11-04 07:06:49', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (99, 0, 'is_valid', 'boolean', '是否封禁', 1.0, 0, '2020-11-04 07:06:49', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (100, 0, 'update_time', 'datetime', '更新时间', 1.0, 0, '2020-11-04 07:06:50', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (101, 1, 'traceId', 'string', '请求日志id', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (102, 1, 'applicationName', 'string', '服务名称', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (103, 1, 'requestIp', 'string', '请求IP', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (104, 1, 'type', 'int', '操作类型 1 操作记录 2异常记录', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (105, 1, 'userName ', 'string', '操作人name', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (106, 1, 'userId', 'string', '操作人ID', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (107, 1, 'clientId ', 'string', '客户端ID', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (108, 1, 'description', 'string', '操作描述', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (109, 1, 'actionMetho', 'string', '请求方法', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (110, 1, 'actionUrl', 'string', '请求url', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (111, 1, 'params', 'string', '请求参数', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (112, 1, 'ua ', 'string', '浏览器', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (113, 1, 'classPath', 'string', '类路径', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (114, 1, 'requestMethod', 'string', '请求方法', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (115, 1, 'operateTyp', 'string', '操作类型（1查询/获取，2添加，3修改，4删除）', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (116, 1, 'startTime', 'datetime', '开始时间', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (117, 1, 'finishTime', 'datetime', '完成时间', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (118, 1, 'consumingTime', 'int', '消耗时间', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (119, 1, 'exDetail ', 'string', '异常详情信息 堆栈信息', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (120, 1, 'exDesc', 'string', '异常描述 e.getMessage', 1.0, 0, '2020-11-04 07:08:56', null);
insert into log_meta (id, meta_type, field, field_type, field_desc, meta_version, status, create_time, update_time) values (121, 1, 'tenantId ', 'string', '租户id', 1.0, 0, '2020-11-04 07:08:56', null);
