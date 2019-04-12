-- 创建分级阅读推荐系统的数据库
CREATE DATABASE IF NOT EXISTS grrs CHARACTER SET utf8mb4;

-- 创建用户表
CREATE TABLE IF NOT EXISTS grrs.user(
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '用户自增ID',
  creator VARCHAR(20) NOT NULL COMMENT '创建者',
  modifier VARCHAR(20) NOT NULL COMMENT '修改者',
  create_time DATETIME NOT NULL DEFAULT now() COMMENT '创建时间',
  modify_time DATETIME NOT NULL DEFAULT now() COMMENT '修改时间',
  deleted BIT NOT NULL DEFAULT false COMMENT '删除标志：0-未删除，1-已删除',
  user_name VARCHAR(20) NOT NULL COMMENT '用户名',
  user_pswd VARCHAR(256) NOT NULL COMMENT '用户密码',
  PRIMARY KEY (id) COMMENT 'ID主键',
  UNIQUE user_name_unique_index (user_name) COMMENT '用户名的唯一索引'
) ENGINE = InnoDB;

-- 创建书籍表
CREATE TABLE IF NOT EXISTS grrs.book(
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '用户自增ID',
  creator VARCHAR(20) NOT NULL COMMENT '创建者',
  modifier VARCHAR(20) NOT NULL COMMENT '修改者',
  create_time DATETIME NOT NULL DEFAULT now() COMMENT '创建时间',
  modify_time DATETIME NOT NULL DEFAULT now() COMMENT '修改时间',
  deleted BIT NOT NULL DEFAULT false COMMENT '删除标志：0-未删除，1-已删除',
  book_name VARCHAR(128) NOT NULL COMMENT '书籍名',
  authors VARCHAR(256) NOT NULL  COMMENT '作者列表',
  isbns VARCHAR(256) DEFAULT NULL COMMENT '书籍ISBN列表',
  cover_url VARCHAR(256) DEFAULT NULL COMMENT '书籍封面url',
  summary VARCHAR(1024) DEFAULT NULL COMMENT '书籍总结',
  topics VARCHAR(1024) DEFAULT NULL COMMENT '主题标签列表',
  series VARCHAR(1024) DEFAULT NULL COMMENT '系列标签列表',
  is_fiction BIT DEFAULT NULL COMMENT '是否小说：0-否，1-是',
  ar_bl DECIMAL(2,1) DEFAULT NULL COMMENT 'AR ATOS书籍阅读等级',
  ar_il ENUM('LG(K-3)','MG(4-8)','MG+(6 AND UP)','UG(9-12)') DEFAULT NULL COMMENT 'AR阅读兴趣年龄段',
  ar_points DECIMAL(2,1) DEFAULT NULL COMMENT 'AR QUIZ达标绩点',
  ar_rating DECIMAL(1,1) DEFAULT NULL COMMENT 'AR用户评分',
  lexile_prefix ENUM('AD', 'NC', 'HL', 'IG', 'GN', 'BR', 'NP') DEFAULT NULL COMMENT 'Lexile分级前缀',
  lexile INT DEFAULT NULL COMMENT 'Lexile分级数值',
  wordcount INT DEFAULT NULL COMMENT '书籍词数',
  pagecount INT DEFAULT NULL COMMENT '书籍页数',
  amazon_rating DECIMAL(1,1) DEFAULT NULL COMMENT '亚马逊用户评分',
  PRIMARY KEY (id) COMMENT 'ID主键',
  INDEX book_name_prefix_index (book_name(10)) COMMENT '书名前缀索引',
  INDEX authors_prefix_index (authors(10)) COMMENT '作者列表前缀索引'
) ENGINE = InnoDB;