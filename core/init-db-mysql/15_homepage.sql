DROP TABLE IF EXISTS `homepage`;
CREATE TABLE `homepage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '广告标题',
  `subtitle` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '二级标题',
  `img` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图片',
  `url` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '广告地址',
  `des` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `rank` int(11) DEFAULT '0' COMMENT '排序，数值越大排名越靠前',
  `status` tinyint(3) DEFAULT NULL COMMENT '0禁用 1启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of homepage
-- ----------------------------
INSERT INTO `homepage` VALUES ('1', '厨师与羊', '北美最高点击的爆笑动漫', 'images/homepage01.jpg', 'http://v.youku.com/v_show/id_XODA5NDQ4MzA4.html', '厨师与羊：抓住他！别跟爷玩高科技', '1', '1');
INSERT INTO `homepage` VALUES ('2', '超神学院', '恶魔舰队席卷而来，蛮大哥无尽的怒火。', 'images/homepage02.jpg', 'http://v.youku.com/v_show/id_XODEwMjc0OTE2.html', '推荐16岁以上群众观看', '2', '1');
INSERT INTO `homepage` VALUES ('3', '敢达', '遇到了首都军的路线，发现首都军的战力意外...', 'images/homepage03.jpg', 'http://v.youku.com/v_show/id_XODEwMDY0NTM2.html', '', '3', '1');
INSERT INTO `homepage` VALUES ('4', '侠岚', '辗迟等人暂时击退汰、成功脱身。', 'images/homepage04.jpg', 'http://v.youku.com/v_show/id_XNzIyNDE1MDQ4.html', '', '4', '1');
INSERT INTO `homepage` VALUES ('5', '疯神榜', '美艳小绿茶，一秒就送达', 'images/homepage05.jpg', 'http://v.youku.com/v_show/id_XODA5OTAyMzgw.html', '', '5', '1');