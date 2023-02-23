CREATE TABLE gen_datasource
(
    id          bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    db_type     varchar(200) COMMENT '数据库类型',
    conn_name   varchar(200) NOT NULL COMMENT '连接名',
    conn_url    varchar(500) COMMENT 'URL',
    username    varchar(200) COMMENT '用户名',
    password    varchar(200) COMMENT '密码',
    create_time datetime COMMENT '创建时间',
    primary key (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='数据源管理';

CREATE TABLE gen_field_type
(
    id           bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    column_type  varchar(200) COMMENT '字段类型',
    attr_type    varchar(200) COMMENT '属性类型',
    package_name varchar(200) COMMENT '属性包名',
    create_time  datetime COMMENT '创建时间',
    primary key (id),
    unique key (column_type)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='字段类型管理';

CREATE TABLE gen_base_class
(
    id           bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    package_name varchar(200) COMMENT '基类包名',
    code         varchar(200) COMMENT '基类编码',
    fields       varchar(500) COMMENT '基类字段，多个用英文逗号分隔',
    remark       varchar(200) COMMENT '备注',
    create_time  datetime COMMENT '创建时间',
    primary key (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='基类管理';

CREATE TABLE gen_table
(
    id             bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    table_name     varchar(200) COMMENT '表名',
    class_name     varchar(200) COMMENT '类名',
    table_comment  varchar(200) COMMENT '说明',
    author         varchar(200) COMMENT '作者',
    email          varchar(200) COMMENT '邮箱',
    package_name   varchar(200) COMMENT '项目包名',
    version        varchar(200) COMMENT '项目版本号',
    generator_type tinyint COMMENT '生成方式  0：zip压缩包   1：自定义目录',
    backend_path   varchar(500) COMMENT '后端生成路径',
    frontend_path  varchar(500) COMMENT '前端生成路径',
    module_name    varchar(200) COMMENT '模块名',
    function_name  varchar(200) COMMENT '功能名',
    form_layout    tinyint COMMENT '表单布局  1：一列   2：两列',
    datasource_id  bigint COMMENT '数据源ID',
    baseclass_id   bigint COMMENT '基类ID',
    create_time    datetime COMMENT '创建时间',
    primary key (id),
    unique key (table_name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='代码生成表';

CREATE TABLE gen_table_field
(
    id              bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    table_id        bigint COMMENT '表ID',
    field_name      varchar(200) COMMENT '字段名称',
    field_type      varchar(200) COMMENT '字段类型',
    field_comment   varchar(200) COMMENT '字段说明',
    attr_name       varchar(200) COMMENT '属性名',
    attr_type       varchar(200) COMMENT '属性类型',
    package_name    varchar(200) COMMENT '属性包名',
    sort            int COMMENT '排序',
    auto_fill       varchar(20) COMMENT '自动填充  DEFAULT、INSERT、UPDATE、INSERT_UPDATE',
    primary_pk      tinyint COMMENT '主键 0：否  1：是',
    base_field      tinyint COMMENT '基类字段 0：否  1：是',
    form_item       tinyint COMMENT '表单项 0：否  1：是',
    form_required   tinyint COMMENT '表单必填 0：否  1：是',
    form_type       varchar(200) COMMENT '表单类型',
    form_dict       varchar(200) COMMENT '表单字典类型',
    form_validator  varchar(200) COMMENT '表单效验',
    grid_item       tinyint COMMENT '列表项 0：否  1：是',
    grid_sort       tinyint COMMENT '列表排序 0：否  1：是',
    query_item      tinyint COMMENT '查询项 0：否  1：是',
    query_type      varchar(200) COMMENT '查询方式',
    query_form_type varchar(200) COMMENT '查询表单类型',
    query_dict      varchar(200) COMMENT '查询字典类型',
    primary key (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='代码生成表字段';

CREATE TABLE gen_project_modify
(
    id                     bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    project_name           varchar(100) COMMENT '项目名',
    project_code           varchar(100) COMMENT '项目标识',
    project_package        varchar(100) COMMENT '项目包名',
    project_path           varchar(200) COMMENT '项目路径',
    modify_project_name    varchar(100) COMMENT '变更项目名',
    modify_project_code    varchar(100) COMMENT '变更标识',
    modify_project_package varchar(100) COMMENT '变更包名',
    exclusions             varchar(200) COMMENT '排除文件',
    modify_suffix          varchar(200) COMMENT '变更文件',
    modify_tmp_path        varchar(100) COMMENT '变更临时路径',
    create_time            datetime COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='项目名变更';


-- 用于测试代码生成器的表结构 --
CREATE TABLE gen_test_student
(
    id          bigint NOT NULL AUTO_INCREMENT COMMENT '学生ID',
    name        varchar(50) COMMENT '姓名',
    gender      tinyint COMMENT '性别',
    age         int COMMENT '年龄',
    class_name  varchar(50) COMMENT '班级',
    version     int COMMENT '版本号',
    deleted     tinyint COMMENT '删除标识',
    creator     bigint COMMENT '创建者',
    create_time datetime COMMENT '创建时间',
    updater     bigint COMMENT '更新者',
    update_time datetime COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='测试2';


INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('datetime', 'Date', 'java.util.Date', now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('date', 'Date', 'java.util.Date', now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('tinyint', 'Integer', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('smallint', 'Integer', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('mediumint', 'Integer', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('int', 'Integer', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('integer', 'Integer', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('bigint', 'Long', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('float', 'Float', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('double', 'Double', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('decimal', 'BigDecimal', 'java.math.BigDecimal', now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('bit', 'Boolean', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('char', 'String', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('varchar', 'String', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('tinytext', 'String', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('text', 'String', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('mediumtext', 'String', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('longtext', 'String', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('timestamp', 'Date', 'java.util.Date', now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('NUMBER', 'Integer', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('BINARY_INTEGER', 'Integer', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('BINARY_FLOAT', 'Float', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('BINARY_DOUBLE', 'Double', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('VARCHAR2', 'String', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('NVARCHAR', 'String', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('NVARCHAR2', 'String', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('CLOB', 'String', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('int8', 'Long', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('int4', 'Integer', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('int2', 'Integer', NULL, now());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time) VALUES ('numeric', 'BigDecimal', 'java.math.BigDecimal', now());

INSERT INTO gen_base_class (package_name, code, fields, remark, create_time) VALUES ('net.maku.framework.mybatis.entity', 'BaseEntity', 'id,creator,create_time,updater,update_time,version,deleted', '使用该基类，则需要表里有这些字段', now());

INSERT INTO gen_project_modify (project_name, project_code, project_package, project_path, modify_project_name, modify_project_code, modify_project_package, exclusions, modify_suffix, create_time) VALUES ('maku-boot', 'maku', 'net.maku', 'D:/makunet/maku-boot', 'baba-boot', 'baba', 'com.baba', '.git,.idea,target,logs', 'java,xml,yml,txt', now());
INSERT INTO gen_project_modify (project_name, project_code, project_package, project_path, modify_project_name, modify_project_code, modify_project_package, exclusions, modify_suffix, create_time) VALUES ('maku-cloud', 'maku', 'net.maku', 'D:/makunet/maku-cloud', 'baba-cloud', 'baba', 'com.baba', '.git,.idea,target,logs', 'java,xml,yml,txt', now());
