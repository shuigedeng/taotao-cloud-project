package com.taotao.cloud.stock.biz.another.controller;

/**
 * facade服务实现可以作为RPC提供服务，controller则作为本项目HTTP接口提供服务，供前端调用。
 * controller需要注意HTTP相关特性，敏感信息例如登陆用户ID不能依赖前端传递，
 * 登陆后前端会在请求头带一个登陆用户信息，服务端需要从请求头中获取并解析。
 */
