package org.bdilab.grrs.bic.controller;

import io.swagger.annotations.*;
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

/**
 * 用户接口
 * @author caytng@163.com
 * @date 2019/4/10
 */
@Api(description = "用户接口")
@RestController
public class UserController {

    @Autowired
    private UserRepository repository;

    @ApiOperation(value = "用户登录", response = ResponseEntity.class, notes = "参数有误，返回406；用户不存在或密码错误，返回420；登录成功，返回200和用户信息")
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody UserInfo userInfo) {
        if (UserUtil.isIllegalInfo(userInfo)) {
            return ResponseResultUtil.wrongParameters();
        }
        User user = repository.findByUserName(userInfo.getUserName());
        if (UserUtil.isIllegalInfo(user)) {
            return ResponseResultUtil.failure("用户不存在");
        }
        if (UserUtil.matchPswd(userInfo.getUserPswd(), user.getUserPswd())) {
            // 脱敏
            UserInfo info = UserUtil.desensitize(UserUtil.extract(user));
            return ResponseResultUtil.success(info);
        }
        return ResponseResultUtil.failure("密码错误");
    }

    @ApiOperation(value = "用户修改密码", response = ResponseEntity.class, notes = "参数有误，返回406；旧密码验证失败或修改失败，返回420；修改成功，返回200")
    @RequestMapping(value = "/user/modifyPswd", method = RequestMethod.POST)
    public ResponseEntity modifySelfPswd(@SessionAttribute UserInfo curUser, @RequestBody String oldPswd, @RequestBody String newPswd) {
        if (UserUtil.isBlank(oldPswd) || UserUtil.isBlank(newPswd)) {
            return ResponseResultUtil.wrongParameters();
        }
        if (verifyIdentity(curUser, oldPswd)) {
            try {
                repository.update(curUser.getUserName(), UserUtil.encodePswd(newPswd), curUser.getUserName());
            } catch (HibernateException e) {
                LogManager.getLogger(LoggerName.ERROR).error("Admin reset pswd of user[{}] failed", curUser.getUserName());
                LogManager.getLogger(LoggerName.DB).warn("Operation failed: update table[user] set pswd", e);
                return ResponseResultUtil.failure("修改密码失败");
            }
            return ResponseResultUtil.done();
        }
        return ResponseResultUtil.failure("身份验证失败");
    }

    private Boolean verifyIdentity(UserInfo curUser, String oldPswd) {
        User info = repository.findByUserName(curUser.getUserName());
        if (UserUtil.isNull(info) || UserUtil.dismatchPswd(oldPswd, info.getUserPswd())) {
            return false;
        }
        return true;
    }

}
