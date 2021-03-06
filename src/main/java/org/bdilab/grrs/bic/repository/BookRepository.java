package org.bdilab.grrs.bic.repository;

import org.bdilab.grrs.bic.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author caytng@163.com
 * @date 2019/4/11
 */
@Repository
@Transactional(rollbackFor = {Throwable.class})
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * 完全匹配书籍
     * @param bookName
     * @param authors
     * @return
     */
    @Query(value = "SELECT * FROM book WHERE book_name = ?1 AND authors = ?2 AND deleted = FALSE", nativeQuery = true)
    List<Book> findBooksByBookNameAndAuthors(String bookName, String authors);

    /**
     * 根据创建者获取书籍列表
     * @param creator
     * @return
     */
    @Query(value = "SELECT * FROM book WHERE (creator = ?1 OR modifier = ?1) AND deleted = FALSE ORDER BY book_name", nativeQuery = true)
    List<Book> findAllByCreatorOrModifier(String creator);

    /**
     * 根据关键字搜索书籍
     * @param keyword
     * @return
     */
    @Query(value = "SELECT * FROM book WHERE MATCH(book_name, authors) AGAINST(?1)", nativeQuery = true)
    List<Book> findBooksByKeywordInBookNameOrAuthors(String keyword);

}
