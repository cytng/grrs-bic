package org.bdilab.grrs.bic.controller;

import org.bdilab.grrs.bic.entity.Book;
import org.bdilab.grrs.bic.entity.BookInfo;
import org.bdilab.grrs.bic.entity.UserInfo;
import org.bdilab.grrs.bic.repository.BookRepository;
import org.bdilab.grrs.bic.repository.UserRepository;
import org.bdilab.grrs.bic.util.BookUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author caytng@163.com
 * @date 2019/4/13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@SpringBootTest
public class BookControllerTest {

    @Autowired
    private BookController bookController;

    @Autowired
    private AdminController adminController;

    @Autowired
    private UserController userController;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;


    private UserInfo adminInfo;
    private UserInfo userInfo;

    @Before
    public void setUp() throws Exception {
        userRepository.insert("admin", "$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG", "system", "system");
        adminInfo = new UserInfo("admin", "password");
        userInfo = new UserInfo("caytng", "123456");

        userController.login(adminInfo);
        adminController.addOrEnableUser(adminInfo, userInfo);
    }

    @After
    public void tearDown() throws Exception {
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void test() {
        BookInfo newBook = new BookInfo();
        userController.login(userInfo);
        ResponseEntity addResult = bookController.addBook(userInfo, newBook);
        Assert.assertNotNull(addResult);
        Assert.assertTrue(HttpStatus.NOT_ACCEPTABLE.equals(addResult.getStatusCode()));

        newBook.setBookName("The Cat in the Hat");
        List<String> authors = new ArrayList();
        authors.add("Dr. Seuss");
        newBook.setAuthors(authors);

        addResult = bookController.addBook(userInfo, newBook);
        Assert.assertNotNull(addResult);
        Assert.assertTrue(HttpStatus.CREATED.equals(addResult.getStatusCode()));

        userController.login(adminInfo);
        addResult = bookController.addBook(adminInfo, newBook);
        Assert.assertNotNull(addResult);
        Assert.assertTrue(HttpStatus.ACCEPTED.equals(addResult.getStatusCode()));
        Assert.assertNotNull(addResult.getBody());
        Assert.assertTrue(addResult.getBody() instanceof List);
        List<Book> bookList = (List<Book>) addResult.getBody();
        Assert.assertEquals(bookList.size(), 1L);

        List<BookInfo> bookInfos = BookUtil.expand(bookList);
        BookInfo bookInfo1 = bookInfos.get(0);
        Assert.assertTrue(BookUtil.same(newBook, bookInfo1));

        addResult = bookController.forceAddBook(adminInfo, newBook);
        Assert.assertNotNull(addResult);
        Assert.assertTrue(HttpStatus.CREATED.equals(addResult.getStatusCode()));
        Assert.assertTrue(addResult.getBody() instanceof Book);
        BookInfo bookInfo2 = BookUtil.convert((Book) addResult.getBody());
        Assert.assertFalse(bookInfo1.equals(bookInfo2));
        Assert.assertTrue(BookUtil.same(bookInfo1, bookInfo2));

        userController.login(userInfo);
        ResponseEntity listResult = bookController.listBooks(userInfo);
        Assert.assertNotNull(listResult);
        Assert.assertTrue(listResult.getBody() instanceof List);
        List<Book> books = (List<Book>) listResult.getBody();
        Assert.assertEquals(books.size(), 1L);

        ResponseEntity editResult = bookController.editBook(userInfo, bookInfo2);
        Assert.assertNotNull(editResult);
        Assert.assertTrue(editResult.getBody() instanceof Book);
        Book bookAfterEditing = (Book) editResult.getBody();
        Assert.assertFalse(bookAfterEditing.getCreator().equals(bookAfterEditing.getModifier()));

        listResult = bookController.listBooks(userInfo);
        Assert.assertNotNull(listResult);
        Assert.assertTrue(listResult.getBody() instanceof List);
        books = (List<Book>) listResult.getBody();
        Assert.assertEquals(books.size(), 2L);
        System.out.println(books);
    }
}
