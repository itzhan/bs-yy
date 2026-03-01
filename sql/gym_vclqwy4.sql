SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- gym_vclqwy4 数据库脚本

CREATE DATABASE IF NOT EXISTS `gym_vclqwy4`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `gym_vclqwy4`;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `useraccount` varchar(500) COMMENT '账号',
  `userpassword` varchar(500) COMMENT '密码',
  `name` varchar(500) COMMENT '姓名',
  `sex` varchar(500) COMMENT '性别',
  `age` int(11) DEFAULT 0 COMMENT '年龄',
  `phone` varchar(500) COMMENT '手机号码',
  `image` varchar(500) COMMENT '头像',
  `money` double DEFAULT 0 COMMENT '余额',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `cardno` varchar(500) COMMENT '会员卡号',
  `memberlevel` varchar(500) COMMENT '会员等级',
  `expiretime` timestamp NULL DEFAULT NULL COMMENT '会员到期时间',
  `remainingcourses` int(11) DEFAULT 0 COMMENT '剩余课时',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';

-- 教练表
DROP TABLE IF EXISTS `coach`;
CREATE TABLE `coach` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '教练编号',
  `coachaccount` varchar(500) COMMENT '账号',
  `coachpassword` varchar(500) COMMENT '密码',
  `coachname` varchar(500) COMMENT '教练姓名',
  `coachimage` varchar(500) COMMENT '教练头像',
  `sex` varchar(500) COMMENT '性别',
  `phone` varchar(500) COMMENT '手机号码',
  `jobno` varchar(500) COMMENT '工号',
  `specialty` varchar(500) COMMENT '擅长领域',
  `coachlevel` varchar(500) COMMENT '教练等级',
  `workyears` int(11) DEFAULT 0 COMMENT '从业年限',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教练';

-- 课程分类表
DROP TABLE IF EXISTS `coursetype`;
CREATE TABLE `coursetype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类编号',
  `coursetypename` varchar(500) COMMENT '分类名称',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程分类';

-- 健身课程表
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '课程编号',
  `coursename` varchar(500) COMMENT '课程名称',
  `courseimage` varchar(500) COMMENT '课程封面',
  `coursetype` varchar(500) COMMENT '课程分类',
  `classtime` timestamp NULL DEFAULT NULL COMMENT '上课时间',
  `duration` int(11) DEFAULT 0 COMMENT '课程时长',
  `coachname` varchar(500) COMMENT '教练',
  `coachaccount` varchar(500) COMMENT '教练账号',
  `courseprice` double DEFAULT 0 COMMENT '课程单价',
  `quota` int(11) DEFAULT 0 COMMENT '剩余名额',
  `coursedesc` varchar(500) COMMENT '课程简介',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `favoritenum` int(11) DEFAULT 0 COMMENT '收藏数',
  `likenum` int(11) DEFAULT 0 COMMENT '点赞数',
  `stepnum` int(11) DEFAULT 0 COMMENT '踩数',
  `clicknum` int(11) DEFAULT 0 COMMENT '点击数',
  `clicktime` timestamp NULL DEFAULT NULL COMMENT '最后点击时间',
  `auditstatus` varchar(500) COMMENT '审核状态',
  `auditreply` varchar(500) COMMENT '审核回复',
  `discussnum` int(11) DEFAULT 0 COMMENT '评论数',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健身课程';

-- 健身课程评论表
DROP TABLE IF EXISTS `discusscourse`;
CREATE TABLE `discusscourse` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `avatarurl` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `nickname` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `likenum` int(11) DEFAULT '0' COMMENT '点赞数',
  `stepnum` int(11) DEFAULT '0' COMMENT '踩数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健身课程评论表';

-- 课程报名记录表
DROP TABLE IF EXISTS `courseenrollment`;
CREATE TABLE `courseenrollment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '报名编号',
  `coursename` varchar(500) COMMENT '课程名称',
  `courseimage` varchar(500) COMMENT '课程封面',
  `coursetype` varchar(500) COMMENT '课程分类',
  `classtime` timestamp NULL DEFAULT NULL COMMENT '上课时间',
  `duration` int(11) DEFAULT 0 COMMENT '课程时长',
  `coachname` varchar(500) COMMENT '教练',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `useraccount` varchar(500) COMMENT '用户账号',
  `coachaccount` varchar(500) COMMENT '教练账号',
  `courseprice` double DEFAULT 0 COMMENT '课程单价',
  `quantity` int(11) DEFAULT 0 COMMENT '数量',
  `totalprice` double DEFAULT 0 COMMENT '总价',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ispay` varchar(500) COMMENT '是否支付',
  `orderstatus` varchar(500) COMMENT '状态',
  `logistics` varchar(500) COMMENT '物流信息',
  `crossuserid` bigint(20) DEFAULT 0 COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT 0 COMMENT '跨表来源id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程报名记录';

-- 商品分类表
DROP TABLE IF EXISTS `producttype`;
CREATE TABLE `producttype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类编号',
  `producttypename` varchar(500) COMMENT '分类名称',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类';

