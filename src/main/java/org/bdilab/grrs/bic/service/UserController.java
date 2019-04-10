package org.bdilab.grrs.bic.service;

import org.apache.commons.lang3.StringUtils;
import org.bdilab.grrs.bic.dao.User;
import org.bdilab.grrs.bic.dao.UserRepository;
import org.bdilab.grrs.bic.service.result.ResponseResult;
import org.bdilab.grrs.bic.service.result.ResponseResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author caytng@163.com
 * @date 2019/4/10
 */
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/user/login")
    public ResponseResult<User> login(@NotBlank String userName, @NotBlank String userPswd) {
        User user = userRepository.findByUserName(userName);
        if (user == null || StringUtils.isBlank(user.getUserName())
                || StringUtils.isBlank(user.getUserPswd())) {
            return ResponseResultUtil.buildFailedResult("user don't exist");
        }
        if (userPswd.equals(user.getUserPswd())) {
            User userInfo = new User();
            userInfo.setUserName(user.getUserName());
            return ResponseResultUtil.buildSuccessfulResult(userInfo);
        }
        return ResponseResultUtil.buildFailedResult("password is wrong");
    }

//    @RequestMapping("/user/add")
//    public ResponseResult addUser(@NotBlank String userName, @NotBlank String userPswd) {
//        User userInfo = new User();
//        userInfo.setUserName(userName);
//        userInfo.setUserPswd(userPswd);
//        userInfo.setCreator();
//        userInfo.setModifier();
//        return userRepository.insert(userName, userPswd);
//    }
}
