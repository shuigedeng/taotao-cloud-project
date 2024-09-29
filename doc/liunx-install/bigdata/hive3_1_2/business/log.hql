-- 源日志json格式
-- {   "ctime": 1606277316432,
--     "project": "taotao-cloud-backend",
--     "content": {
--         "login_id": "11111111111111",
--         "_track_id": 63276154,
--         "lib": {
--             "$lib": "js",
--             "$lib_method": "code",
--             "$lib_version": "1.15.16"
--             },
--         "distinct_id": "11111111111111",
--         "anonymous_id": "174489e063d961-0903670dbb0a25-15306251-1296000-174489e063ed42",
--         "original_id": "174489e063d961-0903670dbb0a25-15306251-1296000-174489e063ed42",
--         "type": "track_signup",
--         "event": "$SignUp",
--         "properties": {
--             "$screen_width": 1440,
--             "project": "taotao-cloud-backend",
--             "$screen_height": 900,
--             "$lib": "js",
--             "current_url": "http://localhost:3000/login",
--             "$device_id": "174489e063d961-0903670dbb0a25-15306251-1296000-174489e063ed42",
--             "$latest_landing_page": "url???domain????????????",
--             "$title": "React Index",
--             "$timezone_offset": -480,
--             "$lib_version": "1.15.16",
--             "$latest_search_keyword": "url???domain????????????",
--             "$latest_traffic_source_type": "url???domain????????????",
--             "$url": "http://localhost:3000/login",
--             "$latest_referrer": "url???domain????????????",
--             "$latest_referrer_host": "????????????"
--             }
--         }
--     }
-- }


-- 系统元数据表
create external TABLE if not exists taotao_cloud_meta
(
    meta_type     int comment    '元数据类型 0-行为数据日志 1-系统访问日志 2-订单操作日志
                                    3-支付操作日志 4-商品操作日志 5-用户操作日志
                                    6-订单表信息 7-商品表信息 8-用户表详细 9-支付表信息 ',
    field         string comment '字段名称',
    field_type    string comment '字段类型',
    field_desc    string comment '字段描述',
    app_version   string comment '版本号',
    status        int comment '状态 0下线 1上线',
    create_time   string comment '创建时间',
)
partitioned by (logday string)
stored as parquet
location "/taotao/cloud/meta";

-- 行为数据源日志表(只是描述信息项目中不能用  有$符号)
create external TABLE if not exists taotao_cloud_access_log_source
(
    project string,
    ctime   bigint,
    content  struct<login_id: string,
                    track_id: bigint,
                    lib: struct<$lib: string,
                                $lib_method: string,
                                $lib_version: string>,
                    distinct_id: string,
                    anonymous_id: string,
                    original_id: string,
                    type: string,
                    event: string,
                    properties: struct<$screen_width: int,
                                       project: string,
                                       current_url: string,
                                       $lib: string,
                                       $device_id: string,
                                       $latest_landing_page: string,
                                       $title: string,
                                       $timezone_offset: int,
                                       $lib_version: string,
                                       $latest_search_keyword: string,
                                       $latest_traffic_source_type: string,
                                       $url: string,
                                       $latest_referrer: string,
                                       $latest_referrer_host: string,
                                       $screen_height: int>>
)
partitioned by (logday string)
row format serde "org.openx.data.jsonserde.JsonSerDe"
location "/taotao/cloud/access/log/sources";


