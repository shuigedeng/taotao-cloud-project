CREATE TABLE gen_datasource (
    id bigserial NOT NULL,
    db_type varchar(200),
    conn_name varchar(200),
    conn_url varchar(500),
    username varchar(200),
    password varchar(200),
    create_time timestamp,
    primary key (id)
);

COMMENT ON TABLE gen_datasource IS '数据源管理';
COMMENT ON COLUMN gen_datasource.id IS 'id';
COMMENT ON COLUMN gen_datasource.db_type IS '数据库类型';
COMMENT ON COLUMN gen_datasource.conn_name IS '连接名';
COMMENT ON COLUMN gen_datasource.conn_url IS 'URL';
COMMENT ON COLUMN gen_datasource.username IS '用户名';
COMMENT ON COLUMN gen_datasource.password IS '密码';
COMMENT ON COLUMN gen_datasource.create_time IS '创建时间';


CREATE TABLE gen_field_type
(
    id           bigserial NOT NULL,
    column_type  varchar(200),
    attr_type    varchar(200),
    package_name varchar(200),
    create_time  timestamp,
    primary key (id)
);

CREATE UNIQUE INDEX gen_column_type on gen_field_type(column_type);

COMMENT ON TABLE gen_field_type IS '字段类型管理';
COMMENT ON COLUMN gen_field_type.id IS 'id';
COMMENT ON COLUMN gen_field_type.column_type IS '字段类型';
COMMENT ON COLUMN gen_field_type.attr_type IS '属性类型';
COMMENT ON COLUMN gen_field_type.package_name IS '属性包名';
COMMENT ON COLUMN gen_field_type.create_time IS '创建时间';


CREATE TABLE gen_base_class
(
    id           bigserial NOT NULL,
    package_name varchar(200),
    code         varchar(200),
    fields       varchar(500),
    remark       varchar(200),
    create_time  timestamp,
    primary key (id)
);

COMMENT ON TABLE gen_base_class IS '基类管理';
COMMENT ON COLUMN gen_base_class.id IS 'id';
COMMENT ON COLUMN gen_base_class.package_name IS '基类包名';
COMMENT ON COLUMN gen_base_class.code IS '基类编码';
COMMENT ON COLUMN gen_base_class.fields IS '基类字段，多个用英文逗号分隔';
COMMENT ON COLUMN gen_base_class.remark IS '备注';
COMMENT ON COLUMN gen_base_class.create_time IS '创建时间';

CREATE TABLE gen_table
(
    id             bigserial NOT NULL,
    table_name     varchar(200),
    class_name     varchar(200),
    table_comment  varchar(200),
    author         varchar(200),
    email          varchar(200),
    package_name   varchar(200),
    version        varchar(200),
    generator_type int,
    backend_path   varchar(500),
    frontend_path  varchar(500),
    module_name    varchar(200),
    function_name  varchar(200),
    form_layout    int,
    datasource_id  int8,
    baseclass_id   int8,
    create_time    timestamp,
    primary key (id)
);
CREATE UNIQUE INDEX gen_table_name on gen_table(table_name);

COMMENT ON TABLE gen_table IS '代码生成表';
COMMENT ON COLUMN gen_table.id IS 'id';
COMMENT ON COLUMN gen_table.table_name IS '表名';
COMMENT ON COLUMN gen_table.class_name IS '类名';
COMMENT ON COLUMN gen_table.table_comment IS '说明';
COMMENT ON COLUMN gen_table.author IS '作者';
COMMENT ON COLUMN gen_table.email IS '邮箱';
COMMENT ON COLUMN gen_table.package_name IS '项目包名';
COMMENT ON COLUMN gen_table.version IS '项目版本号';
COMMENT ON COLUMN gen_table.generator_type IS '生成方式  0：zip压缩包   1：自定义目录';
COMMENT ON COLUMN gen_table.backend_path IS '后端生成路径';
COMMENT ON COLUMN gen_table.frontend_path IS '前端生成路径';
COMMENT ON COLUMN gen_table.module_name IS '模块名';
COMMENT ON COLUMN gen_table.function_name IS '功能名';
COMMENT ON COLUMN gen_table.form_layout IS '表单布局  1：一列   2：两列';
COMMENT ON COLUMN gen_table.datasource_id IS '数据源ID';
COMMENT ON COLUMN gen_table.baseclass_id IS '基类ID';
COMMENT ON COLUMN gen_table.create_time IS '创建时间';


CREATE TABLE gen_table_field
(
    id              bigserial NOT NULL,
    table_id        int8,
    field_name      varchar(200),
    field_type      varchar(200),
    field_comment   varchar(200),
    attr_name       varchar(200),
    attr_type       varchar(200),
    package_name    varchar(200),
    sort            int,
    auto_fill       varchar(20),
    primary_pk      boolean,
    base_field      boolean,
    form_item       boolean,
    form_required   boolean,
    form_type       varchar(200),
    form_dict       varchar(200),
    form_validator  varchar(200),
    grid_item       boolean,
    grid_sort       boolean,
    query_item      boolean,
    query_type      varchar(200),
    query_form_type varchar(200),
    query_dict      varchar(200),
    primary key (id)
);

