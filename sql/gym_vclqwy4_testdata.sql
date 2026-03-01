-- ============================================================
-- gym_vclqwy4 综合测试数据
-- 覆盖所有业务表，仅使用 upload 目录中实际存在的图片
-- 可用图片: avatar1-10, config1-3, course1-5, news1-5,
--           package1-5, product1-5, share1-5
-- 生成时间：2026-02-27
-- ============================================================
USE `gym_vclqwy4`;
SET FOREIGN_KEY_CHECKS=0;

-- 清空所有业务数据
DELETE FROM `social`;
DELETE FROM `friend`;
DELETE FROM `chatmessage`;
DELETE FROM `chat`;
DELETE FROM `notify`;
DELETE FROM `discussnews`;
DELETE FROM `news`;
DELETE FROM `newstype`;
DELETE FROM `feedback`;
DELETE FROM `discussshare`;
DELETE FROM `share`;
DELETE FROM `sharetype`;
DELETE FROM `refundcardrenewal`;
DELETE FROM `cancelcardrenewal`;
DELETE FROM `cardrenewal`;
DELETE FROM `refundcardapplication`;
DELETE FROM `cancelcardapplication`;
DELETE FROM `cardapplication`;
DELETE FROM `membershippackage`;
DELETE FROM `discussproductorder`;
DELETE FROM `refundproductorder`;
DELETE FROM `cancelproductorder`;
DELETE FROM `productorder`;
DELETE FROM `discussproduct`;
DELETE FROM `product`;
DELETE FROM `producttype`;
DELETE FROM `refundcourseenrollment`;
DELETE FROM `cancelcourseenrollment`;
DELETE FROM `courseenrollment`;
DELETE FROM `discusscourse`;
DELETE FROM `course`;
DELETE FROM `coursetype`;
DELETE FROM `coachreview`;
DELETE FROM `trainingrecord`;
DELETE FROM `trainingplanitem`;
DELETE FROM `trainingplan`;
DELETE FROM `coachmember`;
DELETE FROM `coach`;
DELETE FROM `user`;
DELETE FROM `config`;
DELETE FROM `admin`;
DELETE FROM `token`;
DELETE FROM `log`;

-- ============================================================
-- 1. 管理员
-- ============================================================
INSERT INTO `admin` (`id`,`adminaccount`,`adminpassword`,`image`,`role`) VALUES
  (1,'admin','123456','/upload/avatar10.jpg','管理员');

-- ============================================================
-- 2. 轮播图（3张，对应 config1-3.jpg）
-- ============================================================
INSERT INTO `config` (`id`,`name`,`value`,`url`) VALUES
  (1,'轮播图1','健身从这里开始','/upload/config1.jpg'),
  (2,'轮播图2','专业教练团队','/upload/config2.jpg'),
  (3,'轮播图3','春季特惠活动','/upload/config3.jpg');

-- ============================================================
-- 3. 用户（8个，使用 avatar1-8.jpg）
-- ============================================================
INSERT INTO `user` (`id`,`useraccount`,`userpassword`,`name`,`sex`,`age`,`phone`,`image`,`money`,`cardno`,`memberlevel`,`expiretime`,`remainingcourses`) VALUES
  (1,'user001','123456','张三','男',25,'13800000001','/upload/avatar1.jpg',500.00,'VIP001','金卡','2026-12-31 23:59:59',20),
  (2,'user002','123456','李四','女',28,'13800000002','/upload/avatar2.jpg',350.00,'VIP002','银卡','2026-10-31 23:59:59',15),
  (3,'user003','123456','王五','男',32,'13800000003','/upload/avatar3.jpg',200.00,'VIP003','普通','2026-08-31 23:59:59',8),
  (4,'user004','123456','赵六','女',22,'13800000004','/upload/avatar4.jpg',150.00,'VIP004','钻石','2027-06-30 23:59:59',30),
  (5,'user005','123456','孙七','男',35,'13800000005','/upload/avatar5.jpg',80.00,'VIP005','普通','2026-05-31 23:59:59',5),
  (6,'user006','123456','周八','女',26,'13800000006','/upload/avatar6.jpg',600.00,'VIP006','金卡','2026-11-30 23:59:59',18),
  (7,'user007','123456','吴九','男',30,'13800000007','/upload/avatar7.jpg',100.00,'VIP007','银卡','2026-09-30 23:59:59',12),
  (8,'user008','123456','郑十','女',24,'13800000008','/upload/avatar8.jpg',250.00,'VIP008','普通','2026-07-31 23:59:59',6);