-- 行为数据日志转换表(和源数据合并之后的数据)
create external TABLE if not exists taotao_cloud_access_log_transform
(
    -- 基础信息
    ctime                        timestamp comment '时间--事件触发的时间',
    project                      string comment '项目--taotao-cloud-mimin-app',
    distinct_id                  string comment '用户ID--登录前为匿名 ID，一般为设备 ID，登录后为登录 ID',
    source_type                  string comment '日志来源--web app mini',
    type                         string comment '类型--track',
    event                        string comment '事件--ViewProduct',
    app_state                    string default 'background' comment 'App状态--App当前的状态 仅$AppStartPassively 具有此属性',
    app_id                       string default 'com.sensorsdata.demo' comment '应用唯一标识--App的标识 Android、iOS、小程序具有此属性',
    app_version                  string default '1.2' comment '应用版本--App 的版本号',
    is_first_day                 string comment '是否首日访问--事件是否在首日触发',
    is_first_time                string comment '是否首次触发事件--是否当前用户第一次触发此事件',
    timezone_offset              string default '-480' comment '时区偏移量--设备所在的时区偏移量的分钟数 * -1',
    event_duration               string comment '事件时长--事件的持续时长（单位：秒）',
    latitude                     string comment '纬度--纬度',
    longitude                    string comment '经度--经度',
    title                        string comment '页面标题--Android：Activity的android:label属性值,iOS：ViewController的title或titleView上的文本信息',
    screen_name                  string comment '页面名称--Android：Activity或Fragment对应的包名.类名,iOS: ViewController的类名',
    url                          string comment '页面地址--Android(v3.2.8+ 支持)、iOS( v1.11.5 )：值同 $screen_name Web JS：windows.location.href',
    url_query                    string comment '页面参数--H5 页面 URL 中”?“之后内容',
    url_path                     string comment '页面路径--H5 页面 URL 中 ”/“ 和 ”?“ 之间内容',
    referrer                     string comment '前向地址--上一页面的 $url 信息,微信小程序为上一页面的 $url_path',
    referrer_host                string comment '前向域名	--$referrer 中 host 部分',
    user_agent                   string comment 'UserAgent',
    scene                        string comment '启动场景--打开小程序时的场景信息',
    share_depth                  string comment '分享次数--当前用户在分享链上的层 如.原始访问者为 1，通过原始访问者分享打开页面的的访问者为 2',
    share_distinct_id            string comment '分享者--当前小程序分享者的用户 ID',
    share_url_path               string comment '分享路径--小程序被分享时的路径信息',
    share_method                 string comment '分享时途径--小程序被分享时的途径，朋友圈分享、转发消息卡片等',
    source_package_name          string comment '来源应用包名--来源应用的包名',
    bot_name                     string comment '爬虫名称--SDK 判断为爬虫访问时，才会尝试解析此值',
    viewport_height              string comment '视区高度--浏览器实际视区高度（单位：px）',
    viewport_position            string comment '视区距顶部的位置--单位：px 若页面没有滚动条，则此属性为 0',
    viewport_width               string comment '视区宽度--浏览器实际视区宽度（单位：px）',
    item_join                    string comment 'Item匹配模式--对应维度表的名称',
    receive_time                 string comment '接收时间--服务端接收到该条事件的时间',
    app_crashed_reason           string comment '崩溃原因--App 崩溃时的调用栈信息',
    -- 设备信息
    brand                        string comment '设备品牌--Huawei，HONOR 等',
    manufacturer                 string comment '设备制造商--Apple，Huawei 等',
    model                        string comment '设备型号--iPhone8,2等',
    os                           string comment '操作系统',
    os_version                   string comment '操作系统版本',
    screen_height                string comment '屏幕高度',
    screen_width                 string comment '屏幕宽度',
    wifi                         string comment '是否WIFI',
    carrier                      string comment '运营商--设备运营商信息',
    network_type                 string comment '网络类型',
    device_id                    string comment '设备ID--Android：AndroidID iOS：IDFA 或 IDF',
    screen_orientation           string comment '屏幕方向--设备当前方向（横屏或竖屏）',
    -- 浏览器信息
    browser                      string comment '浏览器',
    browser_version              string comment '浏览器版本',
    -- 点击事件相关信息
    element_id                   string comment '元素ID --Android：控件 android:id	',
    element_name                 string comment '元素名称',
    element_type                 string comment '元素类型',
    element_content              string comment '元素内容',
    element_position             string comment '元素位置--被点击元素在列表控件内的位置',
    element_selector             string comment '元素选择器--元素的路径信息',
    element_target_url           string comment '元素链接地址',
    element_class_name           string comment '元素样式名',
    -- latest相关信息
    latest_referrer              string comment '最近一次站外地址',
    latest_referrer_host         string comment '最近一次站外域名',
    latest_search_keyword        string comment '最近一次搜索引擎关键词--由 $latest_referrer 解析出的搜索关键词',
    latest_traffic_source_type   string comment '最近一次流量来源类型',
    latest_landing_page          string comment '最近一次落地页',
    latest_utm_campaign          string comment '最近一次广告系列名称',
    latest_utm_content           string comment '最近一次广告系列内容',
    latest_utm_medium            string comment '最近一次广告系列媒介',
    latest_utm_term              string comment '最近一次广告系列字词',
    latest_scene                 string comment '最近一次启动场景',
    latest_share_method          string comment '最近一次分享时途径',
    -- SDK相关信息
    lib                          string comment 'SDK类型	',
    lib_version                  string comment 'SDK版本',
    lib_method                   string comment '埋点方式--埋点的触发方式（全埋点、自定义埋点）',
    lib_detail                   string comment '埋点细节--触发埋点时的调用栈信息',
    -- 渠道相关相关信息
    ios_install_source           string comment 'App渠道匹配所需要的设备指纹信息--Android：IMEI 信息 iOS：IDFA 信息',
    channel_device_info          string comment 'App 渠道匹配所需要的设备指纹信息（与上方事件变量名不同）',
    ios_install_disable_callback string comment '是否不进行追踪回调',
    is_channel_callback_event    string comment '是否进行渠道匹配回调--事件渠道匹配结果是否需要回调给渠道商',
    channel_extra_information    string comment '渠道额外信息--渠道监测链接中添加的额外信息 如 IMEI、OAID',
    utm_matching_type            string comment '渠道追踪匹配模式--模糊匹配、精准匹配',
    utm_source                   string comment '广告系列来源',
    utm_medium                   string comment '广告系列媒介',
    utm_term                     string comment '广告系列字词',
    utm_content                  string comment '广告系列内容',
    utm_campaign                 string comment '广告系列名称',
    matched_key                  string comment '渠道匹配关键字--渠道匹配成功后，用于匹配的关键字段信息 如 md5 后的 IMEI，IP_UA 等信息',
    matching_key_list            string comment '渠道匹配关键字列表--用于匹配的关键字段列表 元素为 md5 后的 IMEI，IP_UA 等信息 渠道追踪时会使用各字段在列表内的顺序作为匹配优先级',
    short_url_key                string comment '短链Key--短链 URL 中 path 的最后一个节点值',
    short_url_target             string comment '短链目标地址--短链所对应的完整 URL',
    -- IP相关信息
    ip                           string comment 'IP',
    city                         string comment '城市',
    province                     string comment '省份',
    country                      string comment '国家',
    ip_isp                       string comment 'IP运营商',
    -- 用户相关信息
    is_login_id                  string comment '是否登录 ID--事件的 distinct_id 是否为登录 ID',
    track_signup_original_id     string comment '关联原始 ID--用户登录时，用于关联的匿名 ID',
    -- item相关信息
    is_valid                     string comment '是否封禁'
)
partitioned by (logday string)
row format serde "org.openx.data.jsonserde.JsonSerDe"
with serdepoperties ('ingore.malformed.json' = 'true')
location "/taotao/cloud/access/log/transform";


