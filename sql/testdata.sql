SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE `gym_vclqwy4`;

-- 第一批：分类表
INSERT INTO `coursetype` (`coursetypename`) VALUES
('有氧健身'),
('力量训练'),
('瑜伽'),
('舞蹈'),
('格斗');

INSERT INTO `producttype` (`producttypename`) VALUES
('补剂'),
('服装'),
('器材'),
('饮料'),
('配件');

INSERT INTO `sharetype` (`sharetypename`) VALUES
('健身心得'),
('减脂经验'),
('增肌分享'),
('器械使用'),
('营养搭配');

INSERT INTO `newstype` (`typename`) VALUES
('公告'),
('活动'),
('新闻'),
('健康'),
('优惠');

-- 第二批：角色表
INSERT INTO `user` (`useraccount`, `userpassword`, `name`, `sex`, `age`, `phone`, `image`, `money`, `cardno`, `memberlevel`, `expiretime`, `remainingcourses`) VALUES
('用户账号1', '123456', '王明', '男', 25, '13800138001', '/upload/avatar1.jpg', 500.0, 'MB001', '普通会员', '2027-11-13 00:00:00', 10),
('用户账号2', '123456', '李华', '女', 30, '13800138002', '/upload/avatar2.jpg', 300.0, 'MB002', '银卡会员', '2026-11-13 00:00:00', 15),
('用户账号3', '123456', '张强', '男', 28, '13800138003', '/upload/avatar3.jpg', 400.0, 'MB003', '金卡会员', '2027-05-13 00:00:00', 20),
('用户账号4', '123456', '刘芳', '女', 35, '13800138004', '/upload/avatar4.jpg', 200.0, 'MB004', '钻石会员', '2026-11-13 00:00:00', 25),
('用户账号5', '123456', '陈伟', '男', 22, '13800138005', '/upload/avatar5.jpg', 600.0, 'MB005', '普通会员', '2027-11-13 00:00:00', 5);

INSERT INTO `coach` (`coachaccount`, `coachpassword`, `coachname`, `coachimage`, `sex`, `phone`, `jobno`, `specialty`, `coachlevel`, `workyears`) VALUES
('教练账号1', '123456', '杨教练', '/upload/avatar6.jpg', '男', '13900139001', 'JC001', '力量训练', '初级', 2),
('教练账号2', '123456', '赵教练', '/upload/avatar7.jpg', '女', '13900139002', 'JC002', '瑜伽', '中级', 4),
('教练账号3', '123456', '孙教练', '/upload/avatar8.jpg', '男', '13900139003', 'JC003', '有氧健身', '高级', 6),
('教练账号4', '123456', '周教练', '/upload/avatar9.jpg', '女', '13900139004', 'JC004', '舞蹈', '资深', 8),
('教练账号5', '123456', '吴教练', '/upload/avatar10.jpg', '男', '13900139005', 'JC005', '格斗', '中级', 3);

-- 第三批：其他业务表
INSERT INTO `course` (`coursename`, `courseimage`, `coursetype`, `classtime`, `duration`, `coachname`, `coachaccount`, `quota`, `coursedesc`, `auditstatus`, `auditreply`) VALUES
('有氧入门', '/upload/course1.jpg', '有氧健身', '2025-11-20 09:00:00', 60, '杨教练', '教练账号1', 20, '适合初学者的入门课程', '通过', ''),
('力量增肌', '/upload/course2.jpg', '力量训练', '2025-11-21 14:00:00', 90, '赵教练', '教练账号2', 15, '提升肌肉力量', '通过', ''),
('瑜伽放松', '/upload/course3.jpg', '瑜伽', '2025-11-22 18:00:00', 120, '孙教练', '教练账号3', 10, '缓解压力', '通过', ''),
('舞动活力', '/upload/course4.jpg', '舞蹈', '2025-11-23 19:00:00', 75, '周教练', '教练账号4', 8, '快乐舞蹈', '待审核', ''),
('格斗技巧', '/upload/course5.jpg', '格斗', '2025-11-24 10:00:00', 100, '吴教练', '教练账号5', 12, '自卫术基础', '通过', '');

