package org.bdilab.grrs.bic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息传输对象
 * @author caytng@163.com
 * @date 2019/4/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String userName;
    private String userPswd;
}
