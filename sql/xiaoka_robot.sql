/*
 Navicat Premium Data Transfer

 Source Server         : rm-uf6e4xr978w748b9w7o.mysql.rds.aliyuncs.com
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : rm-uf6e4xr978w748b9w7o.mysql.rds.aliyuncs.com:3306
 Source Schema         : xiaoka_robot

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 09/12/2022 23:41:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_identity
-- ----------------------------
DROP TABLE IF EXISTS `user_identity`;
CREATE TABLE `user_identity` (
  `id` varchar(20) NOT NULL COMMENT '学号',
  `password` varchar(20) NOT NULL COMMENT '密码',
  `phone_number` varchar(20) NOT NULL COMMENT '手机号',
  `mailbox` varchar(20) NOT NULL COMMENT '邮箱',
  `school` varchar(50) NOT NULL COMMENT '学校',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生身份信息表';

SET FOREIGN_KEY_CHECKS = 1;
