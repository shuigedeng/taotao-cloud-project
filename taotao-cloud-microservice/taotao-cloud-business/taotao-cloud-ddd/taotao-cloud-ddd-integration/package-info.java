package com.taotao.cloud.stock.biz.another.infrastructure;

/**
 * 这一层是一个适配层，主要负责外部系统和内部业务系统的适配，这一层的主要作用就是外部系统和内部系统的适配和协议转换。
 */
/**
 * Repository实现类中需要将接口入参中的DO对象转换为PO对象后再调用数据库存储。
 * Repository和聚合的关系是一对一的关系。一个Repository有唯一的对应的聚合。
 * 如果Repository中需要开始事务可以在Repository实现类中开启事务。
 * Rpc层最好是对外部接口的出参和入参定义一个防腐层对象，命名统一以DTO结尾。
 *
 * 作者：京东云开发者
 * 链接：https://juejin.cn/post/7270479996271165479
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
//本层调用外部服务，转换外部DTO成为本项目可以理解对象。
