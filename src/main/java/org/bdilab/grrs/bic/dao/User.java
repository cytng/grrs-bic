package org.bdilab.grrs.bic.dao;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author caytng@163.com
 * @date 2019/4/10
 */
@Data
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
