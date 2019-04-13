package org.bdilab.grrs.bic.repository;

import org.bdilab.grrs.bic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author caytng@163.com
 * @date 2019/4/10
 */
@Repository
@Transactional(rollbackFor = {Throwable.class})
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据创建者获取用户列表，包括已移除的用户
     * @param curUser
     * @return
     */
    @Query(value = "SELECT * FROM user WHERE creator = ?1 ORDER BY deleted", nativeQuery = true)
    List<User> findAllByCreator(String curUser);

    /**
     * 根据名字查找用户
     * @param userName 用户名
     * @return 用户信息
     */
    @Query(value = "SELECT * FROM user WHERE user_name = ?1 AND deleted = FALSE", nativeQuery = true)
    User findByUserName(String userName);

    /**
     * 插入一条用户信息
     * @param userName
     * @param userPswd
     * @param creator
     * @param modifier
     * @return
     */
    @Modifying
    @Query(value = "INSERT INTO user(user_name, user_pswd, creator, modifier) " +
            "VALUES(?1, ?2, ?3, ?4) ON DUPLICATE KEY " +
            "UPDATE deleted = FALSE, modifier = ?4, modify_time = now()", nativeQuery = true)
    Integer insert(String userName, String userPswd, String creator, String modifier);

    /**
     * 更新用户密码
     * @param userName
     * @param newPswd
     * @param modifier
     * @return
     */
    @Modifying
    @Query(value = "UPDATE user SET user_pswd = ?2, modifier = ?3, modify_time = now() " +
            "WHERE user_name = ?1 AND deleted = FALSE", nativeQuery = true)
    Integer update(String userName, String newPswd, String modifier);

    /**
     * 移除用户
     * @param userName
     * @return
     */
    @Modifying
    @Query(value = "UPDATE user SET deleted = TRUE, modifier = ?2, modify_time = now() " +
            "WHERE user_name = ?1 AND deleted = FALSE", nativeQuery = true)
    Integer remove(String userName, String modifier);

}
