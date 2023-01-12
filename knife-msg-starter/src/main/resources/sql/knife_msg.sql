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

 Date: 19/01/2022 13:37:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for msg_ali_account
-- ----------------------------
DROP TABLE IF EXISTS `msg_ali_account`;
CREATE TABLE `msg_ali_account`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `access_key_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'accessKeyId',
  `access_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'accessSecret',
  `weight` int(11) NOT NULL DEFAULT 0 COMMENT '权重',
  `create_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `create_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '版本',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '是否有效 0-未删除 1-已删除',
  `extra` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '额外信息',
  `tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表基础信息' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for msg_mail_account
-- ----------------------------
DROP TABLE IF EXISTS `msg_mail_account`;
CREATE TABLE `msg_mail_account`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'SMTP服务器地址',
  `port` int(255) NOT NULL COMMENT '端口',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登陆账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登陆密码（或授权码）',
  `mail_from` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮件发信人（即真实邮箱）',
  `encryption` int(255) NOT NULL COMMENT '加密方式 1-不加密 2-ssl 3-tls',
  `auth` int(255) NOT NULL COMMENT '是否认证 0-无需认证 1-需要认证',
  `weight` int(11) NOT NULL DEFAULT 0 COMMENT '权重',
  `create_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `create_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '版本',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '是否有效 0-未删除 1-已删除',
  `extra` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '额外信息',
  `tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件账户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for msg_wechat_mp_account
-- ----------------------------
DROP TABLE IF EXISTS `msg_wechat_mp_account`;
CREATE TABLE `msg_wechat_mp_account`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `appid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'appid',
  `secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'secret',
  `access_token` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'accessToken',
  `access_token_expires` bigint(255) NULL DEFAULT NULL COMMENT 'accessToken过期时间',
  `weight` int(11) NOT NULL DEFAULT 0 COMMENT '权重',
  `create_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `create_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '版本',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '是否有效 0-未删除 1-已删除',
  `extra` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '额外信息',
  `tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信公众号账户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for msg_dingtalk_account
-- ----------------------------
DROP TABLE IF EXISTS `msg_dingtalk_account`;
CREATE TABLE `msg_dingtalk_account`  (
                                         `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
                                         `app_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'appKey',
                                         `app_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'appSecret',
                                         `access_token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'accessToken',
                                         `access_token_expires` bigint NULL DEFAULT NULL COMMENT 'accessToken过期时间',
                                         `weight` int NULL DEFAULT 0 COMMENT '权重',
                                         `create_time` bigint NOT NULL DEFAULT 0 COMMENT '创建时间',
                                         `update_time` bigint NOT NULL DEFAULT 0 COMMENT '更新时间',
                                         `create_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
                                         `update_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
                                         `version` bigint NOT NULL DEFAULT 0 COMMENT '版本',
                                         `deleted` int NOT NULL DEFAULT 0 COMMENT '是否有效 0-未删除 1-已删除',
                                         `extra` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '额外信息',
                                         `tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钉钉账户信息' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
