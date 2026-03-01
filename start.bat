@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

:: ============================================================
::  健身房管理系统 —— Windows 一键启动脚本
::  启动顺序：后端 → 管理端 → 用户端
::  使用方式：双击 start.bat 或在 gym_vclqwy4 目录下执行
::  停止方式：关闭各个弹出的命令行窗口
:: ============================================================

set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

echo.
echo ========================================
echo    健身房管理系统 · 一键启动
echo ========================================
echo.

:: ---------- 环境检查 ----------
echo [INFO]  正在检查运行环境...

:: Java
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] 未检测到 Java，请安装 JDK 17+ 后重试
    pause
    exit /b 1
)
for /f "tokens=*" %%i in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    echo [INFO]  Java: %%i
    goto :java_done
)
:java_done

:: Maven
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] 未检测到 Maven，请安装 Maven 3.6.3+ 后重试
    pause
    exit /b 1
)
for /f "tokens=*" %%i in ('mvn -version 2^>^&1 ^| findstr /i "Apache Maven"') do (
    echo [INFO]  Maven: %%i
    goto :mvn_done
)
:mvn_done

:: Node
where node >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] 未检测到 Node.js，请安装 Node 18+ 后重试
    pause
    exit /b 1
)
for /f "tokens=*" %%i in ('node -v') do echo [INFO]  Node: %%i

:: 包管理器
set "PKG_MGR="
where pnpm >nul 2>nul
if %errorlevel% equ 0 (
    set "PKG_MGR=pnpm"
) else (
    where npm >nul 2>nul
    if %errorlevel% equ 0 (
        set "PKG_MGR=npm"
    ) else (
        echo [ERROR] 未检测到 pnpm 或 npm，请先安装
        pause
        exit /b 1
    )
)
echo [INFO]  包管理器: %PKG_MGR%
echo.

:: ---------- 1. 启动后端 ----------
echo [INFO]  ===== 启动后端服务 (端口 8888) =====
cd /d "%SCRIPT_DIR%backend"

if not exist "target" (
    echo [INFO]  首次运行，正在编译后端（可能需要几分钟）...
    call mvn clean package -DskipTests -q
)

start "健身房-后端(8888)" cmd /k "title 健身房-后端(8888) && mvn spring-boot:run"
echo [INFO]  后端服务已在新窗口启动
echo.

:: 等待后端就绪
echo [INFO]  等待后端服务就绪（约30秒）...
set WAITED=0
:wait_loop
if %WAITED% geq 60 (
    echo [WARN]  等待后端超时，继续启动前端...
    goto :start_admin
)
timeout /t 2 /nobreak >nul
set /a WAITED+=2
curl -s http://localhost:8888/gym_vclqwy4 >nul 2>nul
if %errorlevel% neq 0 goto :wait_loop
echo [INFO]  后端服务已就绪！
echo.

:: ---------- 2. 启动管理端 ----------
:start_admin
echo [INFO]  ===== 启动管理端前端 (端口 3001) =====
cd /d "%SCRIPT_DIR%admin"

if not exist "node_modules" (
    echo [INFO]  首次运行，正在安装管理端依赖...
    call %PKG_MGR% install
)

start "健身房-管理端(3001)" cmd /k "title 健身房-管理端(3001) && %PKG_MGR% run dev"
echo [INFO]  管理端已在新窗口启动
echo.

:: ---------- 3. 启动用户端 ----------
echo [INFO]  ===== 启动用户端前端 (端口 3002) =====
cd /d "%SCRIPT_DIR%front"

if not exist "node_modules" (
    echo [INFO]  首次运行，正在安装用户端依赖...
    call %PKG_MGR% install
)

start "健身房-用户端(3002)" cmd /k "title 健身房-用户端(3002) && %PKG_MGR% run dev"
echo [INFO]  用户端已在新窗口启动
echo.

:: ---------- 启动完成 ----------
echo ========================================
echo   所有服务启动完成！
echo.
echo   后端 API:  http://localhost:8888/gym_vclqwy4
echo   管理端:    http://localhost:3001
echo   用户端:    http://localhost:3002
echo.
echo   关闭各窗口即可停止对应服务
echo ========================================
echo.
pause