COMMENT ON TABLE gen_table_field IS '代码生成表字段';
COMMENT ON COLUMN gen_table_field.id IS 'id';
COMMENT ON COLUMN gen_table_field.table_id IS '表ID';
COMMENT ON COLUMN gen_table_field.field_name IS '字段名称';
COMMENT ON COLUMN gen_table_field.field_type IS '字段类型';
COMMENT ON COLUMN gen_table_field.field_comment IS '字段说明';
COMMENT ON COLUMN gen_table_field.attr_name IS '属性名';
COMMENT ON COLUMN gen_table_field.attr_type IS '属性类型';
COMMENT ON COLUMN gen_table_field.package_name IS '属性包名';
COMMENT ON COLUMN gen_table_field.sort IS '排序';
COMMENT ON COLUMN gen_table_field.auto_fill IS '自动填充  DEFAULT、INSERT、UPDATE、INSERT_UPDATE';
COMMENT ON COLUMN gen_table_field.primary_pk IS '主键 0：否  1：是';
COMMENT ON COLUMN gen_table_field.base_field IS '基类字段 0：否  1：是';
COMMENT ON COLUMN gen_table_field.form_item IS '表单项 0：否  1：是';
COMMENT ON COLUMN gen_table_field.form_required IS '表单必填 0：否  1：是';
COMMENT ON COLUMN gen_table_field.form_type IS '表单类型';
COMMENT ON COLUMN gen_table_field.form_dict IS '表单字典类型';
COMMENT ON COLUMN gen_table_field.form_validator IS '表单效验';
COMMENT ON COLUMN gen_table_field.grid_item IS '列表项 0：否  1：是';
COMMENT ON COLUMN gen_table_field.grid_sort IS '列表排序 0：否  1：是';
COMMENT ON COLUMN gen_table_field.query_item IS '查询项 0：否  1：是';
COMMENT ON COLUMN gen_table_field.query_type IS '查询方式';
COMMENT ON COLUMN gen_table_field.query_form_type IS '查询表单类型';
COMMENT ON COLUMN gen_table_field.query_dict IS '查询字典类型';


CREATE TABLE gen_project_modify
(
    id                     bigserial NOT NULL,
    project_name           varchar(100),
    project_code           varchar(100),
    project_package        varchar(100),
    project_path           varchar(200),
    modify_project_name    varchar(100),
    modify_project_code    varchar(100),
    modify_project_package varchar(100),
    exclusions             varchar(200),
    modify_suffix          varchar(200),
    modify_tmp_path        varchar(100),
    create_time            timestamp,
    PRIMARY KEY (id)
);

COMMENT ON TABLE gen_project_modify IS '项目名变更';
COMMENT ON COLUMN gen_project_modify.id IS 'id';
COMMENT ON COLUMN gen_project_modify.project_name IS '项目名';
COMMENT ON COLUMN gen_project_modify.project_code IS '项目标识';
COMMENT ON COLUMN gen_project_modify.project_package IS '项目包名';
COMMENT ON COLUMN gen_project_modify.project_path IS '项目路径';
COMMENT ON COLUMN gen_project_modify.modify_project_name IS '变更项目名';
COMMENT ON COLUMN gen_project_modify.modify_project_code IS '变更标识';
COMMENT ON COLUMN gen_project_modify.modify_project_package IS '变更包名';
COMMENT ON COLUMN gen_project_modify.exclusions IS '排除文件';
COMMENT ON COLUMN gen_project_modify.modify_suffix IS '变更文件';
COMMENT ON COLUMN gen_project_modify.modify_tmp_path IS '变更临时路径';
COMMENT ON COLUMN gen_project_modify.create_time IS '创建时间';


-- 用于测试代码生成器的表结构 --
CREATE TABLE gen_test_student
(
    id          bigserial NOT NULL,
    name        varchar(50),
    gender      int,
    age         int,
    class_name  varchar(50),
    version     int,
    deleted     int,
    creator     int8,
    create_time timestamp,
    updater     int8,
    update_time timestamp,
    PRIMARY KEY (id)
);

COMMENT ON TABLE gen_test_student IS '测试2';
COMMENT ON COLUMN gen_test_student.id IS '学生ID';
COMMENT ON COLUMN gen_test_student.name IS '姓名';
COMMENT ON COLUMN gen_test_student.gender IS '性别';
COMMENT ON COLUMN gen_test_student.age IS '年龄';
COMMENT ON COLUMN gen_test_student.class_name IS '班级';
COMMENT ON COLUMN gen_test_student.version IS '版本号';
COMMENT ON COLUMN gen_test_student.deleted IS '删除标识';
COMMENT ON COLUMN gen_test_student.creator IS '创建者';
COMMENT ON COLUMN gen_test_student.create_time IS '创建时间';
COMMENT ON COLUMN gen_test_student.updater IS '更新者';
COMMENT ON COLUMN gen_test_student.update_time IS '更新时间';


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

commit;