-- 健身商品表
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品编号',
  `productname` varchar(500) COMMENT '商品名称',
  `productimage` varchar(500) COMMENT '商品图片',
  `producttype` varchar(500) COMMENT '商品分类',
  `productprice` double DEFAULT 0 COMMENT '价格',
  `stock` int(11) DEFAULT 0 COMMENT '库存',
  `productdesc` varchar(500) COMMENT '商品描述',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `favoritenum` int(11) DEFAULT 0 COMMENT '收藏数',
  `likenum` int(11) DEFAULT 0 COMMENT '点赞数',
  `stepnum` int(11) DEFAULT 0 COMMENT '踩数',
  `clicknum` int(11) DEFAULT 0 COMMENT '点击数',
  `clicktime` timestamp NULL DEFAULT NULL COMMENT '最后点击时间',
  `auditstatus` varchar(500) COMMENT '审核状态',
  `auditreply` varchar(500) COMMENT '审核回复',
  `discussnum` int(11) DEFAULT 0 COMMENT '评论数',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健身商品';

-- 健身商品评论表
DROP TABLE IF EXISTS `discussproduct`;
CREATE TABLE `discussproduct` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `avatarurl` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `nickname` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `likenum` int(11) DEFAULT '0' COMMENT '点赞数',
  `stepnum` int(11) DEFAULT '0' COMMENT '踩数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健身商品评论表';

-- 商品订单表
DROP TABLE IF EXISTS `productorder`;
CREATE TABLE `productorder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单编号',
  `productname` varchar(500) COMMENT '商品名称',
  `productimage` varchar(500) COMMENT '商品图片',
  `producttype` varchar(500) COMMENT '商品分类',
  `productprice` double DEFAULT 0 COMMENT '商品单价',
  `quantity` int(11) DEFAULT 0 COMMENT '购买数量',
  `totalprice` double DEFAULT 0 COMMENT '订单总价',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ispay` varchar(500) COMMENT '是否支付',
  `orderstatus` varchar(500) COMMENT '状态',
  `logistics` varchar(500) COMMENT '物流信息',
  `discussnum` int(11) DEFAULT 0 COMMENT '评论数',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `crossuserid` bigint(20) DEFAULT 0 COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT 0 COMMENT '跨表来源id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品订单';

-- 商品订单评论表
DROP TABLE IF EXISTS `discussproductorder`;
CREATE TABLE `discussproductorder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `avatarurl` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `nickname` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `likenum` int(11) DEFAULT '0' COMMENT '点赞数',
  `stepnum` int(11) DEFAULT '0' COMMENT '踩数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品订单评论表';

-- 会员卡套餐表
DROP TABLE IF EXISTS `membershippackage`;
CREATE TABLE `membershippackage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '套餐编号',
  `packagename` varchar(500) COMMENT '套餐名称',
  `packageimage` varchar(500) COMMENT '套餐图片',
  `packagetype` varchar(500) COMMENT '套餐类型',
  `packageprice` double DEFAULT 0 COMMENT '价格',
  `validdays` int(11) DEFAULT 0 COMMENT '有效期',
  `includedcourses` int(11) DEFAULT 0 COMMENT '包含课时',
  `packagedesc` varchar(500) COMMENT '套餐说明',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员卡套餐';