-- 行为数据日志
create external TABLE if not exists taotao_cloud_access_log_parquet
(
    -- 基础信息
    ctime                        timestamp comment '时间--事件触发的时间',
    project                      string comment '项目--taotao-cloud-mimin-app',
    distinct_id                  string comment '用户ID--登录前为匿名 ID，一般为设备 ID，登录后为登录 ID',
    source_type                  string comment '日志来源--web app mini',
    type                         string comment '类型--track',
    event                        string comment '事件--ViewProduct',
    app_state                    string default 'background' comment 'App状态--App当前的状态 仅$AppStartPassively 具有此属性',
    app_id                       string default 'com.sensorsdata.demo' comment '应用唯一标识--App的标识 Android、iOS、小程序具有此属性',
    app_version                  string default '1.2' comment '应用版本--App 的版本号',
    is_first_day                 string comment '是否首日访问--事件是否在首日触发',
    is_first_time                string comment '是否首次触发事件--是否当前用户第一次触发此事件',
    timezone_offset              string default '-480' comment '时区偏移量--设备所在的时区偏移量的分钟数 * -1',
    event_duration               string comment '事件时长--事件的持续时长（单位：秒）',
    latitude                     string comment '纬度--纬度',
    longitude                    string comment '经度--经度',
    title                        string comment '页面标题--Android：Activity的android:label属性值,iOS：ViewController的title或titleView上的文本信息',
    screen_name                  string comment '页面名称--Android：Activity或Fragment对应的包名.类名,iOS: ViewController的类名',
    url                          string comment '页面地址--Android(v3.2.8+ 支持)、iOS( v1.11.5 )：值同 $screen_name Web JS：windows.location.href',
    url_query                    string comment '页面参数--H5 页面 URL 中”?“之后内容',
    url_path                     string comment '页面路径--H5 页面 URL 中 ”/“ 和 ”?“ 之间内容',
    referrer                     string comment '前向地址--上一页面的 $url 信息,微信小程序为上一页面的 $url_path',
    referrer_host                string comment '前向域名	--$referrer 中 host 部分',
    user_agent                   string comment 'UserAgent',
    scene                        string comment '启动场景--打开小程序时的场景信息',
    share_depth                  string comment '分享次数--当前用户在分享链上的层 如.原始访问者为 1，通过原始访问者分享打开页面的的访问者为 2',
    share_distinct_id            string comment '分享者--当前小程序分享者的用户 ID',
    share_url_path               string comment '分享路径--小程序被分享时的路径信息',
    share_method                 string comment '分享时途径--小程序被分享时的途径，朋友圈分享、转发消息卡片等',
    source_package_name          string comment '来源应用包名--来源应用的包名',
    bot_name                     string comment '爬虫名称--SDK 判断为爬虫访问时，才会尝试解析此值',
    viewport_height              string comment '视区高度--浏览器实际视区高度（单位：px）',
    viewport_position            string comment '视区距顶部的位置--单位：px 若页面没有滚动条，则此属性为 0',
    viewport_width               string comment '视区宽度--浏览器实际视区宽度（单位：px）',
    item_join                    string comment 'Item匹配模式--对应维度表的名称',
    receive_time                 string comment '接收时间--服务端接收到该条事件的时间',
    app_crashed_reason           string comment '崩溃原因--App 崩溃时的调用栈信息',
    -- 设备信息
    brand                        string comment '设备品牌--Huawei，HONOR 等',
    manufacturer                 string comment '设备制造商--Apple，Huawei 等',
    model                        string comment '设备型号--iPhone8,2等',
    os                           string comment '操作系统',
    os_version                   string comment '操作系统版本',
    screen_height                string comment '屏幕高度',
    screen_width                 string comment '屏幕宽度',
    wifi                         string comment '是否WIFI',
    carrier                      string comment '运营商--设备运营商信息',
    network_type                 string comment '网络类型',
    device_id                    string comment '设备ID--Android：AndroidID iOS：IDFA 或 IDF',
    screen_orientation           string comment '屏幕方向--设备当前方向（横屏或竖屏）',
    -- 浏览器信息
    browser                      string comment '浏览器',
    browser_version              string comment '浏览器版本',
    -- 点击事件相关信息
    element_id                   string comment '元素ID --Android：控件 android:id	',
    element_name                 string comment '元素名称',
    element_type                 string comment '元素类型',
    element_content              string comment '元素内容',
    element_position             string comment '元素位置--被点击元素在列表控件内的位置',
    element_selector             string comment '元素选择器--元素的路径信息',
    element_target_url           string comment '元素链接地址',
    element_class_name           string comment '元素样式名',
    -- latest相关信息
    latest_referrer              string comment '最近一次站外地址',
    latest_referrer_host         string comment '最近一次站外域名',
    latest_search_keyword        string comment '最近一次搜索引擎关键词--由 $latest_referrer 解析出的搜索关键词',
    latest_traffic_source_type   string comment '最近一次流量来源类型',
    latest_landing_page          string comment '最近一次落地页',
    latest_utm_campaign          string comment '最近一次广告系列名称',
    latest_utm_content           string comment '最近一次广告系列内容',
    latest_utm_medium            string comment '最近一次广告系列媒介',
    latest_utm_term              string comment '最近一次广告系列字词',
    latest_scene                 string comment '最近一次启动场景',
    latest_share_method          string comment '最近一次分享时途径',
    -- SDK相关信息
    lib                          string comment 'SDK类型	',
    lib_version                  string comment 'SDK版本',
    lib_method                   string comment '埋点方式--埋点的触发方式（全埋点、自定义埋点）',
    lib_detail                   string comment '埋点细节--触发埋点时的调用栈信息',
    -- 渠道相关相关信息
    ios_install_source           string comment 'App渠道匹配所需要的设备指纹信息--Android：IMEI 信息 iOS：IDFA 信息',
    channel_device_info          string comment 'App 渠道匹配所需要的设备指纹信息（与上方事件变量名不同）',
    ios_install_disable_callback string comment '是否不进行追踪回调',
    is_channel_callback_event    string comment '是否进行渠道匹配回调--事件渠道匹配结果是否需要回调给渠道商',
    channel_extra_information    string comment '渠道额外信息--渠道监测链接中添加的额外信息 如 IMEI、OAID',
    utm_matching_type            string comment '渠道追踪匹配模式--模糊匹配、精准匹配',
    utm_source                   string comment '广告系列来源',
    utm_medium                   string comment '广告系列媒介',
    utm_term                     string comment '广告系列字词',
    utm_content                  string comment '广告系列内容',
    utm_campaign                 string comment '广告系列名称',
    matched_key                  string comment '渠道匹配关键字--渠道匹配成功后，用于匹配的关键字段信息 如 md5 后的 IMEI，IP_UA 等信息',
    matching_key_list            string comment '渠道匹配关键字列表--用于匹配的关键字段列表 元素为 md5 后的 IMEI，IP_UA 等信息 渠道追踪时会使用各字段在列表内的顺序作为匹配优先级',
    short_url_key                string comment '短链Key--短链 URL 中 path 的最后一个节点值',
    short_url_target             string comment '短链目标地址--短链所对应的完整 URL',
    -- IP相关信息
    ip                           string comment 'IP',
    city                         string comment '城市',
    province                     string comment '省份',
    country                      string comment '国家',
    ip_isp                       string comment 'IP运营商',
    -- 用户相关信息
    is_login_id                  string comment '是否登录 ID--事件的 distinct_id 是否为登录 ID',
    track_signup_original_id     string comment '关联原始 ID--用户登录时，用于关联的匿名 ID',
    -- item相关信息
    is_valid                     string comment '是否封禁'
)
    partitioned by (logday string)
    stored as parquet
    location "/taotao/cloud/access/log/parquet";

