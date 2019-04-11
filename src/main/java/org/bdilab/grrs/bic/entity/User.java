package org.bdilab.grrs.bic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户数据表
 * @author caytng@163.com
 * @date 2019/4/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String creator;
    @Column(nullable = false)
    private String modifier;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private Boolean deleted;

    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false)
    private String userPswd;
}
