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

    @ApiOperation(value = "添加书籍", response = ResponseEntity.class, notes = "参数有误，返回406；库中存在同名书籍，返回202和书籍列表；添加成功，返回200")
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
        repository.save(init(newBook, curUser.getUserName()));
        return ResponseResultUtil.done();
    }

    @ApiOperation(value = "強制添加书籍", response = ResponseEntity.class, notes = "参数有误，返回406；添加成功，返回200")
    @RequestMapping(value = "/forceAddBook", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity forceAddBook(@SessionAttribute UserInfo curUser, @RequestBody BookInfo bookInfo) {
        if (BookUtil.isIllegalInfo(bookInfo)) {
            return ResponseResultUtil.wrongParameters();
        }
        Book newBook = init(BookUtil.convert(bookInfo), curUser.getUserName());
        repository.save(newBook);
        return ResponseResultUtil.done();
    }

    @ApiOperation(value = "列举书籍", response = ResponseEntity.class, notes = "返回200和书籍列表，默认按照书名排序")
    @RequestMapping(value = "/listBooks", method = RequestMethod.GET)
    public ResponseEntity listBooks(@SessionAttribute UserInfo curUser) {
        List<Book> books = repository.findAllByCreatorOrModifier(curUser.getUserName());
        return ResponseResultUtil.success(BookUtil.expand(books));
    }

    @ApiOperation(value = "修改书籍", response = ResponseEntity.class, notes = "参数有误，返回406；缺少书籍ID，无法确定操作对象，返回409；修改成功，返回200和修改后的书籍信息")
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
