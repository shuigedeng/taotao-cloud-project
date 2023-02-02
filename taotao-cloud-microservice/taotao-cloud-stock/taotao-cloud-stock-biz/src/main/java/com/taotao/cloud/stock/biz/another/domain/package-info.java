package com.taotao.cloud.stock.biz.another.domain;

/**
 * 4.领域对象 VS 数据对象  1.数据对象使用基本类型保持纯净：PlayerEntity  2.领域对象需要体现业务含义：PlayerQueryResultDomain
 * <p>
 * 领域对象 VS 业务对象 1.数据对象使用基本类型保持纯净：PlayerEntity 2.业务对象同样会体现业务 最大不同是领域对象采用充血模型聚合业务: PlayerCreateBO
 * <p>
 * 领域层 VS 应用层 第一个区别：领域层关注纵向，应用层关注横向。领域层纵向做隔离，本领域业务行为要在本领域内处理完。应用层横向做编排，聚合和编排领域服务。
 * 第二个区别：应用层可以更加灵活组合不同领域业务，并且可以增加流控、监控、日志、权限，分布式锁，相较于领域层功能更为丰富。
 */
