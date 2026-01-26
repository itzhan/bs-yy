# gym_vclqwy4

## 要求

- jdk 17+
- maven 3.6.3+
- mysql 5.7+
- node 18.20.8+

## 快速启动
1. sql文件夹下的sql脚本导入数据库
2. 修改`backend/src/main/resources/application.yml`中的数据库账号密码
3. backend目录下运行`mvn clean package -DskipTests`安装依赖
4. 运行`mvn spring-boot:run`启动服务

## 图片识别配置

图片识别，需要在 `backend/src/main/resources/application.yml` 中配置百度图像识别参数：

- `BAIDU_IMAGE_API_KEY`：百度智能云控制台获取
- `BAIDU_IMAGE_SECRET_KEY`：百度智能云控制台获取
- `BAIDU_IMAGE_ENABLED`：设置为 `true` 启用功能

若未配置或未启用，上述特性的前端自动填充将不会触发。
