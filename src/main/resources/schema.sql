-- 创建分级阅读推荐系统的数据库
CREATE DATABASE IF NOT EXISTS grrs CHARACTER SET utf8mb4;

-- 创建用户表
CREATE TABLE IF NOT EXISTS user(
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '用户自增ID',
  creator VARCHAR(20) NOT NULL COMMENT '创建者',
  modifier VARCHAR(20) NOT NULL COMMENT '修改者',
  create_time DATETIME NOT NULL DEFAULT now() COMMENT '创建时间',
  modify_time DATETIME NOT NULL DEFAULT now() COMMENT '修改时间',
  deleted BIT NOT NULL DEFAULT false COMMENT '删除标志：0-未删除，1-已删除',
  user_name VARCHAR(20) NOT NULL COMMENT '用户名',
  user_pswd VARCHAR(64) NOT NULL COMMENT '用户密码',
  PRIMARY KEY (id) COMMENT 'ID主键',
  UNIQUE uname_unique_index (user_name) COMMENT '用户名的唯一索引'
) ENGINE = InnoDB;

-- 创建书籍表
CREATE TABLE IF NOT EXISTS book(
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '用户自增ID',
  creator VARCHAR(20) NOT NULL COMMENT '创建者',
  modifier VARCHAR(20) NOT NULL COMMENT '修改者',
  create_time DATETIME NOT NULL DEFAULT now() COMMENT '创建时间',
  modify_time DATETIME NOT NULL DEFAULT now() COMMENT '修改时间',
  deleted BIT NOT NULL DEFAULT false COMMENT '删除标志：0-未删除，1-已删除',
  book_name VARCHAR(128) NOT NULL COMMENT '书籍名',
  authors VARCHAR(255) NOT NULL  COMMENT '作者列表',
  isbns VARCHAR(255) DEFAULT NULL COMMENT '书籍ISBN列表',
  cover_url VARCHAR(255) DEFAULT NULL COMMENT '书籍封面url',
  summary TEXT DEFAULT NULL COMMENT '书籍总结',
  topics TEXT DEFAULT NULL COMMENT '主题标签列表',
  series TEXT DEFAULT NULL COMMENT '系列标签列表',
  is_fiction BIT DEFAULT NULL COMMENT '是否小说：0-否，1-是',
  ar_bl DECIMAL(3,1) DEFAULT NULL COMMENT 'AR ATOS书籍阅读等级',
  ar_il ENUM('LG','MG','MG+','UG') DEFAULT NULL COMMENT 'AR阅读兴趣年龄段',
  ar_points DECIMAL(3,1) DEFAULT NULL COMMENT 'AR QUIZ达标绩点',
  ar_rating DECIMAL(2,1) DEFAULT NULL COMMENT 'AR用户评分',
  lexile_prefix ENUM('AD', 'NC', 'HL', 'IG', 'GN', 'BR', 'NP') DEFAULT NULL COMMENT 'Lexile分级前缀',
  lexile INT DEFAULT NULL COMMENT 'Lexile分级数值',
  wordcount INT DEFAULT NULL COMMENT '书籍词数',
  pagecount INT DEFAULT NULL COMMENT '书籍页数',
  amazon_rating DECIMAL(2,1) DEFAULT NULL COMMENT '亚马逊用户评分',
  PRIMARY KEY (id) COMMENT 'ID主键'
) ENGINE = InnoDB;


-- SELECT *, CONCAT(dist*100/sum, '%') AS radio FROM (SELECT COUNT(DISTINCT creator) AS sum, COUNT(DISTINCT LEFT(creator, 5)) AS dist FROM user) AS src;
-- SELECT *, CONCAT(dist*100/sum, '%') AS radio FROM (SELECT COUNT(DISTINCT book_name) AS sum, COUNT(DISTINCT LEFT(book_name, 5)) AS dist FROM book) AS src;
-- SELECT *, CONCAT(dist*100/sum, '%') AS radio FROM (SELECT COUNT(DISTINCT authors) AS sum, COUNT(DISTINCT LEFT(authors, 8)) AS dist FROM book) AS src;
-- ALTER TABLE user DROP INDEX creator_prefix_index;
-- ALTER TABLE book DROP INDEX bookname_prefix_index;
ALTER TABLE user ADD INDEX creator_prefix_index (creator(5)) COMMENT '创建者前缀索引';
ALTER TABLE book ADD INDEX bookname_prefix_index (book_name(5)) COMMENT '书名前缀索引';


-- 配置全文索引结束词
USE grrs;
CREATE TABLE empty_stopword(value VARCHAR(30)) ENGINE = InnoDB;
SET GLOBAL innodb_ft_server_stopword_table = 'grrs/empty_stopword';
ALTER TABLE book ADD FULLTEXT bookname_authors_fulltext (book_name, authors) COMMENT '书名索引全文索引';