SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP SCHEMA IF EXISTS mall;
CREATE SCHEMA mall;
USE mall;

-- ----------------------------
-- Table structure for chain
-- ----------------------------
DROP TABLE IF EXISTS `chain`;
CREATE TABLE `chain`
(
    `id`                bigint(100)                                               NOT NULL COMMENT '主键',
    `project_id`        bigint(100)                                               NULL DEFAULT NULL COMMENT '项目id',
    `chain_method_list` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评估链方法集合',
    `create_time`       timestamp(0)                                              NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`       timestamp(0)                                              NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;
