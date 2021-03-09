/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : openapi-admin

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 09/04/2020 00:30:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PASSWORD` varchar(256) NOT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `REAL_NAME` varchar(20) NOT NULL,
  `STATUS` smallint(6) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_user
-- ----------------------------
BEGIN;
INSERT INTO `admin_user` VALUES (1, '111111', 'admin', '超级管理员', 1);
INSERT INTO `admin_user` VALUES (2, '666666', 'menglili@shiro.com', '3333', 1);
INSERT INTO `admin_user` VALUES (3, '111111', 'test@shiro.com', 'æµ‹è¯•', 1);
INSERT INTO `admin_user` VALUES (4, 'A0F73C801EC01FC62D5BD046BCB775AA', 'aa@shiro.com', '13717967', 0);
INSERT INTO `admin_user` VALUES (5, 'A0F73C801EC01FC62D5BD046BCB775AA', 'bb@shiro.com', 'æµ‹è¯•', 0);
INSERT INTO `admin_user` VALUES (6, 'A8BDD2E90BBAE6B555C56E6F30B9E3D4', 'cc@shiro.com', 'æµ‹è¯•', 0);
INSERT INTO `admin_user` VALUES (7, '111111', 'dasdasd', 'sad', 1);
INSERT INTO `admin_user` VALUES (8, 'dasdasdsa', 'dsadsadsa', 'adasdsad', 1);
INSERT INTO `admin_user` VALUES (9, 'dasdsadsa', 'asddasdsa', 'ddasdsa', 0);
INSERT INTO `admin_user` VALUES (10, 'sadasdsa', 'asdsad', 'asdsadsa', 0);
INSERT INTO `admin_user` VALUES (11, 'dsadsad', 'dasdas', 'dasdsa', 1);
INSERT INTO `admin_user` VALUES (12, '000000', '000', '000', 1);
INSERT INTO `admin_user` VALUES (13, 'safsaf', 'sadsa', 'saffsaf', 1);
INSERT INTO `admin_user` VALUES (14, '111111', '79879', '35325', 1);
INSERT INTO `admin_user` VALUES (15, 'asfasfs', 'czxczx', 'ccasa', 1);
INSERT INTO `admin_user` VALUES (16, '5555555', 'dasda', '5555', 1);
INSERT INTO `admin_user` VALUES (17, 'adasd', 'asdasda', 'asdas', 1);
INSERT INTO `admin_user` VALUES (19, 'dsadad', 'dasdasda', 'ffsdfs', 1);
COMMIT;

-- ----------------------------
-- Table structure for api_mapping
-- ----------------------------
DROP TABLE IF EXISTS `api_mapping`;
CREATE TABLE `api_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gatewayApiName` varchar(100) NOT NULL COMMENT '对外接口名称',
  `insideApiUrl` varchar(100) NOT NULL COMMENT '对内的接口URL',
  `state` tinyint(4) NOT NULL COMMENT 'api状态 1 可用 0 不可用 ',
  `description` varchar(500) DEFAULT NULL COMMENT '接口描述',
  `serviceId` varchar(50) NOT NULL,
  `idempotents` char(1) NOT NULL DEFAULT '0',
  `needfee` char(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_openapiname` (`gatewayApiName`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of api_mapping
-- ----------------------------
BEGIN;
INSERT INTO `api_mapping` VALUES (1, 'taobao.pop.order.get', '/orderget', 0, '获取订单信息接口', 'openapi-order', '0', '1');
INSERT INTO `api_mapping` VALUES (2, 'taobao.order.order.update', 'xxxx', 0, '修改订单状态接口', 'daseqweq', '0', '1');
INSERT INTO `api_mapping` VALUES (6, 'testservice01_02', '/testservice01/test02', 0, '测试服务 01 的 02 方法', 'testservice01', '0', '1');
INSERT INTO `api_mapping` VALUES (7, 'testservice02_02', '/testservice02//test02', 0, '测试服务 2 的 02 方法', 'testservice02', '0', '1');
INSERT INTO `api_mapping` VALUES (8, 'ceshi3_2', '/ceshi3/ceshi2', 0, '测试服务 3 普通方法', 'testservice03', '0', '1');
INSERT INTO `api_mapping` VALUES (9, 'testservice01_01', '/testservice01/test01/{name}', 0, '测试服务 1 的测试 1 方法', 'testservice01', '0', '1');
INSERT INTO `api_mapping` VALUES (10, 'testservice01_03', '/testservice01/test03/{name}/{age}/{pwd}', 0, '测试多路径参数', 'testservice01', '0', '1');
INSERT INTO `api_mapping` VALUES (11, 'orderget', '/orderget', 0, '查询订单', 'openapi-order', '0', '1');
INSERT INTO `api_mapping` VALUES (12, 'ordercancel', '/ordercancel', 0, '退货', 'openapi-order', '0', '1');
INSERT INTO `api_mapping` VALUES (13, 'ffs', 'fsdf', 0, 'fsf', 'fsdfs', '0', '1');
INSERT INTO `api_mapping` VALUES (14, 'fsdf', 'fsdf', 0, 'fsdfs', 'fsdfs', '0', '1');
COMMIT;

-- ----------------------------
-- Table structure for api_systemparam
-- ----------------------------
DROP TABLE IF EXISTS `api_systemparam`;
CREATE TABLE `api_systemparam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '参数名',
  `description` varchar(255) DEFAULT NULL COMMENT '介绍',
  `state` smallint(1) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nameuni` (`name`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of api_systemparam
-- ----------------------------
BEGIN;
INSERT INTO `api_systemparam` VALUES (1, 'aaa', 'aaa', 1);
INSERT INTO `api_systemparam` VALUES (3, 'aaaa', 'asdasda', 1);
COMMIT;

-- ----------------------------
-- Table structure for app_info
-- ----------------------------
DROP TABLE IF EXISTS `app_info`;
CREATE TABLE `app_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `corpName` varchar(50) NOT NULL COMMENT '公司名称',
  `appName` varchar(50) NOT NULL COMMENT 'app名称',
  `appKey` varchar(50) NOT NULL COMMENT 'APPKEY',
  `appSecret` varchar(50) NOT NULL COMMENT 'APP密码',
  `redirectUrl` varchar(100) NOT NULL COMMENT '回调地址',
  `limit` int(11) NOT NULL COMMENT 'APP每天限流量',
  `description` varchar(500) DEFAULT NULL COMMENT 'APP描述',
  `cusId` int(11) DEFAULT NULL COMMENT '所属客户 id',
  `state` smallint(1) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`),
  KEY `idx_corpname` (`appKey`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_info
-- ----------------------------
BEGIN;
INSERT INTO `app_info` VALUES (1, '海尔集团', '海尔电器ERP', '561AC1A8676CFCB0CC61B041AE42ABB8', 'ff2ff43192a84bb49988720c307182c8', 'https://www.haier.com/cn/', 300000, 'desc', 1, 1);
INSERT INTO `app_info` VALUES (2, '联想', '联想', 'dasdasd', 'adsdasdsa', 'dasdasd', 231312, 'asdasda', 2, 1);
COMMIT;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL COMMENT '公司名',
  `password` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `money` bigint(255) DEFAULT '0' COMMENT '金钱',
  `address` longtext COMMENT '地址',
  `state` smallint(1) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer
-- ----------------------------
BEGIN;
INSERT INTO `customer` VALUES (1, 'haier', '111111', '海尔集团', 2000000, ' 青岛', 1);
INSERT INTO `customer` VALUES (2, 'lianxiang', '111111', '联想', 1000000, ' 联想', 1);
INSERT INTO `customer` VALUES (3, 'google', '111111', '谷歌', 1092, '没过', 1);
COMMIT;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `PARENT_ID` int(11) DEFAULT NULL,
  `URL` varchar(500) DEFAULT NULL,
  `ICON` varchar(30) DEFAULT NULL,
  `PERMS` varchar(100) DEFAULT NULL,
  `TYPE` smallint(6) DEFAULT NULL,
  `SORT` int(11) DEFAULT '1000',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
BEGIN;
INSERT INTO `menu` VALUES (2, '权限管理', NULL, NULL, 'fa fa-bug', NULL, 0, 1);
INSERT INTO `menu` VALUES (3, '菜单管理', 2, '/sys/menu.html', 'fa fa-th-list', 'sys:menu:list', 1, 1000);
INSERT INTO `menu` VALUES (4, '角色管理', 2, '/sys/role.html', 'fa fa-key', 'sys:user:list', 1, 1000);
INSERT INTO `menu` VALUES (5, '用户管理', 2, '/sys/user.html', 'fa fa-user', 'sys:user:list', 1, 1000);
INSERT INTO `menu` VALUES (6, '添加', 5, '/sys/user/add', NULL, 'sys:user:add', 2, 1000);
INSERT INTO `menu` VALUES (7, '修改', 5, '/sys/user/update', NULL, 'sys:user:update', 2, 1000);
INSERT INTO `menu` VALUES (8, '删除', 5, '/sys/user/del', NULL, 'sys:user:delete', 2, 1000);
INSERT INTO `menu` VALUES (9, '授权', 5, '/sys/user/assign_role', NULL, 'sys:user:assign', 2, 1000);
INSERT INTO `menu` VALUES (10, '添加', 3, '/sys/menu/add', NULL, 'sys:menu:add', 2, 1000);
INSERT INTO `menu` VALUES (11, '修改', 3, '/sys/menu/update', NULL, 'sys:menu:update', 2, 1000);
INSERT INTO `menu` VALUES (12, '删除', 3, '/sys/menu/del', NULL, 'sys:menu:delete', 2, 1000);
INSERT INTO `menu` VALUES (13, '列表', 3, '/sys/menu/list', NULL, 'sys:menu:list', 2, 1000);
INSERT INTO `menu` VALUES (14, '菜单树', 3, '/sys/menu/tree', NULL, 'sys:menu:tree', 2, 1000);
INSERT INTO `menu` VALUES (15, '详情', 3, '/sys/menu/info', NULL, 'sys:menu:info', 2, 1000);
INSERT INTO `menu` VALUES (16, '添加', 4, '/sys/role/add', NULL, 'sys:role:add', 2, 1000);
INSERT INTO `menu` VALUES (17, '修改', 4, '/sys/role/update', NULL, 'sys:role:update', 2, 1000);
INSERT INTO `menu` VALUES (18, '删除', 4, '/sys/role/delete', NULL, 'sys:role:delete', 2, 1000);
INSERT INTO `menu` VALUES (19, '授权', 4, '/sys/role/assign_menu', NULL, 'sys:role:assign', 2, 1000);
INSERT INTO `menu` VALUES (20, '路由管理', NULL, '/sys/api_mapping.html', 'fa fa-cogs', 'sys:api:list', 1, 4);
INSERT INTO `menu` VALUES (21, '添加', 20, NULL, NULL, 'sys:api:add', 2, 1000);
INSERT INTO `menu` VALUES (22, '修改', 20, NULL, NULL, 'sys:api:update', 2, 1000);
INSERT INTO `menu` VALUES (23, '删除', 20, NULL, NULL, 'sys:api:delete', 2, 1000);
INSERT INTO `menu` VALUES (24, '日志搜索', NULL, '/sys/search.html', 'fa fa-search', 'sys:search:list', 1, 8);
INSERT INTO `menu` VALUES (25, 'token管理', NULL, '/sys/token.html', 'fa fa-cubes', 'sys:token:list', 1, 6);
INSERT INTO `menu` VALUES (26, '修改', 25, NULL, NULL, 'sys:token:update', 2, 1000);
INSERT INTO `menu` VALUES (27, '应用管理', NULL, '/sys/app_info.html', 'fa fa-diamond', 'sys:app:list', 1, 3);
INSERT INTO `menu` VALUES (28, '修改', 27, NULL, NULL, 'sys:app:update', 2, 1000);
INSERT INTO `menu` VALUES (29, '添加', 25, NULL, NULL, 'sys:token:add', 2, 1);
INSERT INTO `menu` VALUES (30, '列表', 2, '/sys/user/table', NULL, 'sys:user:table', 2, 1000);
INSERT INTO `menu` VALUES (31, '用户角色树', 2, '/sys/user/role_tree', NULL, 'sys:user:role_tree', 2, 1000);
INSERT INTO `menu` VALUES (32, '用户角色', 2, '/sys/user/user_role', NULL, 'sys:user:user_role', 2, 1000);
INSERT INTO `menu` VALUES (33, '分配角色', 2, '/sys/user/assign_role', NULL, 'sys:user:ssign_role', 2, 1000);
INSERT INTO `menu` VALUES (34, '详情', 2, '/sys/user/info', NULL, 'sys:user:info', 2, 1000);
INSERT INTO `menu` VALUES (35, '系统参数管理', NULL, '/sys/api_systemparamter.html', 'fa fa-diamond', 'sys:systemparamter:list', 1, 5);
INSERT INTO `menu` VALUES (36, '客户管理', NULL, '/sys/customer.html', 'fa fa-diamond', 'sys:customer:list', 1, 2);
INSERT INTO `menu` VALUES (37, '充值管理', NULL, '/sys/recharge.html', 'fa fa-diamond', 'sys:recharge:list', 1, 7);
INSERT INTO `menu` VALUES (38, '菜单树', 4, '/sys/role/menu_tree', NULL, NULL, 2, 1000);
INSERT INTO `menu` VALUES (39, '获取角色菜单树', 4, '/sys/role/role_menu', NULL, NULL, 2, 1000);
INSERT INTO `menu` VALUES (40, '角色信息', 4, '/sys/role/info', NULL, NULL, 2, 1000);
COMMIT;

-- ----------------------------
-- Table structure for recharge
-- ----------------------------
DROP TABLE IF EXISTS `recharge`;
CREATE TABLE `recharge` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cusId` int(11) NOT NULL COMMENT '客户 id',
  `orderId` bigint(255) DEFAULT NULL COMMENT '订单号',
  `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `money` int(11) DEFAULT NULL COMMENT '充值金额(分)',
  `state` smallint(1) NOT NULL DEFAULT '0' COMMENT '状态,0创建,1 支付,2 取消',
  `paymenttype` smallint(3) DEFAULT NULL COMMENT '支付方式 支付宝 微信等',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of recharge
-- ----------------------------
BEGIN;
INSERT INTO `recharge` VALUES (1, 1, 412323143241324, '2020-04-06 03:49:34', '2020-04-06 04:05:50', 1000000, 1, 1);
INSERT INTO `recharge` VALUES (2, 1, 412352523125124, '2020-04-06 04:13:58', '2020-04-06 04:17:30', 2000000, 1, 1);
INSERT INTO `recharge` VALUES (3, 1, 363253454352349, '2020-04-06 04:14:58', '2020-04-06 04:17:33', 3000000, 2, 2);
INSERT INTO `recharge` VALUES (4, 2, 623623623423623, '2020-04-06 04:17:24', '2020-04-06 04:17:24', 1330000, 1, 1);
INSERT INTO `recharge` VALUES (5, 1, 326236234624362, '2020-04-06 04:24:28', '2020-04-06 04:24:28', 1000000, 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `REMARK` varchar(50) NOT NULL,
  `STATUS` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES (1, 'admin', '超级管理员', 1);
INSERT INTO `role` VALUES (2, 'test', 'remark', 1);
INSERT INTO `role` VALUES (3, 'aa', '333', 0);
INSERT INTO `role` VALUES (4, 'asd', 'sadsa', 0);
COMMIT;

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `ROLE_ID` int(11) NOT NULL,
  `MENU_ID` int(11) NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
BEGIN;
INSERT INTO `role_menu` VALUES (1, 2);
INSERT INTO `role_menu` VALUES (1, 3);
INSERT INTO `role_menu` VALUES (1, 4);
INSERT INTO `role_menu` VALUES (1, 5);
INSERT INTO `role_menu` VALUES (1, 6);
INSERT INTO `role_menu` VALUES (1, 7);
INSERT INTO `role_menu` VALUES (1, 8);
INSERT INTO `role_menu` VALUES (1, 9);
INSERT INTO `role_menu` VALUES (1, 10);
INSERT INTO `role_menu` VALUES (1, 11);
INSERT INTO `role_menu` VALUES (1, 12);
INSERT INTO `role_menu` VALUES (1, 13);
INSERT INTO `role_menu` VALUES (1, 14);
INSERT INTO `role_menu` VALUES (1, 15);
INSERT INTO `role_menu` VALUES (1, 16);
INSERT INTO `role_menu` VALUES (1, 17);
INSERT INTO `role_menu` VALUES (1, 18);
INSERT INTO `role_menu` VALUES (1, 19);
INSERT INTO `role_menu` VALUES (1, 20);
INSERT INTO `role_menu` VALUES (1, 21);
INSERT INTO `role_menu` VALUES (1, 22);
INSERT INTO `role_menu` VALUES (1, 23);
INSERT INTO `role_menu` VALUES (1, 24);
INSERT INTO `role_menu` VALUES (1, 25);
INSERT INTO `role_menu` VALUES (1, 26);
INSERT INTO `role_menu` VALUES (1, 27);
INSERT INTO `role_menu` VALUES (1, 28);
INSERT INTO `role_menu` VALUES (1, 29);
INSERT INTO `role_menu` VALUES (1, 30);
INSERT INTO `role_menu` VALUES (1, 31);
INSERT INTO `role_menu` VALUES (1, 32);
INSERT INTO `role_menu` VALUES (1, 33);
INSERT INTO `role_menu` VALUES (1, 34);
INSERT INTO `role_menu` VALUES (1, 35);
INSERT INTO `role_menu` VALUES (1, 36);
INSERT INTO `role_menu` VALUES (1, 37);
INSERT INTO `role_menu` VALUES (2, 2);
INSERT INTO `role_menu` VALUES (2, 4);
INSERT INTO `role_menu` VALUES (2, 5);
INSERT INTO `role_menu` VALUES (2, 6);
INSERT INTO `role_menu` VALUES (2, 7);
INSERT INTO `role_menu` VALUES (2, 8);
INSERT INTO `role_menu` VALUES (2, 16);
INSERT INTO `role_menu` VALUES (2, 17);
INSERT INTO `role_menu` VALUES (2, 18);
COMMIT;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `USER_ID` int(11) NOT NULL,
  `ROLE_ID` int(11) NOT NULL,
  PRIMARY KEY (`USER_ID`,`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
INSERT INTO `user_role` VALUES (1, 1);
INSERT INTO `user_role` VALUES (2, 2);
INSERT INTO `user_role` VALUES (2, 3);
INSERT INTO `user_role` VALUES (3, 1);
COMMIT;

-- ----------------------------
-- Table structure for user_token
-- ----------------------------
DROP TABLE IF EXISTS `user_token`;
CREATE TABLE `user_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appId` int(11) NOT NULL,
  `cusId` int(11) NOT NULL,
  `access_token` varchar(50) NOT NULL,
  `expireTime` datetime NOT NULL,
  `startTime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_token
-- ----------------------------
BEGIN;
INSERT INTO `user_token` VALUES (1, 1, 1, 'eb6aa496-4918-4099-9082-19582e8438e1\n', '2020-04-06 05:06:11', '2020-04-30 05:06:16');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
