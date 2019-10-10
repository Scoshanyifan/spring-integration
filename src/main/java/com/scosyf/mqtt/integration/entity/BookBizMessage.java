package com.scosyf.mqtt.integration.entity;

/**
 * @project: spring-integration
 * @author: kunbu
 * @create: 2019-10-10 16:34
 **/
public class BookBizMessage extends BizMessage {

    private String bookName;
    private int bookYear;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getBookYear() {
        return bookYear;
    }

    public void setBookYear(int bookYear) {
        this.bookYear = bookYear;
    }
}