-- ============================================================
-- 4. 教练（5个，使用 avatar6-10.jpg 作为教练头像）
-- ============================================================
INSERT INTO `coach` (`id`,`coachaccount`,`coachpassword`,`coachname`,`coachimage`,`sex`,`phone`,`jobno`,`specialty`,`coachlevel`,`workyears`) VALUES
  (1,'coach001','123456','李教练','/upload/avatar6.jpg','男','13900000001','C001','力量训练','高级',8),
  (2,'coach002','123456','王教练','/upload/avatar7.jpg','女','13900000002','C002','有氧塑形','中级',5),
  (3,'coach003','123456','陈教练','/upload/avatar8.jpg','男','13900000003','C003','体能训练','高级',6),
  (4,'coach004','123456','赵教练','/upload/avatar9.jpg','女','13900000004','C004','瑜伽普拉提','资深',10),
  (5,'coach005','123456','刘教练','/upload/avatar10.jpg','男','13900000005','C005','搏击格斗','中级',4);

-- ============================================================
-- 5. 课程分类（5个）
-- ============================================================
INSERT INTO `coursetype` (`id`,`coursetypename`) VALUES
  (1,'增肌'),(2,'减脂'),(3,'体能'),(4,'瑜伽'),(5,'搏击');

-- ============================================================
-- 6. 健身课程（5个，对应 course1-5.jpg）
-- ============================================================
INSERT INTO `course` (`id`,`coursename`,`courseimage`,`coursetype`,`classtime`,`duration`,`coachname`,`coachaccount`,`courseprice`,`quota`,`coursedesc`,`auditstatus`,`auditreply`,`favoritenum`,`likenum`,`clicknum`,`discussnum`) VALUES
  (1,'力量基础入门','/upload/course1.jpg','增肌','2026-03-05 09:00:00',60,'李教练','coach001',199,20,'适合零基础学员的力量训练入门课程','通过','',12,8,56,3),
  (2,'燃脂有氧操','/upload/course2.jpg','减脂','2026-03-06 18:00:00',50,'王教练','coach002',159,25,'高效燃脂有氧操，轻松减肥','通过','',20,15,89,2),
  (3,'HIIT极限挑战','/upload/course3.jpg','体能','2026-03-07 07:30:00',45,'陈教练','coach003',189,15,'高强度间歇训练，突破体能极限','通过','',8,6,42,2),
  (4,'流瑜伽入门','/upload/course4.jpg','瑜伽','2026-03-08 10:00:00',75,'赵教练','coach004',149,30,'舒缓身心，提高柔韧性','通过','',25,18,105,2),
  (5,'拳击健身课','/upload/course5.jpg','搏击','2026-03-09 19:00:00',60,'刘教练','coach005',179,18,'基础拳击动作与体能结合','待审核','',10,7,38,1);

-- ============================================================
-- 7. 课程评论
-- ============================================================
INSERT INTO `discusscourse` (`id`,`refid`,`userid`,`nickname`,`avatarurl`,`content`,`reply`,`likenum`,`stepnum`) VALUES
  (1,1,1,'张三','/upload/avatar1.jpg','课程安排很合理，教练讲解清晰','感谢反馈',5,0),
  (2,1,2,'李四','/upload/avatar2.jpg','适合新手入门','',3,0),
  (3,1,4,'赵六','/upload/avatar4.jpg','学到了很多基础动作','',2,0),
  (4,2,3,'王五','/upload/avatar3.jpg','强度适中，出汗很多','',4,0),
  (5,2,6,'周八','/upload/avatar6.jpg','音乐很带感','',2,1),
  (6,3,5,'孙七','/upload/avatar5.jpg','太累了但很爽','',3,0),
  (7,3,7,'吴九','/upload/avatar7.jpg','心率飙升，效果好','',1,0),
  (8,4,2,'李四','/upload/avatar2.jpg','很放松，身心愉悦','',6,0),
  (9,4,8,'郑十','/upload/avatar8.jpg','教练引导很到位','',4,0),
  (10,5,1,'张三','/upload/avatar1.jpg','打拳很解压','',2,0);

-- ============================================================
-- 8. 课程报名记录（8条）
-- ============================================================
INSERT INTO `courseenrollment` (`id`,`coursename`,`courseimage`,`coursetype`,`classtime`,`duration`,`coachname`,`userid`,`useraccount`,`coachaccount`,`courseprice`,`quantity`,`totalprice`,`ispay`,`orderstatus`,`crossuserid`,`crossrefid`) VALUES
  (1,'力量基础入门','/upload/course1.jpg','增肌','2026-03-05 09:00:00',60,'李教练',1,'user001','coach001',199,1,199,'已支付','已完成',1,1),
  (2,'燃脂有氧操','/upload/course2.jpg','减脂','2026-03-06 18:00:00',50,'王教练',1,'user001','coach002',159,1,159,'已支付','已支付',1,2),
  (3,'力量基础入门','/upload/course1.jpg','增肌','2026-03-05 09:00:00',60,'李教练',2,'user002','coach001',199,1,199,'已支付','已完成',2,1),
  (4,'流瑜伽入门','/upload/course4.jpg','瑜伽','2026-03-08 10:00:00',75,'赵教练',2,'user002','coach004',149,1,149,'已支付','已支付',2,4),
  (5,'HIIT极限挑战','/upload/course3.jpg','体能','2026-03-07 07:30:00',45,'陈教练',3,'user003','coach003',189,1,189,'未支付','未支付',3,3),
  (6,'拳击健身课','/upload/course5.jpg','搏击','2026-03-09 19:00:00',60,'刘教练',4,'user004','coach005',179,1,179,'已支付','已发货',4,5),
  (7,'燃脂有氧操','/upload/course2.jpg','减脂','2026-03-06 18:00:00',50,'王教练',5,'user005','coach002',159,1,159,'已支付','已完成',5,2),
  (8,'流瑜伽入门','/upload/course4.jpg','瑜伽','2026-03-08 10:00:00',75,'赵教练',8,'user008','coach004',149,1,149,'已支付','已取消',8,4);

