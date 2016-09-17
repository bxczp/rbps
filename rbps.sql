/*
Navicat MySQL Data Transfer

Source Server         : root-ASUS
Source Server Version : 50624
Source Host           : 127.0.0.1:3306
Source Database       : rbps

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-09-17 16:21:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_auth`;
CREATE TABLE `t_auth` (
  `authId` int(11) NOT NULL AUTO_INCREMENT,
  `authName` varchar(20) DEFAULT NULL,
  `authPath` varchar(100) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  `authDescription` varchar(200) DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  `iconCls` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`authId`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_auth
-- ----------------------------
INSERT INTO `t_auth` VALUES ('1', '某系统', '', '-1', null, 'closed', 'icon-home');
INSERT INTO `t_auth` VALUES ('2', '权限管理', '', '1', null, 'closed', 'icon-permission');
INSERT INTO `t_auth` VALUES ('3', '学生管理', '', '1', null, 'closed', 'icon-student');
INSERT INTO `t_auth` VALUES ('4', '课程管理', '', '1', null, 'closed', 'icon-course');
INSERT INTO `t_auth` VALUES ('5', '住宿管理', 'zsgl.html', '3', null, 'open', 'icon-item');
INSERT INTO `t_auth` VALUES ('6', '学生信息管理', 'xsxxgl.html', '3', null, 'open', 'icon-item');
INSERT INTO `t_auth` VALUES ('7', '学籍管理', 'xjgl.html', '3', null, 'open', 'icon-item');
INSERT INTO `t_auth` VALUES ('8', '奖惩管理', 'jcgl.html', '3', null, 'open', 'icon-item');
INSERT INTO `t_auth` VALUES ('9', '课程设置', 'kcsz.html', '4', null, 'open', 'icon-item');
INSERT INTO `t_auth` VALUES ('10', '选课情况', 'xkqk.html', '4', null, 'open', 'icon-item');
INSERT INTO `t_auth` VALUES ('11', '成绩录入', 'cjlr.html', '4', null, 'open', 'icon-item');
INSERT INTO `t_auth` VALUES ('12', '用户管理', 'userManage.html', '2', null, 'open', 'icon-userManage');
INSERT INTO `t_auth` VALUES ('13', '角色管理', 'roleManage.html', '2', null, 'open', 'icon-roleManage');
INSERT INTO `t_auth` VALUES ('14', '菜单管理', 'menuManage.html', '2', null, 'open', 'icon-menuManage');
INSERT INTO `t_auth` VALUES ('15', '修改密码', '', '1', null, 'open', 'icon-modifyPassword');
INSERT INTO `t_auth` VALUES ('16', '安全退出', '', '1', null, 'open', 'icon-exit');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `roleId` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(20) DEFAULT NULL,
  `authIds` varchar(50) DEFAULT NULL,
  `roleDescription` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '超级管理员', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16', '具有最高权限');
INSERT INTO `t_role` VALUES ('2', '宿舍管理员', '1,3,5,6,7,8,15,16', '管理学生宿舍信息');
INSERT INTO `t_role` VALUES ('3', '辅导员', '', '负责学生的生活，学习及心里问题');
INSERT INTO `t_role` VALUES ('8', '教师', '', '讲课的');
INSERT INTO `t_role` VALUES ('9', '学生', '', '你懂的');
INSERT INTO `t_role` VALUES ('18', '辅导员', '', '略');
INSERT INTO `t_role` VALUES ('19', 'a', null, null);
INSERT INTO `t_role` VALUES ('20', 'aaa', null, null);
INSERT INTO `t_role` VALUES ('21', 'b', null, null);
INSERT INTO `t_role` VALUES ('22', 'bbbbb', null, null);
INSERT INTO `t_role` VALUES ('23', 'c', null, null);
INSERT INTO `t_role` VALUES ('24', 'ccccccc', null, null);
INSERT INTO `t_role` VALUES ('25', 'd', null, null);
INSERT INTO `t_role` VALUES ('26', 'ddddddd', null, null);
INSERT INTO `t_role` VALUES ('27', 'e', null, null);
INSERT INTO `t_role` VALUES ('28', 'eeeeee', null, null);
INSERT INTO `t_role` VALUES ('29', 'f', null, null);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `userType` tinyint(4) DEFAULT NULL,
  `roleId` int(11) DEFAULT NULL,
  `userDescription` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`userId`),
  KEY `FK_t_user` (`roleId`),
  CONSTRAINT `FK_t_user` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '11', '123456', '1', '1', '用户备注待添加');
INSERT INTO `t_user` VALUES ('2', 'm', '123456', '2', '2', '用户备注待添加');
INSERT INTO `t_user` VALUES ('3', 'java', '123456', '2', '3', '用户备注待添加');
INSERT INTO `t_user` VALUES ('15', 'javacc', '123456', '2', '18', 'wewewewewewewewewwee');
INSERT INTO `t_user` VALUES ('28', 'java', '123456', '2', '2', '用户备注待添加');
INSERT INTO `t_user` VALUES ('29', 'java', '123456', '2', '2', '用户备注待添加');
INSERT INTO `t_user` VALUES ('30', null, '123456', '2', '2', '');
INSERT INTO `t_user` VALUES ('31', 'xiaoli', '123456', '2', '3', '用户备注待添加');
INSERT INTO `t_user` VALUES ('32', 'ddd', '123456', '2', '18', '用户备注待添加');
INSERT INTO `t_user` VALUES ('33', '111', '123456', '2', '2', '用户备注待添加');
INSERT INTO `t_user` VALUES ('34', 'lisi', '123456', '2', '2', '用户备注待添加');
INSERT INTO `t_user` VALUES ('35', '21', '123456', '2', '2', '用户备注待添加');
INSERT INTO `t_user` VALUES ('41', null, '123456', '2', '3', '');
INSERT INTO `t_user` VALUES ('42', '33333333333333', '123456', '2', '3', '用户备注待添加');
INSERT INTO `t_user` VALUES ('43', 'qqqqqqq', '123456', '2', '3', '');
INSERT INTO `t_user` VALUES ('44', '吾问无为谓呜呜呜', '123456', '2', '3', 'wwwwwwwwwww');
INSERT INTO `t_user` VALUES ('77', 'wwwwwwwwwwwwwwwwwwww', '123456', '2', '3', 'wwwwwwwwwwwwwwwwwwwwwwwww');
INSERT INTO `t_user` VALUES ('78', 'qqqqqqqaa', '123456', '2', '2', 'aaaaaaaaaaaaaaaaaaaaaaaaaa');
INSERT INTO `t_user` VALUES ('80', 'nnnnn', '123456', '2', '3', 'iiiiiiiiiiiiii');