-- 办卡记录表
DROP TABLE IF EXISTS `cardapplication`;
CREATE TABLE `cardapplication` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '办卡编号',
  `packagename` varchar(500) COMMENT '套餐名称',
  `packageimage` varchar(500) COMMENT '套餐图片',
  `packagetype` varchar(500) COMMENT '套餐类型',
  `packageprice` double DEFAULT 0 COMMENT '价格',
  `validdays` int(11) DEFAULT 0 COMMENT '有效期',
  `includedcourses` int(11) DEFAULT 0 COMMENT '包含课时',
  `quantity` int(11) DEFAULT 0 COMMENT '数量',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ispay` varchar(500) COMMENT '是否支付',
  `orderstatus` varchar(500) COMMENT '状态',
  `logistics` varchar(500) COMMENT '物流信息',
  `crossuserid` bigint(20) DEFAULT 0 COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT 0 COMMENT '跨表来源id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='办卡记录';

-- 续卡记录表
DROP TABLE IF EXISTS `cardrenewal`;
CREATE TABLE `cardrenewal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '续卡编号',
  `packagename` varchar(500) COMMENT '套餐名称',
  `packagetype` varchar(500) COMMENT '套餐类型',
  `packageprice` double DEFAULT 0 COMMENT '价格',
  `renewaldays` int(11) DEFAULT 0 COMMENT '续费时长',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ispay` varchar(500) COMMENT '是否支付',
  `orderstatus` varchar(500) COMMENT '状态',
  `logistics` varchar(500) COMMENT '物流信息',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `crossuserid` bigint(20) DEFAULT 0 COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT 0 COMMENT '跨表来源id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='续卡记录';

-- 交流分类表
DROP TABLE IF EXISTS `sharetype`;
CREATE TABLE `sharetype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类编号',
  `sharetypename` varchar(500) COMMENT '分类名称',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交流分类';

-- 会员交流表
DROP TABLE IF EXISTS `share`;
CREATE TABLE `share` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '交流编号',
  `sharetitle` varchar(500) COMMENT '标题',
  `sharecontent` varchar(500) COMMENT '内容',
  `shareimage` varchar(500) COMMENT '配图',
  `sharetype` varchar(500) COMMENT '分类',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `favoritenum` int(11) DEFAULT 0 COMMENT '收藏数',
  `likenum` int(11) DEFAULT 0 COMMENT '点赞数',
  `stepnum` int(11) DEFAULT 0 COMMENT '踩数',
  `clicknum` int(11) DEFAULT 0 COMMENT '点击数',
  `clicktime` timestamp NULL DEFAULT NULL COMMENT '最后点击时间',
  `auditstatus` varchar(500) COMMENT '审核状态',
  `auditreply` varchar(500) COMMENT '审核回复',
  `discussnum` int(11) DEFAULT 0 COMMENT '评论数',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员交流';

-- 会员交流评论表
DROP TABLE IF EXISTS `discussshare`;
CREATE TABLE `discussshare` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `avatarurl` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `nickname` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `likenum` int(11) DEFAULT '0' COMMENT '点赞数',
  `stepnum` int(11) DEFAULT '0' COMMENT '踩数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员交流评论表';

-- 意见反馈表
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '反馈编号',
  `feedbacktitle` varchar(500) COMMENT '标题',
  `feedbackcontent` varchar(500) COMMENT '内容',
  `feedbacktype` varchar(500) COMMENT '反馈类型',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `auditstatus` varchar(500) COMMENT '审核状态',
  `auditreply` varchar(500) COMMENT '审核回复',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='意见反馈';

-- 健身课程评论表表
DROP TABLE IF EXISTS `discusscourse`;
CREATE TABLE `discusscourse` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `refid` bigint(20) DEFAULT 0 COMMENT '关联表id',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户id',
  `nickname` varchar(500) COMMENT '用户名',
  `avatarurl` varchar(500) COMMENT '头像',
  `content` varchar(500) COMMENT '评论内容',
  `reply` varchar(500) COMMENT '回复内容',
  `likenum` int(11) DEFAULT 0 COMMENT '点赞数',
  `stepnum` int(11) DEFAULT 0 COMMENT '踩数',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健身课程评论表';

