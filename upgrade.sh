#!/bin/bash
# ============================================================
#  健身房管理系统 — 一键升级脚本
#  使用方式：在项目根目录执行  bash upgrade.sh
# ============================================================

set -e

echo "========== 1. 停止旧容器 =========="
docker compose down

echo "========== 2. 重新构建并启动所有服务 =========="
docker compose up --build -d

echo "========== 3. 等待 MySQL 就绪 =========="
until docker exec gym-mysql mysqladmin ping -h localhost -uroot -p"ab123168" --silent 2>/dev/null; do
  echo "  等待 MySQL 启动..."
  sleep 3
done
echo "  MySQL 已就绪"

echo "========== 4. 执行数据库迁移 =========="
docker exec -i gym-mysql mysql -uroot -pab123168 --default-character-set=utf8mb4 gym_vclqwy4 <<'SQL'
-- 退款审核状态字段
ALTER TABLE refundcourseenrollment ADD COLUMN auditstatus VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态';
ALTER TABLE refundproductorder ADD COLUMN auditstatus VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态';
ALTER TABLE refundcardapplication ADD COLUMN auditstatus VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态';
ALTER TABLE refundcardrenewal ADD COLUMN auditstatus VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态';
-- 约课报名审核状态字段
ALTER TABLE courseenrollment ADD COLUMN auditstatus VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态';
-- 修复已有数据的审核状态
UPDATE refundcourseenrollment SET auditstatus='待审核' WHERE auditstatus IS NULL OR auditstatus='';
UPDATE refundproductorder SET auditstatus='待审核' WHERE auditstatus IS NULL OR auditstatus='';
UPDATE refundcardapplication SET auditstatus='待审核' WHERE auditstatus IS NULL OR auditstatus='';
UPDATE refundcardrenewal SET auditstatus='待审核' WHERE auditstatus IS NULL OR auditstatus='';
UPDATE courseenrollment SET auditstatus='待审核' WHERE auditstatus IS NULL OR auditstatus='';
SQL

echo ""
echo "========== 升级完成 =========="
echo "  管理端: http://localhost:${ADMIN_PORT:-3001}"
echo "  用户端: http://localhost:${FRONT_PORT:-3002}"
echo "  后  端: http://localhost:${BACKEND_PORT:-8888}"
