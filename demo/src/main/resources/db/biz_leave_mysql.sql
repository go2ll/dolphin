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
-- Table structure for DOLPHIN_EXAMPLE_LEAVE
-- ----------------------------
DROP TABLE IF EXISTS `DOLPHIN_EXAMPLE_LEAVE`;
CREATE TABLE `DOLPHIN_EXAMPLE_LEAVE` (
  `ID` int(2) NOT NULL auto_increment,
  `APPLIER` int(20) default NULL,
  `APPLY_DATE` datetime default NULL,
  `LEAVE_DAY` int(2) default NULL,
  `REASON` text,
  `APPROVER` int(20) default NULL,
  `APPROVE_FLAG` varchar(10) default NULL,
  `PROCESS_INS_ID` int(20) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
