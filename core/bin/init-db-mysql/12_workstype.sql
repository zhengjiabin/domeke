DROP TABLE IF EXISTS `works_type`;
CREATE TABLE `works_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` tinyint(3) DEFAULT '0' COMMENT '0为视频，1为漫画',
  `indextop` int(11) DEFAULT '0' COMMENT '大于0则在首页显示 值越高排名越靠前',
  `cartoontop` int(11) DEFAULT NULL,
  `des` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '简介',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `works_type` VALUES ('10', '原创动漫', '1', '99', '95', '原创动漫');
INSERT INTO `works_type` VALUES ('20', '亲子动漫', '1', '98', '96', '亲子动漫');
INSERT INTO `works_type` VALUES ('30', 'HIGH动漫', '1', '97', '97', 'HIGH动漫');
INSERT INTO `works_type` VALUES ('40', 'HI豆推荐', '1', '96', '98', 'HI豆推荐');
INSERT INTO `works_type` VALUES ('50', '原创精选', '1', '95', '94', '原创精选');
INSERT INTO `works_type` VALUES ('60', '人气作品', '1', '94', '99', '人气作品');
INSERT INTO `works_type` VALUES ('61', '新作预告', '1', '93', '93', '新作预告');