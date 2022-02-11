/*
 Navicat Premium Data Transfer

 Source Server         : 8.129.25.245
 Source Server Type    : MariaDB
 Source Server Version : 100412
 Source Host           : 8.129.25.245:3308
 Source Schema         : cloud_app_demo

 Target Server Type    : MariaDB
 Target Server Version : 100412
 File Encoding         : 65001

 Date: 11/02/2022 11:49:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for qrtz_task
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_task`;
CREATE TABLE `qrtz_task`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '自增主键',
  `task_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名',
  `task_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务组',
  `task_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行类',
  `note` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务说明',
  `cron` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '定时规则',
  `exec_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行参数',
  `exec_date` bigint(20) NULL DEFAULT NULL COMMENT '执行时间',
  `exec_result` int(2) NULL DEFAULT NULL COMMENT '执行结果（成功:1、失败:0、正在执行：-1)',
  `concurrent` int(2) NULL DEFAULT 0 COMMENT '是否允许并发，0(false)：不允许 1（true）：允许',
  `weight` int(11) NULL DEFAULT 0 COMMENT '权重',
  `create_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `create_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `version` bigint(20) NOT NULL DEFAULT 0 COMMENT '版本',
  `extra` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '额外信息',
  `tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_task_class`(`task_class`) USING BTREE,
  UNIQUE INDEX `uk_task_name_task_group`(`task_name`, `task_group`) USING BTREE COMMENT '任务名任务组复合唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_task_log
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_task_log`;
CREATE TABLE `qrtz_task_log`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '自增主键',
  `task_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名',
  `exec_date` bigint(20) NULL DEFAULT NULL COMMENT '执行时间',
  `exec_result` int(2) NULL DEFAULT NULL COMMENT '执行结果（成功:1、失败:0、正在执行：-1)',
  `exec_result_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '成功信息或抛出的异常信息',
  `task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务id',
  `weight` int(11) NULL DEFAULT 0 COMMENT '权重',
  `create_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `create_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `version` bigint(20) NOT NULL DEFAULT 0 COMMENT '版本',
  `extra` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '额外信息',
  `tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
