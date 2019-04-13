package org.bdilab.grrs.bic.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bdilab.grrs.bic.entity.*;
import org.bdilab.grrs.bic.repository.BookRepository;
import org.bdilab.grrs.bic.util.BookUtil;
import org.bdilab.grrs.bic.util.ResponseResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    private BookRepository repository;

    @ApiOperation("添加书籍")
    @RequestMapping(value = "/addBook", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addBook(@SessionAttribute UserInfo curUser, @RequestBody BookInfo bookInfo) {
        if (BookUtil.isIllegalInfo(bookInfo)) {
            return ResponseResultUtil.wrongParameters();
        }
        Book newBook = BookUtil.convert(bookInfo);
        List<Book> bookList = repository.findBooksByBookNameAndAuthors(newBook.getBookName(), newBook.getAuthors());
        if (BookUtil.isNotEmpty(bookList)) {
            return ResponseResultUtil.accepted(bookList);
        }
        Book result = repository.save(init(newBook, curUser.getUserName()));
        return ResponseResultUtil.created(result);
    }

    @ApiOperation("強制添加书籍")
    @RequestMapping(value = "/forceAddBook", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity forceAddBook(@SessionAttribute UserInfo curUser, @RequestBody BookInfo bookInfo) {
        if (BookUtil.isIllegalInfo(bookInfo)) {
            return ResponseResultUtil.wrongParameters();
        }
        Book newBook = init(BookUtil.convert(bookInfo), curUser.getUserName());
        Book result = repository.save(newBook);
        return ResponseResultUtil.created(result);
    }

    @ApiOperation("列举书籍")
    @RequestMapping(value = "/listBooks", method = RequestMethod.GET)
    public ResponseEntity listBooks(@SessionAttribute UserInfo curUser) {
        List<Book> books = repository.findAllByCreatorOrModifier(curUser.getUserName());
        return ResponseResultUtil.success(BookUtil.expand(books));
    }

    @ApiOperation("修改书籍")
    @RequestMapping(value = "/editBook", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editBook(@SessionAttribute UserInfo curUser, @RequestBody BookInfo bookInfo) {
        if (BookUtil.isIllegalInfo(bookInfo)) {
            return ResponseResultUtil.wrongParameters();
        }
        if (BookUtil.withoutId(bookInfo)) {
            return ResponseResultUtil.tooManyObjects();
        }
        if (!repository.existsById(bookInfo.getId())) {
            return ResponseResultUtil.wrongParameters();
        }
        Book updateCondition = edit(BookUtil.convert(bookInfo), curUser.getUserName());
        Book bookFromDB = repository.findById(bookInfo.getId()).get();
        Book result = repository.save(BookUtil.merge(bookFromDB, updateCondition));
        return ResponseResultUtil.success(result);
    }

    private Book init(Book book, String operator) {
        book.setCreator(operator);
        book.setModifier(operator);
        book.setCreateTime(LocalDateTime.now());
        book.setModifyTime(LocalDateTime.now());
        book.setDeleted(false);
        return book;
    }

    private Book edit(Book book, String operator) {
        book.setModifier(operator);
        book.setModifyTime(LocalDateTime.now());
        return book;
    }
}
