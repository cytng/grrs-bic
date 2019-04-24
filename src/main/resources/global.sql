USE grrs;
CREATE TABLE empty_stopword(value VARCHAR(30)) ENGINE = InnoDB;
SET GLOBAL innodb_ft_server_stopword_table = 'grrs/empty_stopword';