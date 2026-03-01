#!/bin/bash
# ============================================================
#  健身房管理系统 —— Mac / Linux 一键启动脚本
#  启动顺序：后端 → 管理端 → 用户端
#  使用方式：在 gym_vclqwy4 目录下执行  ./start.sh
#  停止方式：按 Ctrl+C 即可停止全部服务
# ============================================================

set -e

# ---------- 颜色定义 ----------
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # 无颜色

# ---------- 工具函数 ----------
info()  { echo -e "${GREEN}[INFO]${NC}  $1"; }
warn()  { echo -e "${YELLOW}[WARN]${NC}  $1"; }
error() { echo -e "${RED}[ERROR]${NC} $1"; }

# 记录子进程 PID，退出时统一清理
PIDS=()
cleanup() {
    echo ""
    warn "正在停止所有服务..."
    for pid in "${PIDS[@]}"; do
        if kill -0 "$pid" 2>/dev/null; then
            kill "$pid" 2>/dev/null
            wait "$pid" 2>/dev/null || true
        fi
    done
    info "所有服务已停止，再见！"
    exit 0
}
trap cleanup SIGINT SIGTERM

# ---------- 项目根目录 ----------
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo ""
echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN}   健身房管理系统 · 一键启动          ${NC}"
echo -e "${CYAN}========================================${NC}"
echo ""

# ---------- 环境检查 ----------
info "正在检查运行环境..."

# Java
if ! command -v java &>/dev/null; then
    error "未检测到 Java，请安装 JDK 17+ 后重试"
    exit 1
fi
JAVA_VER=$(java -version 2>&1 | head -n1)
info "Java: $JAVA_VER"

# Maven
if ! command -v mvn &>/dev/null; then
    error "未检测到 Maven，请安装 Maven 3.6.3+ 后重试"
    exit 1
fi
MVN_VER=$(mvn -version 2>&1 | head -n1)
info "Maven: $MVN_VER"

# Node
if ! command -v node &>/dev/null; then
    error "未检测到 Node.js，请安装 Node 18+ 后重试"
    exit 1
fi
NODE_VER=$(node -v)
info "Node: $NODE_VER"

# 包管理器：优先 pnpm，其次 npm
if command -v pnpm &>/dev/null; then
    PKG_MGR="pnpm"
elif command -v npm &>/dev/null; then
    PKG_MGR="npm"
else
    error "未检测到 pnpm 或 npm，请先安装"
    exit 1
fi
info "包管理器: $PKG_MGR"

echo ""

# ---------- 1. 启动后端 ----------
info "===== 启动后端服务 (端口 8888) ====="
cd "$SCRIPT_DIR/backend"

if [ ! -d "target" ]; then
    info "首次运行，正在编译后端（可能需要几分钟）..."
    mvn clean package -DskipTests -q
fi

mvn spring-boot:run -q &
PIDS+=($!)
info "后端服务已在后台启动 (PID: ${PIDS[${#PIDS[@]}-1]})"
echo ""

# 等待后端就绪
info "等待后端服务就绪..."
MAX_WAIT=60
WAITED=0
while ! curl -s http://localhost:8888/gym_vclqwy4 >/dev/null 2>&1; do
    sleep 2
    WAITED=$((WAITED + 2))
    if [ $WAITED -ge $MAX_WAIT ]; then
        warn "等待后端超时（${MAX_WAIT}s），继续启动前端..."
        break
    fi
done
if [ $WAITED -lt $MAX_WAIT ]; then
    info "后端服务已就绪！"
fi
echo ""

# ---------- 2. 启动管理端 ----------
info "===== 启动管理端前端 (端口 3001) ====="
cd "$SCRIPT_DIR/admin"

if [ ! -d "node_modules" ]; then
    info "首次运行，正在安装管理端依赖..."
    $PKG_MGR install
fi

$PKG_MGR run dev &
PIDS+=($!)
info "管理端已在后台启动 (PID: ${PIDS[${#PIDS[@]}-1]})"
echo ""

# ---------- 3. 启动用户端 ----------
info "===== 启动用户端前端 (端口 3002) ====="
cd "$SCRIPT_DIR/front"

if [ ! -d "node_modules" ]; then
    info "首次运行，正在安装用户端依赖..."
    $PKG_MGR install
fi

$PKG_MGR run dev &
PIDS+=($!)
info "用户端已在后台启动 (PID: ${PIDS[${#PIDS[@]}-1]})"
echo ""

# ---------- 启动完成 ----------
echo -e "${CYAN}========================================${NC}"
echo -e "${GREEN}  所有服务启动完成！${NC}"
echo ""
echo -e "  后端 API:  ${CYAN}http://localhost:8888/gym_vclqwy4${NC}"
echo -e "  管理端:    ${CYAN}http://localhost:3001${NC}"
echo -e "  用户端:    ${CYAN}http://localhost:3002${NC}"
echo ""
echo -e "  按 ${YELLOW}Ctrl+C${NC} 停止所有服务"
echo -e "${CYAN}========================================${NC}"
echo ""

# 保持脚本运行，等待所有子进程
wait
