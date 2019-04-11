package org.bdilab.grrs.bic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author caytng@163.com
 * @date 2019/4/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookInfo {
    private Long id;
    private String bookName;
    private List<String> authors;
    private List<String> isbns;
    private String coverUrl;
    private String summary;
    private List<String> topics;
    private List<String> series;
    private Boolean is_fiction;
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
