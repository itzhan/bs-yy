# 🐳 Docker 一键部署指南

## 快速开始

```bash
# 1. 进入项目目录
cd gym_vclqwy4

# 2. 一键启动所有服务
docker compose up --build -d

# 3. 查看运行状态
docker compose ps

# 4. 查看日志
docker compose logs -f
```

## 访问地址

| 服务 | 地址 |
|------|------|
| 管理端 | http://localhost:3001 |
| 用户端 | http://localhost:3002 |
| 后端 API | http://localhost:8888/gym_vclqwy4 |
| MySQL | localhost:3306 |

## 自定义端口

修改 `.env.docker` 中的端口配置，然后重启：

```bash
# 使用自定义环境变量
docker compose --env-file .env.docker up --build -d
```

## 常用命令

```bash
# 停止所有服务
docker compose down

# 停止并清除数据（⚠️ 会删除数据库数据）
docker compose down -v

# 重新构建并启动
docker compose up --build -d

# 只重启后端
docker compose restart backend

# 查看某个服务的日志
docker compose logs -f backend
```

## 目录结构

```
gym_vclqwy4/
├── docker-compose.yml    # 编排配置
├── .env.docker           # 环境变量
├── .dockerignore         # Docker 构建排除
├── backend/
│   └── Dockerfile        # 后端镜像（Maven → JRE 17）
├── admin/
│   ├── Dockerfile        # 管理端镜像（Node → Nginx）
│   └── nginx.conf        # Nginx 配置（SPA + API 代理）
├── front/
│   ├── Dockerfile        # 用户端镜像（Node → Nginx）
│   └── nginx.conf        # Nginx 配置（SPA + API 代理）
└── ../sql/               # SQL 初始化脚本（自动导入）
```

## 注意事项

1. 首次启动时 MySQL 会自动执行 `sql/` 目录下的 `.sql` 文件
2. 上传的文件存储在 Docker Volume `upload_data` 中
3. 数据库数据持久化在 Docker Volume `mysql_data` 中
4. 如需修改数据库密码，同步修改 `.env.docker` 中的 `MYSQL_ROOT_PASSWORD`
