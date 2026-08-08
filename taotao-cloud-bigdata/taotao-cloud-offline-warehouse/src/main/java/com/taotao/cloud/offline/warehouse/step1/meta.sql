-- 元数据信息表
CREATE TABLE meta_info
(
    id           bigint(20)   NOT NULL AUTO_INCREMENT,
    meta_type    int          not null default 0 comment '元数据类型-0-行为数据日志 1-系统访问日志 2-订单操作日志
   3-支付操作日志 4-商品操作日志 5-用户操作日志 6-订单表信息 7-商品表信息 8-用户表详细 9-支付表信息 ',
    field        varchar(128) not null comment '字段',
    field_type   varchar(128) not null comment '字段类型-string|int|bigint|double|array|datetime',
    field_desc   varchar(256) not null comment '字段描述',
    meta_version varchar(128) not null default '1.0' comment '元数据版本',
    status       int          not null default 0 comment '0-可用 1-不可用',
    create_time  timestamp    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time  timestamp    null comment '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- 日志来源表
CREATE TABLE meta_log_source
(
    id           bigint(20)   NOT NULL AUTO_INCREMENT,
    meta_type    int          not null default 0 comment '',
    field        varchar(128) not null comment '字段',
    field_type   varchar(128) not null comment '',
    field_desc   varchar(256) not null comment '字段描述',
    meta_version varchar(128) not null default '1.0' comment '元数据版本',
    status       int          not null default 0 comment '0-可用 1-不可用',
    create_time  timestamp    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time  timestamp    null comment '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- 事件表

-- 类型表

-- 请求类型表

-- 时间维度表
CREATE TABLE meta_dim_date
(
    id                bigint(20)   NOT NULL AUTO_INCREMENT,
    date_day          varchar(30)  not null comment '2020-09-15',
    date_day_desc     varchar(30)  not null comment '日期格式 2020-09-15',
    date_day_month    varchar(30)  not null comment '本月第几天 20',
    date_day_year     varchar(30)  not null comment '本年第几天 120',
    date_day_en       varchar(30)  not null comment '当前时间是星期几 monday',
    date_week         varchar(30)  not null comment '本月第几周 4',
    date_week_desc    varchar(30)  not null comment '本月第几周',
    date_month        varchar(30)  not null comment '第几月 5',
    date_month_en     varchar(30)  not null comment '英文第几月 may',
    date_month_desc   varchar(30)  not null comment '第几月 5',
    date_quarter      varchar(30)  not null comment '第几季度 3',
    date_quarter_en   varchar(30)  not null comment '英文第几季度 Q2',
    date_quarter_desc varchar(30)  not null comment '第几季度 Q2',
    date_year         varchar(30)  not null comment '哪一年 2020',
    meta_version      varchar(128) not null default '1.0' comment '元数据版本',
    status            int          not null default 0 comment '0-可用 1-不可用',
    create_time       timestamp    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time       timestamp    null comment '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- 地区维度表
CREATE TABLE meta_dim_area
(
    id                   bigint(20)   NOT NULL AUTO_INCREMENT,
    region_code          varchar(30)  not null comment '地区编码 20320|23783',
    region_code_desc     varchar(30)  not null comment '地区编码描述 如渝北区 江北区',
    region_city          varchar(30)  not null comment '城市编码 20320|23783',
    region_city_desc     varchar(30)  not null comment '城市编码描述 如重庆市',
    region_province      varchar(30)  not null comment '省编码 20320|23783',
    region_province_desc varchar(30)  not null comment '省编码描述 如四川省',
    meta_version         varchar(128) not null default '1.0' comment '元数据版本',
    status               int          not null default 0 comment '0-可用 1-不可用',
    create_time          timestamp    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time          timestamp    null comment '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- 通用字典维度表
CREATE TABLE meta_dim_dict
(
    id           bigint(20)   NOT NULL AUTO_INCREMENT,
    dict_type    varchar(30)  not null comment '字典类型',
    dict_code    varchar(30)  not null comment '字典编码',
    dict_remark  varchar(30)  null comment '字典描述',
    dict_ext1    varchar(30)  null comment '字典扩展1',
    dict_ext2    varchar(30)  null comment '字典扩展2',
    dict_ext3    varchar(30)  null comment '字典扩展3',
    dict_ext4    varchar(30)  null comment '字典扩展4',
    meta_version varchar(128) not null default '1.0' comment '元数据版本',
    status       int          not null default 0 comment '0-可用 1-不可用',
    create_time  timestamp    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time  timestamp    null comment '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
