package org.bdilab.grrs.bic.repository;

import org.bdilab.grrs.bic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
    @Query(value = "SELECT * FROM grrs.user WHERE user_name = ?1 AND deleted = FALSE", nativeQuery = true)
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
    @Query(value = "INSERT INTO grrs.user(user_name, user_pswd, creator, modifier) " +
            "VALUES(?1, ?2, ?3, ?4)", nativeQuery = true)
    Boolean insert(String userName, String userPswd, String creator, String modifier);

    /**
     * 更新用户密码
     * @param userName
     * @param newPswd
     * @param modifier
     * @return
     */
    @Modifying
    @Query(value = "UPDATE grrs.user SET user_pswd = ?2, modifier = ?3 " +
            "WHERE user_name = ?1 AND deleted = FALSE", nativeQuery = true)
    Boolean update(String userName, String newPswd, String modifier);

    /**
     * 移除用户
     * @param userName
     * @return
     */
    @Modifying
    @Query(value = "UPDATE grrs.user SET deleted = TRUE " +
            "WHERE user_name = ?1 AND deleted = FALSE", nativeQuery = true)
    Boolean remove(String userName);
}
