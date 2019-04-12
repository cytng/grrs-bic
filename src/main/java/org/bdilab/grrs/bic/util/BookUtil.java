package org.bdilab.grrs.bic.util;

import org.bdilab.grrs.bic.entity.Book;
import org.bdilab.grrs.bic.entity.BookInfo;
import org.springframework.beans.BeanUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author caytng@163.com
 * @date 2019/4/12
 */
public class BookUtil extends CommonUtil {
    private static final String SEPARATOR = ";";

    public static Boolean isIllegalInfo(BookInfo bookInfo) {
        return isNull(bookInfo)
                || isBlank(bookInfo.getBookName())
                || isEmpty(bookInfo.getAuthors());
    }

    public static Boolean withoutId(BookInfo bookInfo) {
        return isNull(bookInfo.getId());
    }

    public static Book convert(BookInfo bookInfo) {
        Book book = new Book();
        BeanUtils.copyProperties(bookInfo, book);
        if (isNotEmpty(bookInfo.getAuthors())) {
            book.setAuthors(convert(bookInfo.getAuthors()));
        }
        if (isNotEmpty(bookInfo.getIsbns())) {
            book.setIsbns(convert(bookInfo.getIsbns()));
        }
        if (isNotEmpty(bookInfo.getTopics())) {
            book.setTopics(convert(bookInfo.getTopics()));
        }
        if (isNotEmpty(bookInfo.getSeries())) {
            book.setSeries(convert(bookInfo.getSeries()));
        }
        return book;
    }

    public static BookInfo convert(Book book) {
        BookInfo bookInfo = new BookInfo();
        BeanUtils.copyProperties(book, bookInfo);
        if (isNotBlank(book.getAuthors())) {
            bookInfo.setAuthors(convert(book.getAuthors()));
        }
        if (isNotBlank(book.getIsbns())) {
            bookInfo.setIsbns(convert(book.getIsbns()));
        }
        if (isNotBlank(book.getTopics())) {
            bookInfo.setTopics(convert(book.getTopics()));
        }
        if (isNotBlank(book.getSeries())) {
            bookInfo.setSeries(convert(book.getSeries()));
        }
        return bookInfo;
    }

    public static List<Book> combine(List<BookInfo> bookInfos) {
        List<Book> bookList = new ArrayList<>();
        for (BookInfo bookInfo: bookInfos) {
            bookList.add(convert(bookInfo));
        }
        return bookList;
    }

    public static List<BookInfo> expand(List<Book> bookList) {
        List<BookInfo> bookInfos = new ArrayList<>();
        for (Book book: bookList) {
            bookInfos.add(convert(book));
        }
        return bookInfos;
    }

    private static String convert(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            builder.append(str);
            builder.append(SEPARATOR);
        }
        return builder.toString();
    }

    private static List<String> convert(String str) {
        List<String> list = Arrays.asList();
        String[] strings = str.split(SEPARATOR);
        for (String s: strings) {
            if (isNotBlank(s)) {
                list.add(s);
            }
        }
        return list;
    }


}
