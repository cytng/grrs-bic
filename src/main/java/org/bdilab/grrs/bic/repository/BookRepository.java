package org.bdilab.grrs.bic.repository;

import org.bdilab.grrs.bic.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author caytng@163.com
 * @date 2019/4/11
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
