package org.bdilab.grrs.bic.entity;

import lombok.*;

/**
 * 用户信息传输对象
 * @author caytng@163.com
 * @date 2019/4/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    @NonNull private String userName;
    private String userPswd;
}
