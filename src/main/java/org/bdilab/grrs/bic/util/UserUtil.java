package org.bdilab.grrs.bic.util;

import org.apache.commons.lang3.StringUtils;
import org.bdilab.grrs.bic.entity.User;
import org.bdilab.grrs.bic.entity.UserInfo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author caytng@163.com
 * @date 2019/4/11
 */
public class UserUtil extends CommonUtil {
    public static final String ADMIN_NAME = "admin";
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static Boolean isAdmin(UserInfo userInfo) {
        /// TODO: 更严密的管理员验证手段
        return ADMIN_NAME.equals(userInfo.getUserName());
    }

    public static Boolean isNotAdmin(UserInfo userInfo) {
        return !isAdmin(userInfo);
    }

    public static Boolean isSelf(UserInfo operator, UserInfo userInfo) {
        return Objects.equals(operator.getUserName(), userInfo.getUserName());
    }

    public static UserInfo extract(User user) {
        if (isNull(user)) {
            return null;
        }
        return new UserInfo(user.getUserName(), user.getUserPswd());
    }

    public static List<UserInfo> extract(List<User> userList) {
        List<UserInfo> userInfos = new ArrayList<>();
        for (User user: userList) {
            userInfos.add(extract(user));
        }
        return userInfos;
    }

    public static UserInfo desensitize(UserInfo userInfo) {
        if (isNotNull(userInfo)) {
            userInfo.setUserPswd(null);
        }
        return userInfo;
    }

    private static User desensitize(User user) {
        if (isNotNull(user)) {
            user.setUserPswd(null);
        }
        return user;
    }

    public static List<User> desensitize(List<User> userList) {
        for (User user: userList) {
            desensitize(user);
        }
        return userList;
    }

    public static Boolean isBlankString(String str) {
        return StringUtils.isBlank(str);
    }

    public static Boolean isIllegalInfo(UserInfo userInfo) {
        return isNull(userInfo)
                || isBlank(userInfo.getUserName())
                || isBlank(userInfo.getUserPswd());
    }

    public static Boolean isIllegalInfo(User user) {
        return isNull(user)
                || isBlank(user.getUserName())
                || isBlank(user.getUserPswd());
    }

    public static Boolean matchPswd(String rawPswd, String encodedPswd) {
        return ENCODER.matches(rawPswd, encodedPswd);
    }

    public static Boolean dismatchPswd(String rawPswd, String encodedPswd) {
        return !matchPswd(rawPswd, encodedPswd);
    }

    public static String encodePswd(String pswd) {
        return ENCODER.encode(pswd);
    }
}
