# seckill-backend（后端）

在线秒杀系统后端，Spring Boot 3.2 + JDK 17 + Spring JDBC + MySQL。

## 技术要点

- 统一返回结构 `{code, msg, data}`，`code=0` 为成功
- 密码加盐 SHA-256 存储，登录后返回 token（`X-Token` 头携带）
- 秒杀防超卖：`update product set stock = stock - 1 where id = ? and stock > 0`
- 每人每商品限购 1 件，库存扣减与订单写入在同一事务

## 接口

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/health` | 健康检查（含数据库连通性） |
| POST | `/api/auth/register` | 注册 `{username, password}` |
| POST | `/api/auth/login` | 登录，返回 `{token, username}` |
| GET | `/api/products` | 商品列表 |
| GET | `/api/products/{id}` | 商品详情 |
| POST | `/api/seckill/{productId}` | 秒杀下单（需 `X-Token`） |
| GET | `/api/orders` | 我的订单（需 `X-Token`） |

## 数据库初始化

在 RDS for MySQL 上用 root 账号执行 `db/init.sql`（建库、建表、插入 6 个商品）。

## 构建与运行

```bash
# 打包（需要 JDK 17 + Maven）
mvn clean package -DskipTests
# 产物：target/seckill-backend.jar

# 运行（数据库地址可用环境变量覆盖）
java -jar target/seckill-backend.jar

# 指定数据库
DB_HOST=192.168.0.100 DB_USER=seckill DB_PASSWORD=Seckill@123 \
  java -jar target/seckill-backend.jar
```

默认配置见 `src/main/resources/application.yml`：

| 环境变量 | 默认值 |
|---|---|
| `DB_HOST` | 192.168.0.100 |
| `DB_PORT` | 3306 |
| `DB_NAME` | seckill |
| `DB_USER` | seckill |
| `DB_PASSWORD` | Seckill@123 |

## 目录结构

```
├── pom.xml
├── db/init.sql
└── src/main
    ├── java/com/seckill
    │   ├── SeckillApplication.java
    │   ├── common/      # 统一响应、异常、全局处理
    │   ├── config/      # 跨域配置
    │   ├── controller/  # 认证 / 商品 / 订单 / 健康检查
    │   ├── model/       # DTO 与记录类型
    │   ├── service/     # 业务逻辑
    │   └── util/        # 密码、token
    └── resources/application.yml
```