-- 取消课程报名记录表
DROP TABLE IF EXISTS `cancelcourseenrollment`;
CREATE TABLE `cancelcourseenrollment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '报名编号',
  `coursename` varchar(500) COMMENT '课程名称',
  `courseimage` varchar(500) COMMENT '课程封面',
  `coursetype` varchar(500) COMMENT '课程分类',
  `classtime` timestamp NULL DEFAULT NULL COMMENT '上课时间',
  `duration` int(11) DEFAULT 0 COMMENT '课程时长',
  `coachname` varchar(500) COMMENT '教练',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `cancelreason` varchar(500) COMMENT '取消原因',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `crossuserid` bigint(20) DEFAULT 0 COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT 0 COMMENT '跨表来源id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='取消课程报名记录';

-- 课程报名记录退款表
DROP TABLE IF EXISTS `refundcourseenrollment`;
CREATE TABLE `refundcourseenrollment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '报名编号',
  `coursename` varchar(500) COMMENT '课程名称',
  `courseimage` varchar(500) COMMENT '课程封面',
  `coursetype` varchar(500) COMMENT '课程分类',
  `classtime` timestamp NULL DEFAULT NULL COMMENT '上课时间',
  `duration` int(11) DEFAULT 0 COMMENT '课程时长',
  `coachname` varchar(500) COMMENT '教练',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `refundreason` varchar(500) COMMENT '退款原因',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `crossuserid` bigint(20) DEFAULT 0 COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT 0 COMMENT '跨表来源id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程报名记录退款';

-- 健身商品评论表表
DROP TABLE IF EXISTS `discussproduct`;
CREATE TABLE `discussproduct` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `refid` bigint(20) DEFAULT 0 COMMENT '关联表id',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户id',
  `nickname` varchar(500) COMMENT '用户名',
  `avatarurl` varchar(500) COMMENT '头像',
  `content` varchar(500) COMMENT '评论内容',
  `reply` varchar(500) COMMENT '回复内容',
  `likenum` int(11) DEFAULT 0 COMMENT '点赞数',
  `stepnum` int(11) DEFAULT 0 COMMENT '踩数',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健身商品评论表';

-- 取消办卡记录表
DROP TABLE IF EXISTS `cancelcardapplication`;
CREATE TABLE `cancelcardapplication` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '办卡编号',
  `packagename` varchar(500) COMMENT '套餐名称',
  `packageimage` varchar(500) COMMENT '套餐图片',
  `packagetype` varchar(500) COMMENT '套餐类型',
  `packageprice` double DEFAULT 0 COMMENT '价格',
  `validdays` int(11) DEFAULT 0 COMMENT '有效期',
  `includedcourses` int(11) DEFAULT 0 COMMENT '包含课时',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `cancelreason` varchar(500) COMMENT '取消原因',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `crossuserid` bigint(20) DEFAULT 0 COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT 0 COMMENT '跨表来源id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='取消办卡记录';

-- 办卡记录退款表
DROP TABLE IF EXISTS `refundcardapplication`;
CREATE TABLE `refundcardapplication` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '办卡编号',
  `packagename` varchar(500) COMMENT '套餐名称',
  `packageimage` varchar(500) COMMENT '套餐图片',
  `packagetype` varchar(500) COMMENT '套餐类型',
  `packageprice` double DEFAULT 0 COMMENT '价格',
  `validdays` int(11) DEFAULT 0 COMMENT '有效期',
  `includedcourses` int(11) DEFAULT 0 COMMENT '包含课时',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `refundreason` varchar(500) COMMENT '退款原因',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `crossuserid` bigint(20) DEFAULT 0 COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT 0 COMMENT '跨表来源id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='办卡记录退款';

