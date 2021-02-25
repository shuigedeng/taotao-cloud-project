package com.taotao.cloud.product.biz;// 银行卡表
//
//         CREATE TABLE `member_card` (
//         `id` int(10)unsigned NOT NULL AUTO_INCREMENT,
//         `member_id` int(11)NOT NULL COMMENT'用户编码',
//         `card_name` varchar(25)COLLATE utf8mb4_unicode_ci NOT NULL COMMENT'持卡人姓名',
//         `card_number` varchar(25)COLLATE utf8mb4_unicode_ci NOT NULL COMMENT'银行卡号',
//         `created_at` timestamp NULL DEFAULT NULL,
//         `updated_at` timestamp NULL DEFAULT NULL,
//         PRIMARY KEY(`id`),
//         UNIQUE KEY `member_card_card_number_unique` (`card_number`)
//         )ENGINE=InnoDB AUTO_INCREMENT=11DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
//
//         购物车表
//         CREATE TABLE `member_cart` (
//         `id` int(10)unsigned NOT NULL AUTO_INCREMENT,
//         `member_id` int(11)NOT NULL COMMENT'用户编码',
//         `created_at` timestamp NULL DEFAULT NULL,
//         `updated_at` timestamp NULL DEFAULT NULL,
//         PRIMARY KEY(`id`),
//         UNIQUE KEY `member_cart_member_id_unique` (`member_id`),
//         KEY `member_cart_member_id_index` (`member_id`)
//         )ENGINE=InnoDB AUTO_INCREMENT=28DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
//
//         购物车商品表
//         CREATE TABLE `member_cart_item` (
//         `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
//         `cart_id` int(11) NOT NULL COMMENT '购物车编码',
//         `product_desc` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品sku信息',
//         `product_img` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品快照',
//         `product_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
//         `price` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
//         `product_id` int(11) NOT NULL COMMENT '商品编码',
//         `supplier_id` int(11) NOT NULL COMMENT '店铺编码',
//         `sku_id` int(11) NOT NULL COMMENT '商品sku编码',
//         `number` int(11) NOT NULL DEFAULT '1' COMMENT '商品数量',
//         `created_at` timestamp NULL DEFAULT NULL,
//         `updated_at` timestamp NULL DEFAULT NULL,
//         PRIMARY KEY (`id`),
//         KEY `member_cart_item_cart_id_product_id_supplier_id_index` (`cart_id`,`product_id`,`supplier_id`)
//         ) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
//
//         用户搜索历史表
//         CREATE TABLE `member_query_history` (
//         `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
//         `member_id` int(11) NOT NULL COMMENT '用户编码',
//         `keyword` varchar(125) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '关键字',
//         `created_at` timestamp NULL DEFAULT NULL,
//         `updated_at` timestamp NULL DEFAULT NULL,
//         PRIMARY KEY (`id`)
//         ) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