-- 系统请求日志源数据表
create external TABLE if not exists taotao_cloud_request_log_source
(
    trace_id          string comment '请求日志id',
    application_name  string comment '服务名称',
    request_ip        string comment '请求IP',
    type              int comment '操作类型 1 操作记录 2异常记录',
    username          string comment '操作人name',
    user_id           bigint comment '操作人ID',
    client_id         string comment '客户端ID',
    description       string comment '操作描述',
    action_method     string comment '请求方法',
    action_url        string comment '请求url',
    params            string comment '请求参数',
    ua                string comment '浏览器',
    classpath         string comment '类路径',
    request_method    string comment '请求方法',
    operate_type      int comment '操作类型（1查询/获取，2添加，3修改，4删除）',
    start_time        timestamp comment '开始时间',
    finish_time       timestamp comment '完成时间',
    consuming_time    bigint comment '消耗时间',
    ex_detail         string comment '异常详情信息 堆栈信息',
    ex_desc           string comment '异常描述 e.getMessage',
    tenant_id         string comment '租户id'
)
partitioned by (logday string)
row format serde "org.openx.data.jsonserde.JsonSerDe"
with serdepoperties ('ingore.malformed.json' = 'true')
location "/taotao/cloud/request/log/sources";

-- 系统请求日志源数据表
create external TABLE if not exists taotao_cloud_request_log_parquet
(
    trace_id          string comment '请求日志id',
    application_name  string comment '服务名称',
    request_ip        string comment '请求IP',
    type              int comment '操作类型 1 操作记录 2异常记录',
    username          string comment '操作人name',
    user_id           bigint comment '操作人ID',
    client_id         string comment '客户端ID',
    description       string comment '操作描述',
    action_method     string comment '请求方法',
    action_url        string comment '请求url',
    params            string comment '请求参数',
    ua                string comment '浏览器',
    classpath         string comment '类路径',
    request_method    string comment '请求方法',
    operate_type      int comment '操作类型（1查询/获取，2添加，3修改，4删除）',
    start_time        bigint comment '开始时间',
    finish_time       bigint comment '完成时间',
    consuming_time    bigint comment '消耗时间',
    ex_detail         string comment '异常详情信息 堆栈信息',
    ex_desc           string comment '异常描述 e.getMessage',
    tenant_id         string comment '租户id'
)
partitioned by (logday string)
stored as parquet
location "/taotao/cloud/request/log/parquet";

