# ============================================================
#  健身房管理系统 — 后端 Dockerfile (多阶段构建)
# ============================================================

# ---------- Stage 1: 构建 ----------
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build

# 先复制 pom.xml 利用 Docker 缓存
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源码并打包
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- Stage 2: 运行 ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# 创建上传目录
RUN mkdir -p /app/upload

COPY --from=builder /build/target/backend.jar ./backend.jar

# 暴露端口
EXPOSE 8888

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD curl -f http://localhost:8888/gym_vclqwy4 || exit 1

ENTRYPOINT ["java", "-jar", "backend.jar"]
