package org.bdilab.grrs.bic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bdilab.grrs.bic.param.ARInterestLevel;
import org.bdilab.grrs.bic.param.LexilePrefix;

import javax.validation.constraints.NotNull;
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
    @NonNull private String bookName;
//    @NonNull private List<String> authors;
    @NotNull private String authors;
//    private List<String> isbns;
    private String isbns;
    private String coverUrl;
    private String summary;
//    private List<String> topics;
    private String topics;
//    private List<String> series;
    private String series;
    private Boolean isFiction;
    private Float arBl;
    private ARInterestLevel arIl;
    private Float arPoints;
    private Float arRating;
    private LexilePrefix lexilePrefix;
    private Integer lexile;
    private Integer wordcount;
    private Integer pagecount;
    private Float amazonRating;
}
