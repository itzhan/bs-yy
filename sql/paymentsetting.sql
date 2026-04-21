-- 支付设置表
CREATE TABLE IF NOT EXISTS `paymentsetting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `payname` varchar(100) DEFAULT NULL COMMENT '支付方式名称',
  `paycode` varchar(50) DEFAULT NULL COMMENT '支付方式编码',
  `payicon` varchar(255) DEFAULT NULL COMMENT '图标',
  `enabled` varchar(10) DEFAULT '是' COMMENT '是否启用',
  `sortorder` int(11) DEFAULT 0 COMMENT '排序',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付设置';

-- 默认支付方式
INSERT INTO `paymentsetting` (`payname`, `paycode`, `payicon`, `enabled`, `sortorder`) VALUES
  ('余额支付', 'balance', '', '是', 0),
  ('微信支付', 'weixin', '', '是', 1),
  ('支付宝支付', 'zhifubao', '', '是', 2),
  ('建设银行', 'jianshe', '', '是', 3),
  ('交通银行', 'jiaotong', '', '是', 4),
  ('农业银行', 'nongye', '', '是', 5),
  ('中国银行', 'zhongguo', '', '是', 6);

-- 订单表添加支付方式字段
ALTER TABLE `courseenrollment` ADD COLUMN `paymentmethod` varchar(50) DEFAULT NULL COMMENT '支付方式';
ALTER TABLE `cardapplication` ADD COLUMN `paymentmethod` varchar(50) DEFAULT NULL COMMENT '支付方式';
ALTER TABLE `cardrenewal` ADD COLUMN `paymentmethod` varchar(50) DEFAULT NULL COMMENT '支付方式';
ALTER TABLE `productorder` ADD COLUMN `paymentmethod` varchar(50) DEFAULT NULL COMMENT '支付方式';