-- 系统日志源数据表
create external TABLE if not exists taotao_cloud_sys_log_source
(
    application_name string comment '服务名称',
    trace_id         string comment '请求日志id',
    server_ip        string comment '服务器ip',
    server_port      string comment '服务器端口',
    timestamp        timestamp comment '时间戳',
    thread           string comment '线程',
    pid              string comment 'pid',
    parent_span_id   string comment 'parentSpanId',
    span_id          string comment 'span_id',
    exportable       string comment 'exportable',
    logger           string comment 'logger',
    level            string comment 'level',
    message          string comment 'message',
    host             string comment 'host',
    stack_trace      string comment 'stack_trace'
)
partitioned by (logday string)
row format serde "org.openx.data.jsonserde.JsonSerDe"
with serdepoperties ('ingore.malformed.json' = 'true')
location "/taotao/cloud/sys/log/sources";

-- 系统日志parquet数据表
create external TABLE if not exists taotao_cloud_sys_log_parquet
(
      application_name string comment '服务名称',
      trace_id         string comment '请求日志id',
      server_ip        string comment '服务器ip',
      server_port      string comment '服务器端口',
      timestamp        timestamp comment '时间戳',
      thread           string comment '线程',
      pid              string comment 'pid',
      parent_span_id   string comment 'parentSpanId',
      span_id          string comment 'span_id',
      exportable       string comment 'exportable',
      logger           string comment 'logger',
      level            string comment 'level',
      message          string comment 'message',
      host             string comment 'host',
      stack_trace      string comment 'stack_trace'
)
partitioned by (logday string)
stored as parquet
location "/taotao/cloud/sys/log/parquet";

-- 订单日志表
create external TABLE if not exists taotao_cloud_biz_order_log_parquet
(
    order_id    bigint comment '订单id',
    member_id   bigint comment '用户id',
    type        int  comment '类型 订单创建 订单支付 订单发货 订单签收 订单评价
                            订单完成 订单取消 订单编辑 订单自动签收 订单自动评价
                            订单自动完成 订单自动取消',
    msg         string  comment '备注',
    data        string  comment 'json数据',
    ctime       timestamp  comment '创建时间'
)
 partitioned by (logday string)
 stored as parquet
 location "/taotao/cloud/biz/order/log/parquet";

-- 用户登录日志表
create external TABLE if not exists taotao_cloud_biz_member_login_log_parquet
(
    member_id      bigint comment '用户id',
    login_time     timestamp  comment '登录时间',
    login_ip       string  comment '登录ip',
    login_status   int  comment '登录状态'
)
 partitioned by (logday string)
 stored as parquet
 location "/taotao/cloud/biz/member/login/log/parquet";
