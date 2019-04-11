package org.bdilab.grrs.bic.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.bdilab.grrs.bic.entity.UserInfo;
import org.bdilab.grrs.bic.param.LoggerName;
import org.bdilab.grrs.bic.repository.UserRepository;
import org.bdilab.grrs.bic.service.util.ResponseResultUtil;
import org.bdilab.grrs.bic.service.util.UserUtil;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author caytng@163.com
 * @date 2019/4/11
 */
@Api(description = "管理员接口")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserRepository repository;

    @ApiOperation(value = "管理员添加用户")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addUser(@SessionAttribute UserInfo curUser, @RequestBody UserInfo newUserInfo) {
        if (UserUtil.isIllegalInfo(newUserInfo)) {
            return ResponseResultUtil.wrongParameters();
        }
        try {
            repository.insert(newUserInfo.getUserName(), newUserInfo.getUserPswd(),
                    curUser.getUserName(), curUser.getUserName());
        } catch (HibernateException e) {
            LogManager.getLogger(LoggerName.ERROR).error("Admin add user[{}] failed", newUserInfo.getUserName());
            LogManager.getLogger(LoggerName.DB).warn("Operation failed: insert into table[user]", e);
            return ResponseResultUtil.failure("添加用户失败");
        }
        return ResponseResultUtil.success(true);
    }

    @ApiOperation(value = "管理员移除用户")
    @RequestMapping(value = "/removeUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeUser(@SessionAttribute UserInfo curUser, @RequestBody String userName) {
        if (UserUtil.isBlankString(userName)) {
            return ResponseResultUtil.wrongParameters();
        }
        try {
            repository.remove(userName);
        } catch (HibernateException e) {
            LogManager.getLogger(LoggerName.ERROR).error("Admin remove user[{}] failed", userName);
            LogManager.getLogger(LoggerName.DB).warn("Operation failed: update table[user] set deleted", e);
            return ResponseResultUtil.failure("移除用户失败");
        }
        return ResponseResultUtil.success(true);
    }

    @ApiOperation(value = "管理员重置密码")
    @RequestMapping(value = "/resetPswd", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity resetPswd(@SessionAttribute UserInfo curUser, @RequestBody UserInfo userInfo) {
        if (UserUtil.isIllegalInfo(userInfo)) {
            return ResponseResultUtil.wrongParameters();
        }
        try {
            repository.update(userInfo.getUserName(), userInfo.getUserPswd(), curUser.getUserName());
        } catch (HibernateException e) {
            LogManager.getLogger(LoggerName.ERROR).error("Admin reset pswd of user[{}] failed", userInfo.getUserName());
            LogManager.getLogger(LoggerName.DB).warn("Operation failed: update table[user] set user_pswd", e);
            return ResponseResultUtil.failure("重置密码失败");
        }
        return ResponseResultUtil.success(true);
    }


}