-- ============================================================
-- 9. 取消课程报名（3条）
-- ============================================================
INSERT INTO `cancelcourseenrollment` (`id`,`coursename`,`courseimage`,`coursetype`,`classtime`,`duration`,`coachname`,`userid`,`cancelreason`,`crossuserid`,`crossrefid`) VALUES
  (1,'HIIT极限挑战','/upload/course3.jpg','体能','2026-03-07 07:30:00',45,'陈教练',3,'时间冲突，无法参加',3,3),
  (2,'流瑜伽入门','/upload/course4.jpg','瑜伽','2026-03-08 10:00:00',75,'赵教练',8,'身体不适',8,4),
  (3,'燃脂有氧操','/upload/course2.jpg','减脂','2026-03-06 18:00:00',50,'王教练',7,'出差无法参加',7,2);

-- ============================================================
-- 10. 课程报名退款（2条）
-- ============================================================
INSERT INTO `refundcourseenrollment` (`id`,`coursename`,`courseimage`,`coursetype`,`classtime`,`duration`,`coachname`,`userid`,`refundreason`,`crossuserid`,`crossrefid`) VALUES
  (1,'燃脂有氧操','/upload/course2.jpg','减脂','2026-03-06 18:00:00',50,'王教练',5,'课程与预期不符',5,2),
  (2,'拳击健身课','/upload/course5.jpg','搏击','2026-03-09 19:00:00',60,'刘教练',4,'受伤无法继续',4,5);

-- ============================================================
-- 11. 商品分类（5个）
-- ============================================================
INSERT INTO `producttype` (`id`,`producttypename`) VALUES
  (1,'健身器材'),(2,'运动营养'),(3,'运动服饰'),(4,'运动配件'),(5,'健身饮品');

-- ============================================================
-- 12. 健身商品（5个，对应 product1-5.jpg）
-- ============================================================
INSERT INTO `product` (`id`,`productname`,`productimage`,`producttype`,`productprice`,`stock`,`productdesc`,`auditstatus`,`auditreply`,`favoritenum`,`likenum`,`clicknum`,`discussnum`) VALUES
  (1,'可调节哑铃套装','/upload/product1.jpg','健身器材',299,50,'10-40kg可调节哑铃，家用健身必备','通过','',18,12,88,2),
  (2,'TPE瑜伽垫','/upload/product2.jpg','健身器材',89,120,'6mm加厚防滑瑜伽垫','通过','',22,16,102,2),
  (3,'乳清蛋白粉','/upload/product3.jpg','运动营养',259,80,'5磅装乳清蛋白粉，巧克力味','通过','',15,10,76,1),
  (4,'速干运动T恤','/upload/product4.jpg','运动服饰',129,200,'透气速干面料，多色可选','通过','',30,20,135,2),
  (5,'运动护膝','/upload/product5.jpg','运动配件',69,150,'弹力支撑护膝，保护关节','待审核','',8,5,45,1);

-- ============================================================
-- 13. 商品评论
-- ============================================================
INSERT INTO `discussproduct` (`id`,`refid`,`userid`,`nickname`,`avatarurl`,`content`,`reply`,`likenum`,`stepnum`) VALUES
  (1,1,1,'张三','/upload/avatar1.jpg','质量不错，手感很好','感谢支持',4,0),
  (2,1,3,'王五','/upload/avatar3.jpg','调节方便，适合家用','',2,0),
  (3,2,2,'李四','/upload/avatar2.jpg','防滑效果好，厚度合适','',5,0),
  (4,2,6,'周八','/upload/avatar6.jpg','颜色好看，材质柔软','',3,0),
  (5,3,4,'赵六','/upload/avatar4.jpg','溶解快，口感不错','',2,0),
  (6,4,5,'孙七','/upload/avatar5.jpg','面料很舒服，透气','',6,0),
  (7,4,8,'郑十','/upload/avatar8.jpg','尺码准确','',1,0),
  (8,5,7,'吴九','/upload/avatar7.jpg','支撑性好','',2,0);

