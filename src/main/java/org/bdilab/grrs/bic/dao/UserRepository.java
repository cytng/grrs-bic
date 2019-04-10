package org.bdilab.grrs.bic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author caytng@163.com
 * @date 2019/4/10
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据名字查找用户
     * @param userName 用户名
     * @return 用户信息
     */
    @Query(value = "SELECT id, user_name, DECODE(user_pswd, 'grrs') user_pswd, creator, modifier, create_time, modify_time, deleted FROM grrs.user WHERE user_name = :name AND deleted = FALSE ", nativeQuery = true)
    User findByUserName(@Param("name") String userName);

    /**
     * 插入一个用户
     * @param userName
     * @param userPswd
     * @param creator
     * @param modifier
     * @return
     */
    @Query(value = "INSERT INTO grrs.user(user_name, user_pswd, creator, modifier) VALUES(:userName, ENCODE(:userPswd, 'grrs'), :creator, :modifier)", nativeQuery = true)
    @Modifying
    User insert(String userName, String userPswd, String creator, String modifier);
}