INSERT INTO `courseenrollment` (`coursename`, `courseimage`, `coursetype`, `classtime`, `duration`, `coachname`, `userid`, `useraccount`, `coachaccount`, `courseprice`, `quantity`, `totalprice`, `ispay`, `orderstatus`) VALUES
('有氧入门', '/upload/course1.jpg', '有氧健身', '2025-11-20 09:00:00', 60, '杨教练', 1, '用户账号1', '教练账号1', 50.0, 1, 50.0, '已支付', '已完成'),
('力量增肌', '/upload/course2.jpg', '力量训练', '2025-11-21 14:00:00', 90, '赵教练', 2, '用户账号2', '教练账号2', 80.0, 1, 80.0, '已支付', '已发货'),
('瑜伽放松', '/upload/course3.jpg', '瑜伽', '2025-11-22 18:00:00', 120, '孙教练', 3, '用户账号3', '教练账号3', 60.0, 1, 60.0, '未支付', '未支付'),
('舞动活力', '/upload/course4.jpg', '舞蹈', '2025-11-23 19:00:00', 75, '周教练', 4, '用户账号4', '教练账号4', 70.0, 1, 70.0, '已支付', '已支付'),
('格斗技巧', '/upload/course5.jpg', '格斗', '2025-11-24 10:00:00', 100, '吴教练', 5, '用户账号5', '教练账号5', 90.0, 1, 90.0, '已支付', '已取消');

INSERT INTO `product` (`productname`, `productimage`, `producttype`, `productprice`, `stock`, `productdesc`, `auditstatus`, `auditreply`) VALUES
('蛋白质粉', '/upload/product1.jpg', '补剂', 200.0, 50, '增强肌肉', '通过', ''),
('健身T恤', '/upload/product2.jpg', '服装', 150.0, 30, '舒适透气', '通过', ''),
('哑铃', '/upload/product3.jpg', '器材', 300.0, 20, '家用健身', '通过', ''),
('能量饮料', '/upload/product4.jpg', '饮料', 80.0, 100, '补充能量', '待审核', ''),
('腕带', '/upload/product5.jpg', '配件', 50.0, 60, '防水材料', '通过', '');

INSERT INTO `productorder` (`productname`, `productimage`, `producttype`, `productprice`, `quantity`, `totalprice`, `userid`, `ispay`, `orderstatus`) VALUES
('蛋白质粉', '/upload/product1.jpg', '补剂', 200.0, 2, 400.0, 1, '已支付', '已完成'),
('健身T恤', '/upload/product2.jpg', '服装', 150.0, 1, 150.0, 2, '已支付', '已发货'),
('哑铃', '/upload/product3.jpg', '器材', 300.0, 1, 300.0, 3, '未支付', '未支付'),
('能量饮料', '/upload/product4.jpg', '饮料', 80.0, 3, 240.0, 4, '已支付', '已支付'),
('腕带', '/upload/product5.jpg', '配件', 50.0, 2, 100.0, 5, '已支付', '已取消');

INSERT INTO `membershippackage` (`packagename`, `packageimage`, `packagetype`, `packageprice`, `validdays`, `includedcourses`, `packagedesc`) VALUES
('基础月卡', '/upload/package1.jpg', '月卡', 100.0, 30, 10, '每月基础套餐'),
('升级季卡', '/upload/package2.jpg', '季卡', 280.0, 90, 30, '季度优惠'),
('年度钻石', '/upload/package3.jpg', '年卡', 1000.0, 365, 100, '全年畅享'),
('家庭月卡', '/upload/package4.jpg', '月卡', 150.0, 30, 15, '适合家庭'),
('学生季卡', '/upload/package5.jpg', '季卡', 220.0, 90, 25, '学生折扣');

INSERT INTO `cardapplication` (`packagename`, `packageimage`, `packagetype`, `packageprice`, `validdays`, `includedcourses`, `quantity`, `userid`, `ispay`, `orderstatus`) VALUES
('基础月卡', '/upload/package1.jpg', '月卡', 100.0, 30, 10, 1, 1, '已支付', '已完成'),
('升级季卡', '/upload/package2.jpg', '季卡', 280.0, 90, 30, 1, 2, '已支付', '已发货'),
('年度钻石', '/upload/package3.jpg', '年卡', 1000.0, 365, 100, 1, 3, '未支付', '未支付'),
('家庭月卡', '/upload/package4.jpg', '月卡', 150.0, 30, 15, 1, 4, '已支付', '已支付'),
('学生季卡', '/upload/package5.jpg', '季卡', 220.0, 90, 25, 1, 5, '已支付', '已取消');