-- 取消商品订单表
DROP TABLE IF EXISTS `cancelproductorder`;
CREATE TABLE `cancelproductorder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单编号',
  `productname` varchar(500) COMMENT '商品名称',
  `productimage` varchar(500) COMMENT '商品图片',
  `producttype` varchar(500) COMMENT '商品分类',
  `productprice` double DEFAULT 0 COMMENT '商品单价',
  `quantity` int(11) DEFAULT 0 COMMENT '购买数量',
  `totalprice` double DEFAULT 0 COMMENT '订单总价',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `cancelreason` varchar(500) COMMENT '取消原因',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `crossuserid` bigint(20) DEFAULT 0 COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT 0 COMMENT '跨表来源id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='取消商品订单';

-- 商品订单退款表
DROP TABLE IF EXISTS `refundproductorder`;
CREATE TABLE `refundproductorder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单编号',
  `productname` varchar(500) COMMENT '商品名称',
  `productimage` varchar(500) COMMENT '商品图片',
  `producttype` varchar(500) COMMENT '商品分类',
  `productprice` double DEFAULT 0 COMMENT '商品单价',
  `quantity` int(11) DEFAULT 0 COMMENT '购买数量',
  `totalprice` double DEFAULT 0 COMMENT '订单总价',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `refundreason` varchar(500) COMMENT '退款原因',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `crossuserid` bigint(20) DEFAULT 0 COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT 0 COMMENT '跨表来源id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品订单退款';

-- 商品订单评论表表
DROP TABLE IF EXISTS `discussproductorder`;
CREATE TABLE `discussproductorder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `refid` bigint(20) DEFAULT 0 COMMENT '关联表id',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户id',
  `nickname` varchar(500) COMMENT '用户名',
  `avatarurl` varchar(500) COMMENT '头像',
  `content` varchar(500) COMMENT '评论内容',
  `reply` varchar(500) COMMENT '回复内容',
  `likenum` int(11) DEFAULT 0 COMMENT '点赞数',
  `stepnum` int(11) DEFAULT 0 COMMENT '踩数',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品订单评论表';

-- 取消续卡记录表
DROP TABLE IF EXISTS `cancelcardrenewal`;
CREATE TABLE `cancelcardrenewal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '续卡编号',
  `packagename` varchar(500) COMMENT '套餐名称',
  `packagetype` varchar(500) COMMENT '套餐类型',
  `packageprice` double DEFAULT 0 COMMENT '价格',
  `renewaldays` int(11) DEFAULT 0 COMMENT '续费时长',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `cancelreason` varchar(500) COMMENT '取消原因',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `crossuserid` bigint(20) DEFAULT 0 COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT 0 COMMENT '跨表来源id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='取消续卡记录';

-- 续卡记录退款表
DROP TABLE IF EXISTS `refundcardrenewal`;
CREATE TABLE `refundcardrenewal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '续卡编号',
  `packagename` varchar(500) COMMENT '套餐名称',
  `packagetype` varchar(500) COMMENT '套餐类型',
  `packageprice` double DEFAULT 0 COMMENT '价格',
  `renewaldays` int(11) DEFAULT 0 COMMENT '续费时长',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `refundreason` varchar(500) COMMENT '退款原因',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `crossuserid` bigint(20) DEFAULT 0 COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT 0 COMMENT '跨表来源id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='续卡记录退款';

-- 会员交流评论表表
DROP TABLE IF EXISTS `discussshare`;
CREATE TABLE `discussshare` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `refid` bigint(20) DEFAULT 0 COMMENT '关联表id',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户id',
  `nickname` varchar(500) COMMENT '用户名',
  `avatarurl` varchar(500) COMMENT '头像',
  `content` varchar(500) COMMENT '评论内容',
  `reply` varchar(500) COMMENT '回复内容',
  `likenum` int(11) DEFAULT 0 COMMENT '点赞数',
  `stepnum` int(11) DEFAULT 0 COMMENT '踩数',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员交流评论表';

-- 公告资讯评论表表
DROP TABLE IF EXISTS `discussnews`;
CREATE TABLE `discussnews` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `refid` bigint(20) DEFAULT 0 COMMENT '关联表id',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户id',
  `nickname` varchar(500) COMMENT '用户名',
  `avatarurl` varchar(500) COMMENT '头像',
  `content` varchar(500) COMMENT '评论内容',
  `reply` varchar(500) COMMENT '回复内容',
  `likenum` int(11) DEFAULT 0 COMMENT '点赞数',
  `stepnum` int(11) DEFAULT 0 COMMENT '踩数',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告资讯评论表';

-- 管理员表
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `adminaccount` varchar(500) COMMENT '账号',
  `adminpassword` varchar(500) COMMENT '密码',
  `image` varchar(500) COMMENT '头像',
  `role` varchar(500) COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员';

-- 公告分类表
DROP TABLE IF EXISTS `newstype`;
CREATE TABLE `newstype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `typename` varchar(500) COMMENT '分类名称',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告分类';

