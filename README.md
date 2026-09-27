# StudentID_calculator_backend
前后端分离计算器 - 后端 SpringBoot 项目

## 项目介绍
本项目是前后端分离计算器后端，提供表达式计算、历史记录增删查接口，数据持久化到MySQL。
支持四则运算、括号复合表达式、负数、小数，处理除零、非法表达式异常。

## 技术栈
- SpringBoot 2.7.x
- MyBatis-Plus
- MySQL 8.0
- Java 11

## 环境要求
- JDK 11
- MySQL 8.0
- Maven

## 数据库初始化
```sql
CREATE DATABASE calculator_db;
USE calculator_db;
CREATE TABLE calculation_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    expression VARCHAR(512) NOT NULL COMMENT '表达式',
    result DECIMAL(20,6) NOT NULL COMMENT '计算结果',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);