INSERT INTO `cardrenewal` (`packagename`, `packagetype`, `packageprice`, `renewaldays`, `userid`, `ispay`, `orderstatus`) VALUES
('基础月卡', '月卡', 100.0, 30, 1, '已支付', '已完成'),
('升级季卡', '季卡', 280.0, 90, 2, '已支付', '已发货'),
('年度钻石', '年卡', 1000.0, 365, 3, '未支付', '未支付'),
('家庭月卡', '月卡', 150.0, 30, 4, '已支付', '已支付'),
('学生季卡', '季卡', 220.0, 90, 5, '已支付', '已取消');

INSERT INTO `share` (`sharetitle`, `sharecontent`, `shareimage`, `sharetype`, `auditstatus`, `auditreply`) VALUES
('健身初体验', '我开始健身了，感觉很好', '/upload/share1.jpg', '健身心得', '通过', ''),
('减脂秘诀', '坚持跑步和控制饮食', '/upload/share2.jpg', '减脂经验', '通过', ''),
('增肌之路', '用蛋白质粉和杠铃', '/upload/share3.jpg', '增肌分享', '通过', ''),
('器械使用指南', '如何正确使用哑铃', '/upload/share4.jpg', '器械使用', '待审核', ''),
('营养推荐', '早餐的重要性', '/upload/share5.jpg', '营养搭配', '通过', '');

INSERT INTO `feedback` (`feedbacktitle`, `feedbackcontent`, `feedbacktype`, `userid`, `auditstatus`, `auditreply`) VALUES
('建议加更多课程', '希望有更多瑜伽课程', '功能建议', 1, '通过', ''),
('设备维护', '椭圆机坏了', '问题反馈', 2, '通过', ''),
('客服回复慢', '投诉客服效率', '投诉', 3, '待审核', ''),
('优惠活动', '希望有更多折扣', '功能建议', 4, '通过', ''),
('卫生问题', '更衣室清洁不够', '问题反馈', 5, '通过', '');

INSERT INTO `cancelcourseenrollment` (`coursename`, `courseimage`, `coursetype`, `classtime`, `duration`, `coachname`, `userid`, `cancelreason`) VALUES
('有氧入门', '/upload/course1.jpg', '有氧健身', '2025-11-20 09:00:00', 60, '杨教练', 5, '时间冲突'),
('力量增肌', '/upload/course2.jpg', '力量训练', '2025-11-21 14:00:00', 90, '赵教练', 4, '身体不适'),
('瑜伽放松', '/upload/course3.jpg', '瑜伽', '2025-11-22 18:00:00', 120, '孙教练', 3, '临时有事'),
('舞动活力', '/upload/course4.jpg', '舞蹈', '2025-11-23 19:00:00', 75, '周教练', 2, '兴趣改变'),
('格斗技巧', '/upload/course5.jpg', '格斗', '2025-11-24 10:00:00', 100, '吴教练', 1, '家庭原因');

INSERT INTO `refundcourseenrollment` (`coursename`, `courseimage`, `coursetype`, `classtime`, `duration`, `coachname`, `userid`, `refundreason`) VALUES
('有氧入门', '/upload/course1.jpg', '有氧健身', '2025-11-20 09:00:00', 60, '杨教练', 5, '课程太难'),
('力量增肌', '/upload/course2.jpg', '力量训练', '2025-11-21 14:00:00', 90, '赵教练', 4, '教练风格不合'),
('瑜伽放松', '/upload/course3.jpg', '瑜伽', '2025-11-22 18:00:00', 120, '孙教练', 3, '价格过高'),
('舞动活力', '/upload/course4.jpg', '舞蹈', '2025-11-23 19:00:00', 75, '周教练', 2, '时间不便'),
('格斗技巧', '/upload/course5.jpg', '格斗', '2025-11-24 10:00:00', 100, '吴教练', 1, '学员少');

INSERT INTO `cancelcardapplication` (`packagename`, `packageimage`, `packagetype`, `packageprice`, `validdays`, `includedcourses`, `userid`, `cancelreason`) VALUES
('基础月卡', '/upload/package1.jpg', '月卡', 100.0, 30, 10, 5, '预算不足'),
('升级季卡', '/upload/package2.jpg', '季卡', 280.0, 90, 30, 4, '计划改变'),
('年度钻石', '/upload/package3.jpg', '年卡', 1000.0, 365, 100, 3, '出国留学'),
('家庭月卡', '/upload/package4.jpg', '月卡', 150.0, 30, 15, 2, '搬家'),
('学生季卡', '/upload/package5.jpg', '季卡', 220.0, 90, 25, 1, '毕业');