-- 公告资讯表
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(500) COMMENT '标题',
  `introduction` varchar(500) COMMENT '简介',
  `typename` varchar(500) COMMENT '分类名称',
  `name` varchar(500) COMMENT '发布人',
  `avatar` varchar(500) COMMENT '头像',
  `image` varchar(500) COMMENT '图片',
  `content` varchar(500) COMMENT '内容',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `favoritenum` int(11) DEFAULT 0 COMMENT '收藏数',
  `likenum` int(11) DEFAULT 0 COMMENT '点赞数',
  `stepnum` int(11) DEFAULT 0 COMMENT '踩数',
  `clicknum` int(11) DEFAULT 0 COMMENT '点击数',
  `clicktime` timestamp NULL DEFAULT NULL COMMENT '最后点击时间',
  `auditstatus` varchar(500) COMMENT '审核状态',
  `auditreply` varchar(500) COMMENT '审核回复',
  `discussnum` int(11) DEFAULT 0 COMMENT '评论数',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告资讯';

-- 公告资讯评论表
DROP TABLE IF EXISTS `discussnews`;
CREATE TABLE `discussnews` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `avatarurl` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `nickname` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `likenum` int(11) DEFAULT '0' COMMENT '点赞数',
  `stepnum` int(11) DEFAULT '0' COMMENT '踩数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告资讯评论表';

-- 轮播图表
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(500) COMMENT '配置参数名称',
  `value` varchar(500) COMMENT '配置参数值',
  `url` varchar(500) COMMENT 'url',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮播图';

-- 操作日志表
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tablename` varchar(500) COMMENT '业务表',
  `module` varchar(500) COMMENT '模块名称',
  `operatetype` varchar(500) COMMENT '操作类型',
  `businessid` bigint(20) DEFAULT 0 COMMENT '业务ID',
  `operatorid` bigint(20) DEFAULT 0 COMMENT '操作人ID',
  `operatorname` varchar(500) COMMENT '操作人名称',
  `requesturl` varchar(500) COMMENT '请求路径',
  `requestmethod` varchar(500) COMMENT '请求方法',
  `requestip` varchar(500) COMMENT '请求IP',
  `detail` varchar(2000) COMMENT '操作明细',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志';

-- 消息通知表
DROP TABLE IF EXISTS `notify`;
CREATE TABLE `notify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) DEFAULT 0 COMMENT '接收用户ID',
  `title` varchar(500) COMMENT '消息标题',
  `content` varchar(500) COMMENT '消息内容',
  `messagetype` varchar(500) COMMENT '消息类型',
  `readstatus` varchar(500) COMMENT '阅读状态',
  `senduser` varchar(500) COMMENT '发送人',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息通知';

-- 在线客服表
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `adminid` bigint(20) DEFAULT 0 COMMENT '管理员ID',
  `ask` varchar(500) COMMENT '提问',
  `reply` varchar(500) COMMENT '回复',
  `asktype` int(11) DEFAULT 0 COMMENT '提问类型(1文本/2图片)',
  `replytype` int(11) DEFAULT 0 COMMENT '回复类型(1文本/2图片)',
  `isreply` int(11) DEFAULT 0 COMMENT '是否回复(0/1)',
  `isread` int(11) DEFAULT 0 COMMENT '是否已读(0/1)',
  `uname` varchar(500) COMMENT '用户名',
  `uimage` varchar(500) COMMENT '用户头像',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='在线客服';

