package org.bdilab.grrs.bic.service;

import org.apache.commons.lang3.StringUtils;
import org.bdilab.grrs.bic.entity.User;
import org.bdilab.grrs.bic.entity.UserInfo;
import org.bdilab.grrs.bic.repository.UserRepository;
import org.bdilab.grrs.bic.service.result.ResponseResult;
import org.bdilab.grrs.bic.service.result.ResponseResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.constraints.NotBlank;

/**
 * @author caytng@163.com
 * @date 2019/4/10
 */
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/login")
    public ResponseResult<User> login(@NotBlank String userName, @NotBlank String userPswd) {
        UserInfo info = userRepository.findByUserName(userName);
        if (info == null || StringUtils.isBlank(info.getUserName())
                || StringUtils.isBlank(info.getUserPswd())) {
            return ResponseResultUtil.buildFailedResult("用户不存在");
        }
        if (userPswd.equals(info.getUserPswd())) {
            // 脱敏
            desensitize(info);
            return ResponseResultUtil.buildSuccessfulResult(info);
        }
        return ResponseResultUtil.buildFailedResult("密码错误");
    }

    @RequestMapping("/user/modifyPswd")
    public ResponseResult<Boolean> modifySelfPswd(@SessionAttribute UserInfo curUser, @NotBlank String oldPswd, @NotBlank String newPswd) {
        if (verifyIdentity(curUser, oldPswd)) {
            return updatePswd(curUser.getUserName(), newPswd, curUser.getUserName())?
                    ResponseResultUtil.buildSuccessfulResult(true)
                    : ResponseResultUtil.buildFailedResult("修改密码失败");
        }
        return ResponseResultUtil.buildFailedResult("身份验证失败");
    }

    @RequestMapping("/admin/addUser")
    public ResponseResult<Boolean> addUser(@SessionAttribute UserInfo curUser, @NotBlank String userName, @NotBlank String userPswd) {
        if (!isAdmin(curUser)) {
            return ResponseResultUtil.buildFailedResult("没有操作权限");
        }
        /// TODO: 处理UNIQUE异常
        UserInfo info = userRepository.findByUserName(userName);
        if (info != null) {
            return ResponseResultUtil.buildFailedResult("该用户名已被占用");
        }
        User newUser = userRepository.insert(userName, userPswd, curUser.getUserName(), curUser.getUserName());
        if (newUser.getId() == null || newUser.getId() < 0) {
            return ResponseResultUtil.buildFailedResult("添加失败");
        }
        return ResponseResultUtil.buildSuccessfulResult(true);
    }

    @RequestMapping("/admin/resetPswd")
    public ResponseResult<Boolean> resetPswd(@SessionAttribute UserInfo curUser, @NotBlank String userName, @NotBlank String newPswd) {
        if (isAdmin(curUser)) {
            return updatePswd(userName, newPswd, curUser.getUserName())?
                    ResponseResultUtil.buildSuccessfulResult(true)
                    : ResponseResultUtil.buildFailedResult("修改密码失败");
        }
        return ResponseResultUtil.buildFailedResult("没有操作权限");
    }

    private Boolean verifyIdentity(UserInfo curUser, String oldPswd) {
        UserInfo info = userRepository.findByUserName(curUser.getUserName());
        if (info == null || !oldPswd.equals(info.getUserPswd())) {
            return false;
        }
        return true;
    }

    private Boolean isAdmin(UserInfo userInfo) {
        /// TODO: 更严密的管理员验证手段
        return "admin".equals(userInfo.getUserName());
    }

    private Boolean updatePswd(String userName, String newPswd, String modifier) {
        return userRepository.update(userName, newPswd, modifier);
    }

    private UserInfo desensitize(UserInfo userInfo) {
        if (userInfo != null) {
            userInfo.setUserPswd(null);
        }
        return userInfo;
    }
}
