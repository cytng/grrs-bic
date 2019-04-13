package org.bdilab.grrs.bic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 书籍数据表
 * @author caytng@163.com
 * @date 2019/4/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {
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

    @Column(nullable = false)
    private String bookName;
    @Column(nullable = false)
    private String authors;
    private String isbns;
    private String coverUrl;
    private String summary;
    private String topics;
    private String series;
    private Boolean isFiction;
    private Float arBl;
    private String arIl;
    private Float arPoints;
    private Float arRating;
    private String lexilePrefix;
    private Integer lexile;
    private Integer wordcount;
    private Integer pagecount;
    private Float amazonRating;

}
