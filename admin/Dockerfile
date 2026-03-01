# ============================================================
#  健身房管理系统 — 管理端 Dockerfile (多阶段构建)
# ============================================================

# ---------- Stage 1: 构建 ----------
FROM node:18-alpine AS builder
WORKDIR /build

COPY package.json package-lock.json ./
RUN npm ci

COPY . .

# 构建时注入生产环境 API 地址
ARG VITE_API_BASE_URL=http://localhost:8888/gym_vclqwy4
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}

RUN npm run build

# ---------- Stage 2: Nginx 提供静态文件 ----------
FROM nginx:alpine
COPY --from=builder /build/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
