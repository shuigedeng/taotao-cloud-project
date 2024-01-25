package com.taotao.cloud.ddd.interfaces;

/**
 * facade服务实现可以作为RPC提供服务，controller则作为本项目HTTP接口提供服务，供前端调用。
 * controller需要注意HTTP相关特性，敏感信息例如登陆用户ID不能依赖前端传递，
 * 登陆后前端会在请求头带一个登陆用户信息，服务端需要从请求头中获取并解析。
 */

//Interface Adapters层：这一层是一个适配层，
// 这一层中需要将出入参的DTO和业务层的VO/DO对象进行转换。
// 这一层不要包含任何的业务逻辑，只包含参数转换和业务无关的校验逻辑。
// 接口返回值缓存类的逻辑，可以放在这个模块中实现，因为这个动作不包含业务逻辑。

/**
 * |--- adapter                     -- 适配器层 应用与外部应用交互适配
 * |      |--- controller           -- 控制器层，API中的接口的实现
 * |      |       |--- assembler    -- 装配器，DTO和领域模型的转换
 * |      |       |--- impl         -- 协议层中接口的实现
 * |      |--- repository           -- 仓储层
 * |      |       |--- assembler    -- 装配器，PO和领域模型的转换
 * |      |       |--- impl         -- 领域层中仓储接口的实现
 * |      |--- rpc                  -- RPC层,Domain层中port中依赖的外部的接口实现，调用远程RPC接口
 * |      |--- task                 -- 任务，主要是调度任务的适配器
 */
