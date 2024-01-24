package com.taotao.cloud.stock.biz.another.facade;

/**
 * 1.facade + client 设计模式中有一种Facade模式，称为门面模式或者外观模式。
 * 这种模式提供一个简洁对外语义，屏蔽内部系统复杂性。
 * client承载数据对外传输对象DTO，facade承载对外服务，
 * 必须满足最小知识原则，无关信息不必对外透出。这样做有两个优点：
 *
 * <p>简洁性：对外服务语义明确简洁 安全性：敏感字段不能对外透出
 *
 * <p>client端：相当于feign接口 facade服务实现可以作为RPC提供服务
 */