-- ============================================================
-- 14. 商品订单（8条）
-- ============================================================
INSERT INTO `productorder` (`id`,`productname`,`productimage`,`producttype`,`productprice`,`quantity`,`totalprice`,`ispay`,`orderstatus`,`userid`,`crossuserid`,`crossrefid`) VALUES
  (1,'可调节哑铃套装','/upload/product1.jpg','健身器材',299,1,299,'已支付','已完成',1,1,1),
  (2,'TPE瑜伽垫','/upload/product2.jpg','健身器材',89,2,178,'已支付','已发货',2,2,2),
  (3,'乳清蛋白粉','/upload/product3.jpg','运动营养',259,1,259,'已支付','已完成',3,3,3),
  (4,'速干运动T恤','/upload/product4.jpg','运动服饰',129,3,387,'已支付','已支付',4,4,4),
  (5,'运动护膝','/upload/product5.jpg','运动配件',69,2,138,'未支付','未支付',5,5,5),
  (6,'可调节哑铃套装','/upload/product1.jpg','健身器材',299,1,299,'已支付','已完成',6,6,1),
  (7,'速干运动T恤','/upload/product4.jpg','运动服饰',129,1,129,'已支付','已取消',7,7,4),
  (8,'TPE瑜伽垫','/upload/product2.jpg','健身器材',89,1,89,'已支付','已完成',4,4,2);

-- ============================================================
-- 15. 商品订单评论
-- ============================================================
INSERT INTO `discussproductorder` (`id`,`refid`,`userid`,`nickname`,`avatarurl`,`content`,`reply`,`likenum`,`stepnum`) VALUES
  (1,1,1,'张三','/upload/avatar1.jpg','发货速度快，包装完好','感谢好评',3,0),
  (2,2,2,'李四','/upload/avatar2.jpg','物流很快','',2,0),
  (3,3,3,'王五','/upload/avatar3.jpg','品质正品','',4,0),
  (4,6,6,'周八','/upload/avatar6.jpg','哑铃很好用','',1,0),
  (5,8,4,'赵六','/upload/avatar4.jpg','回购了，很满意','',2,0);

-- ============================================================
-- 16. 取消商品订单（2条）
-- ============================================================
INSERT INTO `cancelproductorder` (`id`,`productname`,`productimage`,`producttype`,`productprice`,`quantity`,`totalprice`,`userid`,`cancelreason`,`crossuserid`,`crossrefid`) VALUES
  (1,'速干运动T恤','/upload/product4.jpg','运动服饰',129,1,129,7,'不需要了',7,4),
  (2,'运动护膝','/upload/product5.jpg','运动配件',69,2,138,5,'买错型号',5,5);

-- ============================================================
-- 17. 商品订单退款（2条）
-- ============================================================
INSERT INTO `refundproductorder` (`id`,`productname`,`productimage`,`producttype`,`productprice`,`quantity`,`totalprice`,`userid`,`refundreason`,`crossuserid`,`crossrefid`) VALUES
  (1,'可调节哑铃套装','/upload/product1.jpg','健身器材',299,1,299,8,'收到有瑕疵',8,1),
  (2,'速干运动T恤','/upload/product4.jpg','运动服饰',129,3,387,4,'尺码不合适',4,4);

-- ============================================================
-- 18. 会员卡套餐（5个，对应 package1-5.jpg）
-- ============================================================
INSERT INTO `membershippackage` (`id`,`packagename`,`packageimage`,`packagetype`,`packageprice`,`validdays`,`includedcourses`,`packagedesc`) VALUES
  (1,'体验月卡','/upload/package1.jpg','月卡',199,30,8,'适合新人体验，含8节课程'),
  (2,'标准季卡','/upload/package2.jpg','季卡',499,90,30,'季度畅练，性价比之选'),
  (3,'尊享半年卡','/upload/package3.jpg','半年卡',899,180,60,'半年无忧健身'),
  (4,'至尊年卡','/upload/package4.jpg','年卡',1599,365,120,'全年畅享所有课程'),
  (5,'学生特惠卡','/upload/package5.jpg','季卡',399,90,25,'学生专属优惠');

-- ============================================================
-- 19. 办卡记录（6条）
-- ============================================================
INSERT INTO `cardapplication` (`id`,`packagename`,`packageimage`,`packagetype`,`packageprice`,`validdays`,`includedcourses`,`quantity`,`userid`,`ispay`,`orderstatus`,`crossuserid`,`crossrefid`) VALUES
  (1,'体验月卡','/upload/package1.jpg','月卡',199,30,8,1,1,'已支付','已完成',1,1),
  (2,'标准季卡','/upload/package2.jpg','季卡',499,90,30,1,2,'已支付','已完成',2,2),
  (3,'至尊年卡','/upload/package4.jpg','年卡',1599,365,120,1,4,'已支付','已支付',4,4),
  (4,'学生特惠卡','/upload/package5.jpg','季卡',399,90,25,1,5,'未支付','未支付',5,5),
  (5,'尊享半年卡','/upload/package3.jpg','半年卡',899,180,60,1,6,'已支付','已完成',6,3),
  (6,'体验月卡','/upload/package1.jpg','月卡',199,30,8,1,8,'已支付','已支付',8,1);

