DROP TABLE IF EXISTS `mall_ad`;
CREATE TABLE `mall_ad`  (
                            `id` bigint(13) NOT NULL AUTO_INCREMENT,
                            `name` varchar(66) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_ad
-- ----------------------------
INSERT INTO `mall_ad` VALUES (1, '太平洋');
INSERT INTO `mall_ad` VALUES (2, '大西洋');