-- 消息表表
DROP TABLE IF EXISTS `chatmessage`;
CREATE TABLE `chatmessage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `fid` bigint(20) DEFAULT 0 COMMENT '好友用户ID',
  `tablename` varchar(500) COMMENT '关联表名',
  `content` varchar(500) COMMENT '内容',
  `format` int(11) DEFAULT 0 COMMENT '格式(1:文字，2:图片)',
  `isread` int(11) DEFAULT 0 COMMENT '消息已读(0:未读，1:已读)',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- 好友表表
DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `fid` bigint(20) DEFAULT 0 COMMENT '好友用户ID',
  `name` varchar(500) COMMENT '好友昵称',
  `picture` varchar(500) COMMENT '头像',
  `role` varchar(500) COMMENT '角色标识',
  `tablename` varchar(500) COMMENT '表名',
  `alias` varchar(500) COMMENT '好友备注',
  `type` int(11) DEFAULT 0 COMMENT '关系类型',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友表';

-- 用户互动统一管理表(收藏/点赞/踩)
DROP TABLE IF EXISTS `social`;
CREATE TABLE `social` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `refid` bigint(20) NOT NULL COMMENT '关联记录ID',
  `tablename` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联表名',
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '记录标题/名称',
  `picture` longtext COLLATE utf8mb4_unicode_ci COMMENT '记录图片',
  `type` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '操作类型(1:收藏,21:点赞,22:踩)',
  `inteltype` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '智能推荐类型',
  `remark` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_record_type` (`userid`, `refid`, `tablename`, `type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户互动记录表';

-- 登录信息
DROP TABLE IF EXISTS `token`;
create table token
(
    id            bigint auto_increment comment '主键'
        primary key,
    userid        bigint                              not null comment '用户id',
    username      varchar(100)                        not null comment '用户名',
    tablename     varchar(100)                        null comment '表名',
    role          varchar(100)                        null comment '角色',
    token         varchar(200)                        not null comment '密码',
    addtime       timestamp default CURRENT_TIMESTAMP not null comment '新增时间',
    updatetime    timestamp default CURRENT_TIMESTAMP not null comment '新增时间',
    expiratedtime timestamp default CURRENT_TIMESTAMP not null comment '过期时间'
)
    comment 'token表' charset = utf8;

-- 系统消息表
DROP TABLE IF EXISTS `notify`;
CREATE TABLE `notify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `userid` bigint(20) DEFAULT '0' COMMENT '接收用户ID',
  `title` varchar(200) DEFAULT '' COMMENT '消息标题',
  `content` varchar(1000) DEFAULT '' COMMENT '消息内容',
  `messagetype` varchar(200) DEFAULT '' COMMENT '消息类型（申请通知、审核通知、系统通知等等）',
  `readstatus` varchar(2) DEFAULT '0' COMMENT '阅读状态（0未读 1已读）',
  `senduser` varchar(200) DEFAULT '系统' COMMENT '发送人',
  `addtime` timestamp default CURRENT_TIMESTAMP not null comment '记录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统消息表';

-- 聊天消息表
DROP TABLE IF EXISTS `chatmessage`;
CREATE TABLE `chatmessage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `fid` bigint(20) DEFAULT NULL COMMENT '好友用户ID',
  `tablename` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联表名',
  `content` longtext COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `format` int(11) DEFAULT 1 COMMENT '格式(1:文字，2:图片)',
  `isread` int(11) DEFAULT 0 COMMENT '消息已读(0:未读，1:已读)',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';

-- 在线客服表
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `adminid` bigint(20) DEFAULT NULL COMMENT '管理员ID',
  `ask` longtext COLLATE utf8mb4_unicode_ci COMMENT '提问',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复',
  `asktype` int(11) DEFAULT 1 COMMENT '类型(1文本/2图片)',
  `replytype` int(11) DEFAULT 1 COMMENT '类型(1文本/2图片)',
  `isreply` int(11) DEFAULT 0 COMMENT '是否回复(0/1)',
  `isread` int(11) DEFAULT 0 COMMENT '是否已读(0/1)',
  `uname` varchar(200) DEFAULT NULL COMMENT '用户名',
  `uimage` longtext COMMENT '用户头像',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='在线客服表';

-- 教练-会员绑定表
DROP TABLE IF EXISTS `coachmember`;
CREATE TABLE `coachmember` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `coachid` bigint(20) DEFAULT 0 COMMENT '教练ID',
  `coachaccount` varchar(500) COMMENT '教练账号',
  `coachname` varchar(500) COMMENT '教练姓名',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `useraccount` varchar(500) COMMENT '用户账号',
  `username` varchar(500) COMMENT '用户姓名',
  `phone` varchar(500) COMMENT '手机号码',
  `userimage` varchar(500) COMMENT '用户头像',
  `bindstatus` varchar(500) DEFAULT '已绑定' COMMENT '绑定状态',
  `remark` varchar(500) COMMENT '备注',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_coach_user` (`coachid`,`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教练-会员绑定';

-- 训练计划表
DROP TABLE IF EXISTS `trainingplan`;
CREATE TABLE `trainingplan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `planname` varchar(500) COMMENT '计划名称',
  `plangoal` varchar(500) COMMENT '训练目标',
  `plancontent` varchar(2000) COMMENT '计划内容',
  `starttime` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `endtime` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `auditstatus` varchar(500) COMMENT '审核状态',
  `auditreply` varchar(500) COMMENT '审核回复',
  `planstatus` varchar(500) COMMENT '计划状态',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `useraccount` varchar(500) COMMENT '用户账号',
  `username` varchar(500) COMMENT '用户姓名',
  `coachid` bigint(20) DEFAULT 0 COMMENT '教练ID',
  `coachaccount` varchar(500) COMMENT '教练账号',
  `coachname` varchar(500) COMMENT '教练姓名',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='训练计划';

-- 训练计划明细表
DROP TABLE IF EXISTS `trainingplanitem`;
CREATE TABLE `trainingplanitem` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `planid` bigint(20) DEFAULT 0 COMMENT '计划ID',
  `planname` varchar(500) COMMENT '计划名称',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `coachid` bigint(20) DEFAULT 0 COMMENT '教练ID',
  `dayno` int(11) DEFAULT 0 COMMENT '第几天/序号',
  `itemcontent` varchar(2000) COMMENT '训练内容',
  `target` varchar(500) COMMENT '目标',
  `sortno` int(11) DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) COMMENT '备注',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='训练计划明细';

-- 训练记录表
DROP TABLE IF EXISTS `trainingrecord`;
CREATE TABLE `trainingrecord` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `planid` bigint(20) DEFAULT 0 COMMENT '计划ID',
  `planname` varchar(500) COMMENT '计划名称',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `useraccount` varchar(500) COMMENT '用户账号',
  `username` varchar(500) COMMENT '用户姓名',
  `coachid` bigint(20) DEFAULT 0 COMMENT '教练ID',
  `coachaccount` varchar(500) COMMENT '教练账号',
  `coachname` varchar(500) COMMENT '教练姓名',
  `recorddate` timestamp NULL DEFAULT NULL COMMENT '记录时间',
  `duration` int(11) DEFAULT 0 COMMENT '训练时长(分钟)',
  `completionrate` int(11) DEFAULT 0 COMMENT '完成度(%)',
  `content` varchar(2000) COMMENT '训练内容',
  `coachcomment` varchar(2000) COMMENT '教练点评',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='训练记录';

-- 教练评价表
DROP TABLE IF EXISTS `coachreview`;
CREATE TABLE `coachreview` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `coachid` bigint(20) DEFAULT 0 COMMENT '教练ID',
  `coachaccount` varchar(500) COMMENT '教练账号',
  `coachname` varchar(500) COMMENT '教练姓名',
  `userid` bigint(20) DEFAULT 0 COMMENT '用户ID',
  `useraccount` varchar(500) COMMENT '用户账号',
  `username` varchar(500) COMMENT '用户姓名',
  `userimage` varchar(500) COMMENT '用户头像',
  `rating` int(11) DEFAULT 0 COMMENT '评分',
  `content` varchar(2000) COMMENT '评价内容',
  `reply` varchar(2000) COMMENT '回复内容',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教练评价';
