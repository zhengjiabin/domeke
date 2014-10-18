
DROP TABLE IF EXISTS `order_detail`;

CREATE TABLE `order_detail` (
  `orderdetailid` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderid` bigint(20) NOT NULL COMMENT '订单主键',
  `ordernum` varchar(32) NOT NULL COMMENT '订单编号',
  `goodsid` bigint(20) DEFAULT NULL,
  `goodsname` varchar(255) DEFAULT NULL,
  `goodsprice` decimal(13,2) DEFAULT NULL,
  `goodsnum` decimal(7,2) DEFAULT NULL,
  `paytype` varchar(4) DEFAULT NULL COMMENT '支付类型：金钱，豆豆，优惠券',
  PRIMARY KEY (`orderdetailid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `orderid` bigint(20) NOT NULL AUTO_INCREMENT,
  `ordernum` varchar(32) NOT NULL COMMENT '订单编号',
  `order` bigint(20) DEFAULT NULL COMMENT '订单人编号',
  `orderPay` varchar(4) DEFAULT NULL COMMENT '支付方式',
  `isPay` varchar(4) DEFAULT NULL COMMENT '是否支付',
  `isDelivery` varchar(4) DEFAULT NULL COMMENT '是否发货',
  `orderAddr` varchar(256) DEFAULT NULL COMMENT '收货地址',
  `orderPhone` varchar(32) DEFAULT NULL COMMENT '收货人电话',
  `postage` float(7,2) DEFAULT NULL COMMENT '邮费',
  `creater` bigint(20) NOT NULL,
  `creattime` timestamp NULL DEFAULT NULL,
  `updater` bigint(20) DEFAULT NULL,
  `updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