-- ============================================================
-- 20. 取消办卡（2条）
-- ============================================================
INSERT INTO `cancelcardapplication` (`id`,`packagename`,`packageimage`,`packagetype`,`packageprice`,`validdays`,`includedcourses`,`userid`,`cancelreason`,`crossuserid`,`crossrefid`) VALUES
  (1,'学生特惠卡','/upload/package5.jpg','季卡',399,90,25,5,'预算不足',5,5),
  (2,'体验月卡','/upload/package1.jpg','月卡',199,30,8,3,'时间安排不开',3,1);

-- ============================================================
-- 21. 办卡退款（1条）
-- ============================================================
INSERT INTO `refundcardapplication` (`id`,`packagename`,`packageimage`,`packagetype`,`packageprice`,`validdays`,`includedcourses`,`userid`,`refundreason`,`crossuserid`,`crossrefid`) VALUES
  (1,'体验月卡','/upload/package1.jpg','月卡',199,30,8,8,'搬家了，距离太远',8,1);

-- ============================================================
-- 22. 续卡记录（4条）
-- ============================================================
INSERT INTO `cardrenewal` (`id`,`packagename`,`packagetype`,`packageprice`,`renewaldays`,`ispay`,`orderstatus`,`userid`,`crossuserid`,`crossrefid`) VALUES
  (1,'体验月卡','月卡',199,30,'已支付','已完成',1,1,1),
  (2,'标准季卡','季卡',499,90,'已支付','已支付',2,2,2),
  (3,'至尊年卡','年卡',1599,365,'未支付','未支付',4,4,4),
  (4,'尊享半年卡','半年卡',899,180,'已支付','已完成',6,6,3);

-- ============================================================
-- 23. 取消续卡（1条）
-- ============================================================
INSERT INTO `cancelcardrenewal` (`id`,`packagename`,`packagetype`,`packageprice`,`renewaldays`,`userid`,`cancelreason`,`crossuserid`,`crossrefid`) VALUES
  (1,'至尊年卡','年卡',1599,365,4,'暂时不续费了',4,4);

-- ============================================================
-- 24. 续卡退款（1条）
-- ============================================================
INSERT INTO `refundcardrenewal` (`id`,`packagename`,`packagetype`,`packageprice`,`renewaldays`,`userid`,`refundreason`,`crossuserid`,`crossrefid`) VALUES
  (1,'体验月卡','月卡',199,30,1,'工作调动',1,1);

-- ============================================================
-- 25. 交流分类（5个）
-- ============================================================
INSERT INTO `sharetype` (`id`,`sharetypename`) VALUES
  (1,'训练日记'),(2,'饮食打卡'),(3,'减脂心得'),(4,'增肌分享'),(5,'器械教程');

-- ============================================================
-- 26. 会员交流（5条，对应 share1-5.jpg）
-- ============================================================
INSERT INTO `share` (`id`,`sharetitle`,`sharecontent`,`shareimage`,`sharetype`,`auditstatus`,`auditreply`,`favoritenum`,`likenum`,`clicknum`,`discussnum`) VALUES
  (1,'今日腿部训练打卡','完成深蹲5组x8次，腿举4组x12次','/upload/share1.jpg','训练日记','通过','',8,12,45,2),
  (2,'低脂高蛋白晚餐','鸡胸肉200g+西兰花+糙米饭','/upload/share2.jpg','饮食打卡','通过','',15,20,78,2),
  (3,'三个月减脂20斤心得','从80kg到70kg的减脂历程分享','/upload/share3.jpg','减脂心得','通过','',25,30,120,2),
  (4,'新手增肌指南','增肌的饮食和训练注意事项','/upload/share4.jpg','增肌分享','通过','',18,22,95,2),
  (5,'正确使用史密斯机','史密斯机深蹲和卧推动作要领','/upload/share5.jpg','器械教程','待审核','',10,8,50,1);

-- ============================================================
-- 27. 交流评论
-- ============================================================
INSERT INTO `discussshare` (`id`,`refid`,`userid`,`nickname`,`avatarurl`,`content`,`reply`,`likenum`,`stepnum`) VALUES
  (1,1,2,'李四','/upload/avatar2.jpg','很棒的训练量！','',3,0),
  (2,1,4,'赵六','/upload/avatar4.jpg','我也要加油','',2,0),
  (3,2,1,'张三','/upload/avatar1.jpg','看起来很健康','',4,0),
  (4,2,6,'周八','/upload/avatar6.jpg','收藏了','',1,0),
  (5,3,5,'孙七','/upload/avatar5.jpg','太励志了','',5,0),
  (6,3,7,'吴九','/upload/avatar7.jpg','请问饮食怎么控制？','',3,0),
  (7,4,8,'郑十','/upload/avatar8.jpg','干货满满','',2,0),
  (8,4,3,'王五','/upload/avatar3.jpg','受益匪浅','',4,0),
  (9,5,1,'张三','/upload/avatar1.jpg','终于学会了','',2,0);

