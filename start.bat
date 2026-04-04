@echo off
echo ========================================
echo   健身房管理系统 · Docker 一键启动
echo ========================================
echo.

echo [INFO] 正在清理旧容器...
docker compose down 2>nul

echo.
echo [INFO] 正在启动所有服务...
docker compose up -d

echo.
echo ========================================
echo   启动完成！
echo.
echo   后端 API:  http://localhost:8888/gym_vclqwy4
echo   管理端:    http://localhost:3001
echo   用户端:    http://localhost:3002
echo ========================================
echo.
pause
