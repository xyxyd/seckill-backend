-- ============================================================
-- 在线秒杀系统 数据库初始化脚本
-- 用法：在 RDS for MySQL 上用 root 账号执行（或通过控制台 SQL 窗口）
-- ============================================================

CREATE DATABASE IF NOT EXISTS seckill
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE seckill;

-- ------------------------------------------------------------
-- 用户表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
  id         BIGINT       NOT NULL AUTO_INCREMENT,
  username   VARCHAR(50)  NOT NULL,
  password   VARCHAR(128) NOT NULL,
  salt       VARCHAR(64)  NOT NULL,
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ------------------------------------------------------------
-- 商品表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product (
  id          BIGINT        NOT NULL AUTO_INCREMENT,
  name        VARCHAR(100)  NOT NULL,
  description VARCHAR(500)  DEFAULT NULL,
  price       DECIMAL(10,2) NOT NULL,
  stock       INT           NOT NULL DEFAULT 0,
  created_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ------------------------------------------------------------
-- 订单表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
  id           BIGINT        NOT NULL AUTO_INCREMENT,
  user_id      BIGINT        NOT NULL,
  product_id   BIGINT        NOT NULL,
  product_name VARCHAR(100)  NOT NULL,
  price        DECIMAL(10,2) NOT NULL,
  status       VARCHAR(20)   NOT NULL DEFAULT 'SUCCESS',
  created_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_user (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ------------------------------------------------------------
-- 初始商品数据
-- ------------------------------------------------------------
INSERT INTO product (name, description, price, stock) VALUES
('小米 14 Ultra',    '骁龙 8 Gen3 · 2K 屏 · 一英寸主摄',      5999.00, 20),
('华为 Mate 60 Pro', '麒麟芯片 · 卫星通话 · 昆仑玻璃',        6999.00, 15),
('iPhone 15 Pro',    'A17 Pro · 钛金属边框 · 灵动岛',         7999.00, 10),
('联想拯救者 Y9000P', 'i9-14900HX · RTX4060 · 2.5K 240Hz',    8999.00, 8),
('索尼 WH-1000XM5',  '头戴式降噪耳机 · 30 小时续航',           2399.00, 30),
('AirPods Pro 2',    '主动降噪 · 自适应通透模式',               1899.00, 50);

-- ------------------------------------------------------------
-- 业务账号（可选）
-- RDS 上如无权限创建用户，请到控制台「账号管理」里手动创建并授权
-- ------------------------------------------------------------
-- CREATE USER 'seckill'@'%' IDENTIFIED BY 'Seckill@123';
-- GRANT ALL PRIVILEGES ON seckill.* TO 'seckill'@'%';
-- FLUSH PRIVILEGES;
