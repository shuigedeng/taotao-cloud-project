-- 元数据表
CREATE TABLE meta
(
    id           bigint(20)   NOT NULL AUTO_INCREMENT,
    meta_type    int          not null default 0 comment '元数据类型-0-行为数据日志 1-系统访问日志 2-订单操作日志
   3-支付操作日志 4-商品操作日志 5-用户操作日志
   6-订单表信息 7-商品表信息 8-用户表详细 9-支付表信息 ',
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

-- 新闻表
CREATE TABLE ad_info
(
    id              bigint(20)  NOT NULL AUTO_INCREMENT,
    advertiser_id   bigint(20)  null     default null comment '广告商id',
    advertiser_name varchar(50) null     default null comment '广告商名称',
    create_time     timestamp   not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time     timestamp   null comment '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
