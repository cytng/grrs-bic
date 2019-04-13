package org.bdilab.grrs.bic.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.bdilab.grrs.bic.entity.User;
import org.bdilab.grrs.bic.entity.UserInfo;
import org.bdilab.grrs.bic.param.LoggerName;
import org.bdilab.grrs.bic.repository.UserRepository;
import org.bdilab.grrs.bic.util.ResponseResultUtil;
import org.bdilab.grrs.bic.util.UserUtil;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author caytng@163.com
 * @date 2019/4/11
 */
@Api(description = "管理员接口")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository repository;

    @ApiOperation(value = "管理员添加或启用用户")
    @RequestMapping(value = "/addOrEnableUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addOrEnableUser(@SessionAttribute UserInfo curUser, @RequestBody UserInfo newUserInfo) {
        if (UserUtil.isIllegalInfo(newUserInfo)) {
            return ResponseResultUtil.wrongParameters();
        }
        if (UserUtil.isSelf(curUser, newUserInfo)) {
            return ResponseResultUtil.withoutPermmision();
        }
        try {
            Integer result = repository.insert(newUserInfo.getUserName(), newUserInfo.getUserPswd(),
                    curUser.getUserName(), curUser.getUserName());
            if (result == 1) {
                User user = repository.findByUserName(newUserInfo.getUserName());
                return ResponseResultUtil.created(user);
            }
            // ON DUPLICATE KEY UPDATE
            if (result == 2) {
                User user = repository.findByUserName(newUserInfo.getUserName());
                return ResponseResultUtil.accepted(user);
            }
        } catch (HibernateException e) {
            LogManager.getLogger(LoggerName.ERROR).error("Admin add user[{}] failed", newUserInfo.getUserName());
            LogManager.getLogger(LoggerName.DB).warn("Operation failed: insert into table[user]", e);
        }
        return ResponseResultUtil.failure("添加用户失败");
    }

    @ApiOperation(value = "管理员禁用用户")
    @RequestMapping(value = "/disableUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity disableUser(@SessionAttribute UserInfo curUser, @RequestBody String userName) {
        if (UserUtil.isBlankString(userName)) {
            return ResponseResultUtil.wrongParameters();
        }
        if (UserUtil.isSelf(curUser, new UserInfo(userName, null))) {
            return ResponseResultUtil.withoutPermmision();
        }
        try {
            repository.remove(userName, curUser.getUserName());
        } catch (HibernateException e) {
            LogManager.getLogger(LoggerName.ERROR).error("Admin remove user[{}] failed", userName);
            LogManager.getLogger(LoggerName.DB).warn("Operation failed: update table[user] set deleted", e);
            return ResponseResultUtil.failure("移除用户失败");
        }
        return ResponseResultUtil.done();
    }

    @ApiOperation(value = "管理员列举用户")
    @RequestMapping(value = "/listUsers", method = RequestMethod.GET)
    public ResponseEntity listUsers(@SessionAttribute UserInfo curUser) {
        List<User> users = repository.findAllByCreator(curUser.getUserName());
        return ResponseResultUtil.success(UserUtil.desensitize(users));
    }

    @ApiOperation(value = "管理员重置密码")
    @RequestMapping(value = "/resetPswd", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity resetPswd(@SessionAttribute UserInfo curUser, @RequestBody UserInfo userInfo) {
        if (UserUtil.isIllegalInfo(userInfo)) {
            return ResponseResultUtil.wrongParameters();
        }
        if (UserUtil.isSelf(curUser, userInfo)) {
            return ResponseResultUtil.withoutPermmision();
        }
        try {
            repository.update(userInfo.getUserName(), UserUtil.encodePswd(userInfo.getUserPswd()), curUser.getUserName());
        } catch (HibernateException e) {
            LogManager.getLogger(LoggerName.ERROR).error("Admin reset pswd of user[{}] failed", userInfo.getUserName());
            LogManager.getLogger(LoggerName.DB).warn("Operation failed: update table[user] set user_pswd", e);
            return ResponseResultUtil.failure("重置密码失败");
        }
        return ResponseResultUtil.done();
    }

}
