-- 退款审核状态字段迁移
ALTER TABLE refundcourseenrollment ADD COLUMN auditstatus VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态';
ALTER TABLE refundproductorder ADD COLUMN auditstatus VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态';
ALTER TABLE refundcardapplication ADD COLUMN auditstatus VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态';
ALTER TABLE refundcardrenewal ADD COLUMN auditstatus VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态';