-- ============================================================
-- 28. 意见反馈（6条）
-- ============================================================
INSERT INTO `feedback` (`id`,`feedbacktitle`,`feedbackcontent`,`feedbacktype`,`userid`,`auditstatus`,`auditreply`) VALUES
  (1,'建议增加晚间课程','希望20:00后也有课程安排','功能建议',1,'通过','已采纳，下月起增加晚间时段'),
  (2,'页面加载较慢','首页图片加载需要较长时间','问题反馈',2,'通过','已优化图片压缩'),
  (3,'更衣室清洁建议','更衣室卫生希望加强','问题反馈',3,'待审核',''),
  (4,'教练预约功能建议','希望能在线预约私教课','功能建议',4,'通过','功能开发中'),
  (5,'会员日活动建议','建议每周设立会员日优惠','功能建议',5,'通过','已安排每周三会员日'),
  (6,'投诉器械维护','跑步机故障三天未修','投诉',6,'待审核','');

-- ============================================================
-- 29. 公告分类（5个）
-- ============================================================
INSERT INTO `newstype` (`id`,`typename`) VALUES
  (1,'公告'),(2,'活动'),(3,'新闻'),(4,'健康知识'),(5,'优惠');

-- ============================================================
-- 30. 公告资讯（5条，对应 news1-5.jpg）
-- ============================================================
INSERT INTO `news` (`id`,`title`,`introduction`,`typename`,`name`,`avatar`,`image`,`content`,`auditstatus`,`auditreply`,`favoritenum`,`likenum`,`clicknum`,`discussnum`) VALUES
  (1,'春季健身活动来袭','参与打卡30天赢好礼','活动','管理员','/upload/avatar10.jpg','/upload/news1.jpg','活动规则：连续打卡30天即可获得价值199元的月卡一张。活动期间每日在APP完成训练打卡即可参与。','通过','',20,15,120,2),
  (2,'营业时间调整通知','3月起周末延长营业','公告','管理员','/upload/avatar10.jpg','/upload/news2.jpg','自2026年3月1日起，周末营业时间调整为7:00-23:00，比工作日延长2小时。','通过','',5,3,85,2),
  (3,'新教练团队加入','五位专业教练加盟','新闻','管理员','/upload/avatar10.jpg','/upload/news3.jpg','我们很高兴地宣布，五位拥有国际认证的专业教练已加入我们的团队，涵盖力量、瑜伽、搏击等多个领域。','通过','',12,8,68,1),
  (4,'科学饮食指南','健身期间如何合理饮食','健康知识','管理员','/upload/avatar10.jpg','/upload/news4.jpg','健身期间的饮食搭配非常重要。建议每天摄入蛋白质1.5-2g/kg体重，碳水化合物3-5g/kg体重。','通过','',30,25,200,2),
  (5,'会员专属优惠活动','季卡享8折优惠','优惠','管理员','/upload/avatar10.jpg','/upload/news5.jpg','即日起至2026年3月底，办理季卡及以上套餐享8折优惠，老会员续卡额外赠送5节私教课。','通过','',18,14,150,1);

-- ============================================================
-- 31. 公告评论
-- ============================================================
INSERT INTO `discussnews` (`id`,`refid`,`userid`,`nickname`,`avatarurl`,`content`,`reply`,`likenum`,`stepnum`) VALUES
  (1,1,1,'张三','/upload/avatar1.jpg','活动很期待，已经开始打卡了！','',5,0),
  (2,1,4,'赵六','/upload/avatar4.jpg','太好了，有动力了','',3,0),
  (3,2,2,'李四','/upload/avatar2.jpg','周末可以多练了','',2,0),
  (4,2,6,'周八','/upload/avatar6.jpg','已知悉','',1,0),
  (5,3,3,'王五','/upload/avatar3.jpg','期待新教练','',4,0),
  (6,4,5,'孙七','/upload/avatar5.jpg','饮食指南很有用','',6,0),
  (7,4,8,'郑十','/upload/avatar8.jpg','收藏学习','',3,0),
  (8,5,7,'吴九','/upload/avatar7.jpg','准备续卡了','',2,0);

-- ============================================================
-- 32. 教练-会员绑定（6条）
-- ============================================================
INSERT INTO `coachmember` (`id`,`coachid`,`coachaccount`,`coachname`,`userid`,`useraccount`,`username`,`phone`,`userimage`,`bindstatus`,`remark`) VALUES
  (1,1,'coach001','李教练',1,'user001','张三','13800000001','/upload/avatar1.jpg','已绑定','重点学员'),
  (2,1,'coach001','李教练',2,'user002','李四','13800000002','/upload/avatar2.jpg','已绑定',''),
  (3,2,'coach002','王教练',3,'user003','王五','13800000003','/upload/avatar3.jpg','已绑定','减脂目标'),
  (4,3,'coach003','陈教练',4,'user004','赵六','13800000004','/upload/avatar4.jpg','已绑定',''),
  (5,4,'coach004','赵教练',6,'user006','周八','13800000006','/upload/avatar6.jpg','已绑定','瑜伽学员'),
  (6,5,'coach005','刘教练',7,'user007','吴九','13800000007','/upload/avatar7.jpg','已绑定','搏击学员');

