package org.bdilab.grrs.bic.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bdilab.grrs.bic.entity.*;
import org.bdilab.grrs.bic.repository.BookRepository;
import org.bdilab.grrs.bic.service.util.BookUtil;
import org.bdilab.grrs.bic.service.util.ResponseResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

/**
 * @author caytng@163.com
 * @date 2019/4/11
 */
@Api(description = "书籍接口")
@RestController
@RequestMapping("/user")
public class BookController {

    @Autowired
    BookRepository repository;

    @ApiOperation("添加书籍")
    @RequestMapping("/addBook")
    public ResponseEntity addBook(@SessionAttribute UserInfo curUser, @RequestBody BookInfo bookInfo) {
        if (BookUtil.isIllegalInfo(bookInfo)) {
            return ResponseResultUtil.wrongParameters();
        }
        Book newBook = BookUtil.convert(bookInfo);
        List<Book> bookList = repository.findBooksByBookNameAndAuthors(newBook.getBookName(), newBook.getAuthors());
        if (BookUtil.isNotEmpty(bookList)) {
            return ResponseResultUtil.success(bookList);
        }
        newBook.setCreator(curUser.getUserName());
        newBook.setModifier(curUser.getUserName());
        Book result = repository.save(newBook);
        return ResponseResultUtil.created(result);
    }


}
