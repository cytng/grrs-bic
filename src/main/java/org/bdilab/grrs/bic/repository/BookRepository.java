package org.bdilab.grrs.bic.repository;

import org.bdilab.grrs.bic.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author caytng@163.com
 * @date 2019/4/11
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * 完全匹配书籍
     * @param bookName
     * @param authors
     * @return
     */
    @Query(value = "SELECT * FROM grrs.book WHERE book_name = ?1 AND authors = ?2", nativeQuery = true)
    List<Book> findBooksByBookNameAndAuthors(String bookName, String authors);
}
