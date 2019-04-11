package org.bdilab.grrs.bic.repository;

import org.bdilab.grrs.bic.entity.User;
import org.bdilab.grrs.bic.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import static org.bdilab.grrs.bic.param.Project.SHORT_NAME;

/**
 * @author caytng@163.com
 * @date 2019/4/10
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String FIND_BY_NAME = "SELECT user_name, DECODE(user_pswd, " + SHORT_NAME + ") user_pswd " +
            "FROM grrs.user WHERE user_name = :name AND deleted = FALSE ";
    String INSERT_NEW_USER = "INSERT INTO grrs.user(user_name, user_pswd, creator, modifier) " +
            "VALUES(:userName, ENCODE(:userPswd, " + SHORT_NAME + "), :creator, :modifier)";
    String UPDATE_PSWD = "UPDATE grrs.user " +
            "SET user_pswd = ENCODE(:newPswd, " + SHORT_NAME + "), modifier = :modifier " +
            "WHERE user_name = :userName";

    /**
     * 根据名字查找用户
     * @param userName 用户名
     * @return 用户信息
     */
    @Query(value = FIND_BY_NAME, nativeQuery = true)
    UserInfo findByUserName(@Param("name") String userName);

    /**
     * 插入一条用户信息
     * @param userName
     * @param userPswd
     * @param creator
     * @param modifier
     * @return
     */
    @Modifying
    @Query(value = INSERT_NEW_USER, nativeQuery = true)
    User insert(String userName, String userPswd, String creator, String modifier);

    /**
     * 更新用户密码
     * @param userName
     * @param newPswd
     * @param modifier
     * @return
     */
    @Modifying
    @Query(value = UPDATE_PSWD, nativeQuery = true)
    Boolean update(String userName, String newPswd, String modifier);
}
