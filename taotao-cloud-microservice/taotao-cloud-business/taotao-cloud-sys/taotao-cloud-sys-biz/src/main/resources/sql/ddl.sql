-- auto Generated on 2022-05-03
-- DROP TABLE IF EXISTS short_link_d_o;
USE short_link;
CREATE TABLE short_link
(
    id          BIGINT(20)                         NOT NULL AUTO_INCREMENT COMMENT 'ID',
    group_id    BIGINT(20)                         NOT NULL COMMENT '分组ID',
    title       VARCHAR(124)                       NOT NULL COMMENT '短链标题',
    origin_url  VARCHAR(255)                       NOT NULL COMMENT '原始URL',
    domain      VARCHAR(124)                       NOT NULL COMMENT '短链域名',
    code        VARCHAR(64)                        NOT NULL COMMENT '短链码',
    sign        VARCHAR(124)                       NOT NULL COMMENT 'TODO 长链的md5码，方便查找',
    account_no  BIGINT(20)                         NOT NULL COMMENT '账户编码',
    state       INT(1)                             NOT NULL COMMENT '状态：0=无锁、1=锁定',
    expired     DATETIME                           NOT NULL COMMENT '过期时间',
    create_time DATETIME default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time DATETIME default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  INT(11)                            NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=否、1=是',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '短链表';

CREATE TABLE domain
(
    id          BIGINT(20)                           NOT NULL AUTO_INCREMENT COMMENT 'id',
    account_no  BIGINT(20)                           NOT NULL COMMENT '用户自己绑定的域名',
    domain_type TINYINT(1)                           NOT NULL COMMENT '域名类型，0=系统自带, 1=用户自建',
    value       VARCHAR(124)                         NOT NULL COMMENT 'value',
    create_time datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  INTEGER(1) default 0                 not null comment '逻辑删除：0=否、1=是',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '短链域名表';

-- auto Generated on 2022-05-03
-- DROP TABLE IF EXISTS link_group;
CREATE TABLE link_group
(
    id          BIGINT(15)                           NOT NULL AUTO_INCREMENT COMMENT 'id',
    title       VARCHAR(50)                          NOT NULL COMMENT '分组名',
    account_no  BIGINT(15)                           NOT NULL COMMENT '账号唯一编号',
    create_time datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  INTEGER(1) default 0                 not null comment '逻辑删除：0=否、1=是',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT 'link_group';
