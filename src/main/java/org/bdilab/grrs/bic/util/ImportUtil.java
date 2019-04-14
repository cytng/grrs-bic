package org.bdilab.grrs.bic.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.*;
import org.bdilab.grrs.bic.entity.Book;
import org.bdilab.grrs.bic.param.ARInterestLevel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.regex.Pattern.*;

/**
 * @author caytng@163.com
 * @date 2019/4/14
 */
public class ImportUtil {

    public static void main(String[] args) {
        System.out.println(importFromExcel(new File("E:\\cytng\\实验室项目\\智慧英语阅读\\书单信息采集",
                "书单信息汇总.xlsx"), "信息采集表"));
    }

    private static String importFromExcel(File excelFile, String sheetName) {
        String fileName = excelFile.getName();
        if (fileName.endsWith(".xlsx")) {
            try (InputStream inputStream = new FileInputStream(excelFile);
                 XSSFWorkbook hssfWorkbook = new XSSFWorkbook(inputStream)) {
                XSSFSheet hssfSheet = hssfWorkbook.getSheet(sheetName);


                XSSFRow titles = hssfSheet.getRow(0);
                int columnCount = titles.getLastCellNum();
                String[] column = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    XSSFCell cell = titles.getCell(i);
                    column[i] = cell.toString();
                }

                List<Book> bookList = new ArrayList();
                int last = hssfSheet.getLastRowNum();
                for (int i = 1; i < last; i++) {
                    Book book = map(column, hssfSheet.getRow(i));
                    if (book == null) {continue;}
                    bookList.add(book);
                }
                return generateSQL(bookList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Book map(String[] column, XSSFRow row) {
        Book book = null;
        int first = row.getFirstCellNum();
        int last = row.getLastCellNum();
        for (int i = first; i < last; i++) {
            XSSFCell cell = row.getCell(i);
            if (cell == null || StringUtils.isBlank(cell.toString())) {
                continue;
            }
            book = book == null? new Book(): book;
            try {
                switch (column[i - first]) {
                    case "Book Name": book.setBookName(cell.toString()); break;
                    case "Authors": book.setAuthors(cell.toString()); break;
                    case "ISBN":
                        try {
                            book.setIsbns(Long.toString(Double.valueOf(cell.getNumericCellValue()).longValue()));
                        } catch (IllegalStateException e) {
                            book.setIsbns(cell.getStringCellValue());
                        }
                        break;
                    case "Cover": String cover = cell.toString();
                        if (cover.startsWith("http")) {book.setCoverUrl(cover);}
                        break;
                    case "Summary": book.setSummary(cell.toString()); break;
                    case "Words": book.setWordcount(Double.valueOf(cell.toString()).intValue()); break;
                    case "Category": Boolean isFiction = "F".equals(cell.toString());
                        book.setIsFiction(isFiction); break;
                    case "Topic": book.setTopics(cell.toString()); break;
                    case "Series": book.setSeries(cell.toString()); break;
                    case "Pages": book.setPagecount(Double.valueOf(cell.toString()).intValue()); break;
                    case "ATOS": book.setArBl((float) cell.getNumericCellValue()); break;
                    case "IL": String str = cell.toString();
                        String fullname = str.substring(str.indexOf('(') + 1, str.indexOf(')'));
                        book.setArIl(ARInterestLevel.find(fullname).name()); break;
                    case "AR Points": book.setArPoints((float) cell.getNumericCellValue()); break;
                    case "Lexile": str = cell.toString();
                        String prefix = str.substring(0, 2);
                        if (!Character.isDigit(prefix.charAt(0)) &&
                                !Character.isDigit(prefix.charAt(1))) {book.setLexilePrefix(prefix);}
                        String level = compile("[^0-9]").matcher(str).replaceAll("");
                        if (StringUtils.isBlank(level)) {break;}
                        book.setLexile(Integer.valueOf(level)); break;
                    case "Rating": str = cell.toString(); String[] ss = str.split(";");
                        for (String s: ss) {
                            String[] kv = s.split("=");
                            if (kv[0].equals("AR")) {book.setArRating(Float.valueOf(kv[1]));}
                            if (kv[0].equals("Amazon")) {book.setAmazonRating(Float.valueOf(kv[1]));}
                        } break;
                    default: break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return book;
    }

    private static String generateSQL(List<Book> books) {
        StringBuilder builder = new StringBuilder();
        builder.append("begin;\n");
        for (Book book: books) {
            builder.append(combine(book));
        }
        builder.append("commit;\n");
        return builder.toString();
    }

    private static String combine(Book book) {
        Map<String, Object> kv = new HashMap<>();
        if (book.getBookName() != null) {
            kv.put("book_name", book.getBookName());
        }
        if (book.getAuthors() != null) {
            kv.put("authors", book.getAuthors());
        }
        if (book.getIsbns() != null) {
            kv.put("isbns", book.getIsbns());
        }
        if (book.getCoverUrl() != null) {
            kv.put("cover_url", book.getCoverUrl());
        }
        if (book.getSummary() != null) {
            kv.put("summary", book.getSummary());
        }
        if (book.getTopics() != null) {
            kv.put("topics", book.getTopics());
        }
        if (book.getSeries() != null) {
            kv.put("series", book.getSeries());
        }
        if (book.getIsFiction() != null) {
            kv.put("is_fiction", book.getIsFiction());
        }
        if (book.getArBl() != null) {
            kv.put("ar_bl", book.getArBl());
        }
        if (book.getArIl() != null) {
            kv.put("ar_il", book.getArIl());
        }
        if (book.getArPoints() != null) {
            kv.put("ar_points", book.getArPoints());
        }
        if (book.getArRating() != null) {
            kv.put("ar_rating", book.getArRating());
        }
        if (book.getLexilePrefix() != null) {
            kv.put("lexile_prefix", book.getLexilePrefix());
        }
        if (book.getLexile() != null) {
            kv.put("lexile", book.getLexile());
        }
        if (book.getWordcount() != null) {
            kv.put("wordcount", book.getWordcount());
        }
        if (book.getPagecount() != null) {
            kv.put("pagecount", book.getPagecount());
        }
        if (book.getAmazonRating() != null) {
            kv.put("amazon_rating", book.getAmazonRating());
        }

        StringBuilder builder = new StringBuilder();
        if (kv.size() > 0) {
            builder.append("insert into book(creator,modifier,");
            for (String column: kv.keySet()) {
                builder.append(column);
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append(") values('system','system',");
            for (Object value: kv.values()) {
                if (value instanceof String) {
                    builder.append("'");
                    value = ((String) value).replaceAll("'", "\\\\\'");
                }
                builder.append(value);
                if (value instanceof String) {
                    builder.append("'");
                }
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append(");\n");
        }
        return builder.toString();
    }

}