INSERT INTO `refundcardapplication` (`packagename`, `packageimage`, `packagetype`, `packageprice`, `validdays`, `includedcourses`, `userid`, `refundreason`) VALUES
('基础月卡', '/upload/package1.jpg', '月卡', 100.0, 30, 10, 5, '服务不满意'),
('升级季卡', '/upload/package2.jpg', '季卡', 280.0, 90, 30, 4, '设施更新'),
('年度钻石', '/upload/package3.jpg', '年卡', 1000.0, 365, 100, 3, '疫情影响'),
('家庭月卡', '/upload/package4.jpg', '月卡', 150.0, 30, 15, 2, '家属反对'),
('学生季卡', '/upload/package5.jpg', '季卡', 220.0, 90, 25, 1, '学校规定');

INSERT INTO `cancelproductorder` (`productname`, `productimage`, `producttype`, `productprice`, `quantity`, `totalprice`, `userid`, `cancelreason`) VALUES
('蛋白质粉', '/upload/product1.jpg', '补剂', 200.0, 2, 400.0, 5, '不喜欢口味'),
('健身T恤', '/upload/product2.jpg', '服装', 150.0, 1, 150.0, 4, '尺寸不合'),
('哑铃', '/upload/product3.jpg', '器材', 300.0, 1, 300.0, 3, '质量问题'),
('能量饮料', '/upload/product4.jpg', '饮料', 80.0, 3, 240.0, 2, '过期'),
('腕带', '/upload/product5.jpg', '配件', 50.0, 2, 100.0, 1, '颜色不佳');

INSERT INTO `refundproductorder` (`productname`, `productimage`, `producttype`, `productprice`, `quantity`, `totalprice`, `userid`, `refundreason`) VALUES
('蛋白质粉', '/upload/product1.jpg', '补剂', 200.0, 2, 400.0, 5, '药物过敏'),
('健身T恤', '/upload/product2.jpg', '服装', 150.0, 1, 150.0, 4, '面料起球'),
('哑铃', '/upload/product3.jpg', '器材', 300.0, 1, 300.0, 3, '损坏'),
('能量饮料', '/upload/product4.jpg', '饮料', 80.0, 3, 240.0, 2, '变质'),
('腕带', '/upload/product5.jpg', '配件', 50.0, 2, 100.0, 1, '不防水');

INSERT INTO `cancelcardrenewal` (`packagename`, `packagetype`, `packageprice`, `renewaldays`, `userid`, `cancelreason`) VALUES
('基础月卡', '月卡', 100.0, 30, 5, '经济压力'),
('升级季卡', '季卡', 280.0, 90, 4, '健身计划暂停'),
('年度钻石', '年卡', 1000.0, 365, 3, '搬离城市'),
('家庭月卡', '月卡', 150.0, 30, 2, '家庭变故'),
('学生季卡', '季卡', 220.0, 90, 1, '学习繁忙');

INSERT INTO `refundcardrenewal` (`packagename`, `packagetype`, `packageprice`, `renewaldays`, `userid`, `refundreason`) VALUES
('基础月卡', '月卡', 100.0, 30, 5, '服务降级'),
('升级季卡', '季卡', 280.0, 90, 4, '教练更换'),
('年度钻石', '年卡', 1000.0, 365, 3, '健康问题'),
('家庭月卡', '月卡', 150.0, 30, 2, '子女不上健身'),
('学生季卡', '季卡', 220.0, 90, 1, '毕业离校');

INSERT INTO `news` (`title`, `introduction`, `typename`, `name`, `avatar`, `image`, `content`, `auditstatus`, `auditreply`) VALUES
('健身房新增课程', '介绍新课程内容', '活动', '管理员', '/upload/avatar10.jpg', '/upload/news1.jpg', '详情内容', '通过', ''),
('健康心得分享', '维持健康的重要性', '健康', '管理员', '/upload/avatar10.jpg', '/upload/news2.jpg', '详情内容', '通过', ''),
('优惠券发放', '会员福利', '优惠', '管理员', '/upload/avatar10.jpg', '/upload/news3.jpg', '详情内容', '通过', ''),
('设备维护通知', '暂时关闭', '公告', '管理员', '/upload/avatar10.jpg', '/upload/news4.jpg', '详情内容', '待审核', ''),
('健身大赛报名', '参加健身比赛', '活动', '管理员', '/upload/avatar10.jpg', '/upload/news5.jpg', '详情内容', '通过', '');