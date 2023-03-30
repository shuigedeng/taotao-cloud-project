package com.taotao.cloud.order.biz.manager;

/**
 * Manager 层：通用业务处理层，它有如下特征：
 *
 * <p>对第三方平台封装的层，预处理返回结果及转化异常信息，适配上层接口； 对 Service 层通用能力的下沉，如缓存方案、中间件通用处理； 与 DAO 层交互，对多个 DAO
 * 的组合复用。（来源Ailibaba JAVA 开发手册）
 *
 * <p>1 .复杂业务，service提供数据给Manager层，负责业务编排，然后把事务下沉到Manager层，Manager层不允许相互调用，不会出现事务嵌套。
 * 2.专注于不带业务sql语言，也可以在manager层进行通用业务的dao层封装。 3.避免复杂的join查询，数据库压力比java大很多，所以要严格控制好sql，所以可以在manager层
 * 进行拆分，比如复杂查询。 4.简单的业务使用，可以不使用Manager层。（此段话借鉴一位前辈）
 */
