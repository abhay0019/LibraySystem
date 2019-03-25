package com.library;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.lang.reflect.Field;

public class Book {


    @NotEmpty
    private Integer bookId;
    @NotEmpty
    private String bookName;
    @NotEmpty
    private String bookAuthor;
    @NotEmpty
    private Integer bookCost;

    public Book() {
        bookId=-1;
        bookName="";
        bookAuthor="";
        bookCost=-1;
    }

    public Book(int bookId, String bookName, String bookAuthor, int bookCost) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookCost = bookCost;
    }

    @JsonProperty
    public int getBookId() {
        return bookId;
    }

    @JsonProperty
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    @JsonProperty
    public String getBookName() {
        return bookName;
    }

    @JsonProperty
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @JsonProperty
    public String getBookAuthor() {
        return bookAuthor;
    }

    @JsonProperty
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    @JsonProperty
    public int getBookCost() {
        return bookCost;
    }

    @JsonProperty
    public void setBookCost(int bookCost) {
        this.bookCost = bookCost;
    }

    public String findValue(String searchField) throws IllegalAccessException {
        Field[] fields = Book.class.getDeclaredFields();
        for(Field f : fields){
            if(searchField == f.getName()) {
                return f.get(this).toString();
            }
        }
        return null;
    }
}