-- ============================================================
-- 33. 训练计划（5条）
-- ============================================================
INSERT INTO `trainingplan` (`id`,`planname`,`plangoal`,`plancontent`,`starttime`,`endtime`,`auditstatus`,`auditreply`,`planstatus`,`userid`,`useraccount`,`username`,`coachid`,`coachaccount`,`coachname`) VALUES
  (1,'力量提升计划','增肌提升力量','深蹲/卧推/硬拉三大项基础训练，配合辅助动作','2026-02-01 09:00:00','2026-03-31 09:00:00','通过','','进行中',1,'user001','张三',1,'coach001','李教练'),
  (2,'塑形减脂计划','降低体脂率至18%','有氧+核心训练结合，每周4次','2026-02-01 18:00:00','2026-03-31 18:00:00','通过','','进行中',2,'user002','李四',1,'coach001','李教练'),
  (3,'体能恢复计划','术后体能恢复','低强度有氧+柔韧性训练','2026-02-15 08:00:00','2026-04-15 08:00:00','通过','','进行中',3,'user003','王五',2,'coach002','王教练'),
  (4,'瑜伽入门计划','提高柔韧性','基础体式+呼吸训练','2026-02-10 10:00:00','2026-03-10 10:00:00','通过','','进行中',6,'user006','周八',4,'coach004','赵教练'),
  (5,'搏击技能计划','掌握基础拳法','拳法组合+步法训练','2026-02-20 19:00:00','2026-04-20 19:00:00','待审核','','未开始',7,'user007','吴九',5,'coach005','刘教练');

-- ============================================================
-- 34. 训练计划明细
-- ============================================================
INSERT INTO `trainingplanitem` (`id`,`planid`,`planname`,`userid`,`coachid`,`dayno`,`itemcontent`,`target`,`sortno`,`remark`) VALUES
  (1,1,'力量提升计划',1,1,1,'深蹲 5组x5次','腿部力量',1,'注意膝盖不超过脚尖'),
  (2,1,'力量提升计划',1,1,2,'卧推 5组x5次','胸部力量',2,'控制离心速度'),
  (3,1,'力量提升计划',1,1,3,'硬拉 5组x5次','背部力量',3,'保持腰背挺直'),
  (4,2,'塑形减脂计划',2,1,1,'跑步 30分钟','心肺耐力',1,'心率保持130-150'),
  (5,2,'塑形减脂计划',2,1,2,'平板支撑 4组x60秒','核心稳定',2,'保持身体一条直线'),
  (6,2,'塑形减脂计划',2,1,3,'卷腹 4组x20次','腹部塑形',3,'不要借力'),
  (7,3,'体能恢复计划',3,2,1,'椭圆机 20分钟','关节活动',1,'低阻力缓速'),
  (8,3,'体能恢复计划',3,2,2,'拉伸 15分钟','柔韧恢复',2,'每个动作30秒'),
  (9,4,'瑜伽入门计划',6,4,1,'山式+树式 15分钟','平衡感',1,'注意呼吸'),
  (10,4,'瑜伽入门计划',6,4,2,'战士一式+二式 15分钟','腿部力量',2,'保持髋部正对前方');

-- ============================================================
-- 35. 训练记录（6条）
-- ============================================================
INSERT INTO `trainingrecord` (`id`,`planid`,`planname`,`userid`,`useraccount`,`username`,`coachid`,`coachaccount`,`coachname`,`recorddate`,`duration`,`completionrate`,`content`,`coachcomment`) VALUES
  (1,1,'力量提升计划',1,'user001','张三',1,'coach001','李教练','2026-02-20 09:30:00',65,90,'完成深蹲5x5@80kg，卧推5x5@60kg','动作标准度提高，继续加重量'),
  (2,1,'力量提升计划',1,'user001','张三',1,'coach001','李教练','2026-02-22 09:30:00',70,85,'完成硬拉5x5@100kg，辅助动作','腰部注意保护，建议加腰带'),
  (3,2,'塑形减脂计划',2,'user002','李四',1,'coach001','李教练','2026-02-21 18:30:00',55,95,'跑步35分钟+核心训练20分钟','节奏控制得越来越好了'),
  (4,3,'体能恢复计划',3,'user003','王五',2,'coach002','王教练','2026-02-22 08:30:00',40,80,'椭圆机20分钟+全身拉伸','恢复进展良好，下周可增加强度'),
  (5,4,'瑜伽入门计划',6,'user006','周八',4,'coach004','赵教练','2026-02-23 10:00:00',60,88,'完成基础体式练习+冥想','平衡感有明显进步'),
  (6,2,'塑形减脂计划',2,'user002','李四',1,'coach001','李教练','2026-02-24 18:30:00',50,92,'HIIT 20分钟+拉伸','体脂率下降0.5%，保持！');

