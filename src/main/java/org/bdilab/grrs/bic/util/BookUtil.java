package org.bdilab.grrs.bic.util;

import org.bdilab.grrs.bic.entity.Book;
import org.bdilab.grrs.bic.entity.BookInfo;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public static Boolean same(Book book1, Book book2) {
        return Objects.equals(book1.getBookName(), book2.getBookName())
                && Objects.equals(book1.getAuthors(), book2.getAuthors());
    }

    public static Boolean same(BookInfo info1, BookInfo info2) {
        return Objects.equals(info1.getBookName(), info2.getBookName())
                && Objects.equals(info1.getAuthors(), info2.getAuthors());
    }

    public static Book merge(Book old, Book update) {
        boolean contentUpdated = false;
        if (isNotNull(update.getBookName())) {
            old.setBookName(update.getBookName());
            contentUpdated = true;
        }
        if (isNotNull(update.getAuthors())) {
            old.setAuthors(update.getAuthors());
            contentUpdated = true;
        }
        if (isNotNull(update.getIsbns())) {
            old.setIsbns(update.getIsbns());
            contentUpdated = true;
        }
        if (isNotNull(update.getCoverUrl())) {
            old.setCoverUrl(update.getCoverUrl());
            contentUpdated = true;
        }
        if (isNotNull(update.getSummary())) {
            old.setSummary(update.getSummary());
            contentUpdated = true;
        }
        if (isNotNull(update.getTopics())) {
            old.setTopics(update.getTopics());
            contentUpdated = true;
        }
        if (isNotNull(update.getSeries())) {
            old.setSeries(update.getSeries());
            contentUpdated = true;
        }
        if (isNotNull(update.getIsFiction())) {
            old.setIsFiction(update.getIsFiction());
            contentUpdated = true;
        }
        if (isNotNull(update.getArBl())) {
            old.setArBl(update.getArBl());
            contentUpdated = true;
        }
        if (isNotNull(update.getArIl())) {
            old.setArIl(update.getArIl());
            contentUpdated = true;
        }
        if (isNotNull(update.getArPoints())) {
            old.setArPoints(update.getArPoints());
            contentUpdated = true;
        }
        if (isNotNull(update.getArRating())) {
            old.setArRating(update.getArRating());
            contentUpdated = true;
        }
        if (isNotNull(update.getLexilePrefix())) {
            old.setLexilePrefix(update.getLexilePrefix());
            contentUpdated = true;
        }
        if (isNotNull(update.getLexile())) {
            old.setLexile(update.getLexile());
            contentUpdated = true;
        }
        if (isNotNull(update.getWordcount())) {
            old.setWordcount(update.getWordcount());
            contentUpdated = true;
        }
        if (isNotNull(update.getPagecount())) {
            old.setPagecount(update.getPagecount());
            contentUpdated = true;
        }
        if (isNotNull(update.getAmazonRating())) {
            old.setAmazonRating(update.getAmazonRating());
            contentUpdated = true;
        }
        if (contentUpdated && isNotNull(update.getModifier())) {
            old.setModifier(update.getModifier());
            old.setModifyTime(LocalDateTime.now());
        }
        return old;
    }

    public static BookInfo merge(BookInfo old, BookInfo update) {
        return convert(merge(convert(old), convert(update)));
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

    public static String convert(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            builder.append(str);
            builder.append(SEPARATOR);
        }
        return builder.toString();
    }

    public static List<String> convert(String str) {
        List<String> list = new ArrayList<>();
        String[] strings = str.split(SEPARATOR);
        for (String s: strings) {
            if (isNotBlank(s)) {
                list.add(s);
            }
        }
        return list;
    }

}
