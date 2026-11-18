# 有为书院 · 智慧阅读与学习平台

## 项目简介

“有为书院”是一套校园阅读与学习平台，包含用户前台、教师/管理工作台、Spring Boot 后端和 MySQL 数据库。前端采用响应式布局和统一视觉规范，后端提供完整的增删改查、分页、条件查询、登录注册、个人资料与业务联动能力。

## 本次新增功能

### 1. 书院地图：百度地图搜索与路线规划

- 前台新增“书院地图”导航和 `/front/map` 页面。
- 支持展示书院空间、关键词地点搜索、起点/终点选择、驾车路线和步行路线。
- 后端新增 `space` 表及 `/space` 接口，预置 4 个书院地点。
- 百度地图浏览器端 AK 不随源码提供，需要在环境变量中配置后重新构建：

```env
# vue/.env.development
VITE_BAIDU_MAP_AK='你的百度地图浏览器端AK'

# vue/.env.production
VITE_BAIDU_MAP_AK='你的百度地图浏览器端AK'
```

获取方式：登录百度地图开放平台，创建“浏览器端”应用并将当前访问域名加入白名单。未配置 AK 时页面会显示配置提示，地点列表和路线选择界面仍可正常查看。

### 2. 作业发布、提交与批改

- 教师/管理端 `/manager/homework`：发布、编辑、删除作业，设置截止时间和附件。
- 学生前台 `/front/homework`：查看作业、在线填写说明、上传附件并提交。
- 教师/管理端 `/manager/submit`：查看学生提交、打分并填写评语。
- 后端表：`homework`、`submit`。
- 业务约束：同一学生对同一作业只能提交一次；提交后教师可进行批改。

### 3. 消息通知中心

- 导航栏新增消息铃铛与未读数。
- 学生前台 `/front/message`，教师/管理端 `/manager/message`。
- 支持消息列表、未读筛选、单条已读、全部已读、删除。
- 已与业务联动：
  - 教师发布作业 → 所有学生收到学习任务通知；
  - 学生提交作业 → 对应教师收到提交提醒；
  - 教师完成批改 → 学生收到成绩与评语通知；
  - 学生借阅图书 → 学生收到借阅通知。
- 后端表：`message`，接口前缀 `/message`。

## 技术栈

- 后端：Spring Boot 3.3、MyBatis、PageHelper、MySQL 8、Lombok
- 前端：Vue 3、Vite、Element Plus、ECharts、Axios
- 地图：百度地图 JavaScript API GL
- 数据库：MySQL 8，可在 Navicat 中直接查看和管理
- 部署：Spring Boot 单 Jar 同源提供前端静态页面；Cloudflare Tunnel 提供临时公网访问

## 测试账号

| 身份 | 账号 | 密码 |
|---|---|---|
| 管理员 | admin | admin |
| 教师 | teacher | 123456 |
| 学生 | user | 123456 |
| 学生 | ill | 123456 |

## 数据库

完整初始化脚本：

```text
sql/有为书院_数据库初始化.sql
```

脚本包含 `admin`、`teacher`、`user`、`book`、`chapter`、`borrow`、`article`、`comments`、`praise`、`collect`、`homework`、`submit`、`space`、`message` 等表及演示数据，可直接在 Navicat 中运行。重复执行使用 `IF NOT EXISTS` 和 `ON DUPLICATE KEY UPDATE`，不会重复插入主键数据。

数据库连接配置位于：

```text
springboot/src/main/resources/application.yml
```

当前本地默认配置：`root / root`，数据库 `free_system`，端口 `3306`。

## 本地启动

### 1. 初始化数据库

在 Navicat 中打开并执行 `sql/有为书院_数据库初始化.sql`。

### 2. 启动后端

在 IDEA 中运行 `SpringBootApplication`，端口 `9090`。

### 3. 前端开发模式

```powershell
cd vue
npm install
npm run dev
```

开发地址：`http://localhost:5173`

### 4. 单包运行

```powershell
cd vue
npm run build
# 将 dist 内容同步到 springboot/src/main/resources/static 后重新打包
mvn -DskipTests package
java -jar springboot/target/springboot-0.0.1-SNAPSHOT.jar
```

访问：`http://localhost:9090`

## 主要接口

```text
GET    /space/selectAll
GET    /homework/selectAll
GET    /homework/selectPage
POST   /homework/add
PUT    /homework/update
DELETE /homework/delete/{id}
POST   /submit/add
PUT    /submit/grade
GET    /submit/selectAll
GET    /message/selectAll
GET    /message/unreadCount
PUT    /message/read/{id}
PUT    /message/readAll
DELETE /message/delete/{id}
```

消息查询参数使用 `userId` 和 `role`，例如：

```text
GET /message/selectAll?userId=2&role=USER
GET /message/unreadCount?userId=2&role=USER
```

## 一键启动并生成公网地址

双击项目根目录的 `一键启动并上网.bat`。脚本会检查后端、启动 Spring Boot 和 Cloudflare 临时隧道，并在窗口中显示公网地址。

> Cloudflare 免费 `trycloudflare.com` 地址是临时隧道，电脑关机、休眠、断网或进程退出后会失效，不能从技术上承诺永久 24 小时在线。需要长期稳定在线时，应部署到云服务器或配置 Cloudflare Named Tunnel。

## 目录说明

```text
free_system/
├── springboot/                 Spring Boot 后端
├── vue/                        Vue 前端
├── sql/                        数据库初始化脚本
├── files/                      上传文件
├── tools/cloudflared.exe       公网隧道工具
├── 一键启动并上网.bat
└── 停止公网服务.bat
```