-- ============================================================
-- 36. 教练评价（5条）
-- ============================================================
INSERT INTO `coachreview` (`id`,`coachid`,`coachaccount`,`coachname`,`userid`,`useraccount`,`username`,`userimage`,`rating`,`content`,`reply`) VALUES
  (1,1,'coach001','李教练',1,'user001','张三','/upload/avatar1.jpg',5,'非常专业，计划详细具体','感谢支持，一起加油'),
  (2,1,'coach001','李教练',2,'user002','李四','/upload/avatar2.jpg',5,'很耐心，减脂效果好','看到你的进步很开心'),
  (3,2,'coach002','王教练',3,'user003','王五','/upload/avatar3.jpg',4,'课程节奏好，体能恢复明显','继续保持'),
  (4,4,'coach004','赵教练',6,'user006','周八','/upload/avatar6.jpg',5,'瑜伽教学非常专业','欢迎继续练习'),
  (5,5,'coach005','刘教练',7,'user007','吴九','/upload/avatar7.jpg',4,'搏击课很有激情','期待你的进步');

-- ============================================================
-- 37. 消息通知（8条）
-- ============================================================
INSERT INTO `notify` (`id`,`userid`,`title`,`content`,`messagetype`,`readstatus`,`senduser`) VALUES
  (1,1,'课程报名成功','您已成功报名【力量基础入门】课程','报名通知','1','系统'),
  (2,1,'支付提醒','您的【燃脂有氧操】课程订单待支付','支付通知','0','系统'),
  (3,2,'训练计划已发布','李教练为您制定了【塑形减脂计划】','计划通知','1','系统'),
  (4,3,'反馈已回复','您提交的反馈已处理，请查看','反馈通知','0','系统'),
  (5,4,'会员卡即将到期','您的钻石会员卡将于一个月后到期，请及时续费','系统通知','0','系统'),
  (6,5,'商品发货通知','您购买的【运动护膝】已发货','物流通知','0','系统'),
  (7,6,'新活动通知','春季健身打卡活动已上线，快来参与吧','活动通知','1','系统'),
  (8,2,'退款审核通过','您的退款申请已通过审核','退款通知','0','系统');

-- ============================================================
-- 38. 在线客服对话（4条）
-- ============================================================
INSERT INTO `chat` (`id`,`userid`,`adminid`,`ask`,`reply`,`asktype`,`replytype`,`isreply`,`isread`,`uname`,`uimage`) VALUES
  (1,1,1,'请问课程时间可以调整吗？','可以联系您的教练协商调整时间',1,1,1,1,'张三','/upload/avatar1.jpg'),
  (2,2,1,'会员卡可以暂停使用吗？','可以的，请到前台办理暂停手续',1,1,1,1,'李四','/upload/avatar2.jpg'),
  (3,3,1,'退款多久能到账？','一般3-5个工作日',1,1,1,0,'王五','/upload/avatar3.jpg'),
  (4,4,1,'有没有团课优惠？','',1,1,0,0,'赵六','/upload/avatar4.jpg');

-- ============================================================
-- 39. 私聊消息（4条）
-- ============================================================
INSERT INTO `chatmessage` (`id`,`uid`,`fid`,`tablename`,`content`,`format`,`isread`) VALUES
  (1,1,1,'coach','你好教练，今天训练安排是什么？',1,1),
  (2,1,1,'coach','深蹲和卧推，9点见',1,1),
  (3,2,1,'coach','教练，我今天膝盖有点不舒服',1,0),
  (4,6,4,'coach','赵教练，下次瑜伽课什么时候？',1,0);

-- ============================================================
-- 40. 好友关系（4条）
-- ============================================================
INSERT INTO `friend` (`id`,`uid`,`fid`,`name`,`picture`,`role`,`tablename`,`alias`,`type`,`userid`) VALUES
  (1,1,1,'李教练','/upload/avatar6.jpg','教练','coach','我的力量教练',1,1),
  (2,2,1,'李教练','/upload/avatar6.jpg','教练','coach','减脂教练',1,2),
  (3,3,2,'王教练','/upload/avatar7.jpg','教练','coach','恢复教练',1,3),
  (4,6,4,'赵教练','/upload/avatar9.jpg','教练','coach','瑜伽教练',1,6);

-- ============================================================
-- 41. 用户互动（收藏/点赞，8条）
-- ============================================================
INSERT INTO `social` (`id`,`userid`,`refid`,`tablename`,`name`,`picture`,`type`) VALUES
  (1,1,1,'course','力量基础入门','/upload/course1.jpg','1'),
  (2,1,1,'course','力量基础入门','/upload/course1.jpg','21'),
  (3,2,2,'course','燃脂有氧操','/upload/course2.jpg','1'),
  (4,2,4,'course','流瑜伽入门','/upload/course4.jpg','1'),
  (5,3,3,'share','三个月减脂20斤心得','/upload/share3.jpg','1'),
  (6,4,1,'product','可调节哑铃套装','/upload/product1.jpg','1'),
  (7,6,4,'course','流瑜伽入门','/upload/course4.jpg','21'),
  (8,7,5,'course','拳击健身课','/upload/course5.jpg','1');

SET FOREIGN_KEY_CHECKS=1;
