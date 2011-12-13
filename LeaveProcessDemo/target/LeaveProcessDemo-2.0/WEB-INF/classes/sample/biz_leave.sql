/*
MySQL Data Transfer
Source Host: localhost
Source Database: dolphin
Target Host: localhost
Target Database: dolphin
Date: 2011/7/22 18:15:49
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for biz_leave
-- ----------------------------
DROP TABLE IF EXISTS `biz_leave`;
CREATE TABLE `biz_leave` (
  `id` int(2) NOT NULL auto_increment,
  `applier` int(20) default NULL,
  `applyDate` datetime default NULL,
  `leaveDay` int(2) default NULL,
  `reason` text,
  `approver` int(20) default NULL,
  `approveFlag` varchar(10) default NULL,
  `process_ins_id` int(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
