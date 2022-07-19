### 构建数据仓库

### 1. hive安装
主要包括

1.安装

2.配置文件

3.安装时需要支持json库 jsonSerde 查看.hiverc

### 2.创建ods_news库
```create database if not exists ods_news```

### 3.创建ods_news.news表
```
-- 日志源表
create external TABLE if not exists news
(
    project string,
    ctime   bigint,
    content  struct<distinct_id: string,
                    event: bigint,
                    properties: struct<model: int,
                                       network_type: string,
                                       is_charging: string,
                                       app_version: string,
                                       element_name: string,
                                       element_page: string,
                                       carrier: string,
                                       os: string,
                                       imei: string,
                                       battery_level: string,
                                       screen_width: string,
                                       screen_height: string,
                                       device_id: string,
                                       client_time: string,
                                       ip: string,
                                       wifi: string,
                                       manufacturer: string>>
)
partitioned by (logday string)
row format serde "org.openx.data.jsonserde.JsonSerDe"
with serdeproperties ( 'ignore.malformed.json' = 'true')
location "/taotao/cloud/access/log/sources";

分区映射
alter table ods_news.news add partition(logday=20200817) location 'hdfs:/taotao/cloud/access/log/sources'
```

### 3.自动添加分区脚本

log_add_partition.sh

### 4.创建行为数据加速表(parquet格式)
```
-- 加速表
create external TABLE if not exists taotao_cloud_access_log_parquet
(
    event                        string comment '事件名称',
    ctime                        string comment '服务器端接受到事件时间',
    distinct_id                  string comment '用户id',
    model                        string comment '手机型号',
    network_type                 string comment '网络类型',
    is_charging                  string comment '是否充电中',
    app_version                  string comment 'app版本',
    element_name                 string comment '元素名称',
    element_page                 string comment '元素所在页面',
    carrier                      string comment '运营商',
    os                           string comment '操作系统',
    imei                         string comment '手机标识imei',
    battery_level                string comment '手机电量',
    screen_width                 string comment '屏幕宽度',
    screen_height                string comment '屏幕高度',
    device_id                    string comment '手机设备id 目前和imei一致',
    client_time                  string comment '日志上报事件',
    ip                           string comment 'ip',
    wifi                         string comment '是否是wifi',
    manufacturer                 string comment '手机制造商',
)
    partitioned by (logday string)
    stored as parquet
    location "/taotao/cloud/access/log/parquet";
```

### 5.自动添加行为数据加速表分区脚本

log_parquet.sh

### 6.创建ods_news.news_article表
```
-- news_article表
create external TABLE if not exists news_article
(
    article_id  string comment 'article_id',
    type_name   string comment '新闻类型',
    pub_time   string comment '发布时间',
    title   string comment '标题',
    source_name   string comment '来源',
    tags   string comment '标签',
)
partitioned by (logday string)
row format serde "org.openx.data.jsonserde.JsonSerDe"
# 是否忽略json解析失败的错误
with serdeproperties ( 'ignore.malformed.json' = 'true')
location "/taotao/cloud/access/log/article";

分区映射
alter table ods_news.news add partition(logday=20200817) location 'hdfs:/taotao/cloud/access/log/sources'
```

### 7.自动添加分区脚本

log_add_partition.sh

### 8.创建业务数据表
```
-- meta表
create external TABLE if not exists meta
(
    meta_type     int    comment    '元数据类型 0-行为数据日志 1-系统访问日志 2-订单操作日志
                                    3-支付操作日志 4-商品操作日志 5-用户操作日志
                                    6-订单表信息 7-商品表信息 8-用户表详细 9-支付表信息 ',
    field         string comment '字段名称',
    field_type    string comment '字段类型',
    field_desc    string comment '字段描述',
    app_version   string comment '版本号',
    status        int    comment '状态 0下线 1上线',
    create_time   string comment '创建时间',
)
partitioned by (logday string)
stored as parquet
location "/taotao/cloud/meta";

-- ad_info表
create external TABLE if not exists ad_info
(
    id              string  comment 'id',
    advertiser_id   string  comment '广告商id',
    advertiser_name string  comment '广告商名称',
    create_time     string  comment '创建时间',
    update_time     string  comment '修改时间',
)
partitioned by (logday string)
stored as parquet
location "/taotao/cloud/ad_info";
```

### 9.自动添加业务数据表分区脚本

log_add_partition.sh


**目的: 创建数据仓库 定时添加表分区 源日志表转换以parquet格式存储的表**
