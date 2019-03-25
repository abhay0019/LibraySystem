package com.library;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LibraryBook extends Book {

    private boolean isIssued=false;
    private int userId=-1;
    private int delayFine=100;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private Date issueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private Date dueDate;
    private int maxDelayDaysAllowed=10;

    public LibraryBook(int bookId, String bookName, String bookAuthor,
                       int bookCost, boolean isIssued, int userId,
                       Date issueDate, Date dueDate, int maxDelayDaysAllowed, int delayFine) {
        super(bookId, bookName, bookAuthor, bookCost);
        this.isIssued = isIssued;
        this.userId = userId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.maxDelayDaysAllowed = maxDelayDaysAllowed;
        this.delayFine=delayFine;
    }

    public LibraryBook(){
        super();
    }

    public LibraryBook(int bookId, String bookName, String bookAuthor, int bookCost){
        super(bookId, bookName, bookAuthor, bookCost);
    }

    public LibraryBook(Book book){
        super(book.getBookId(), book.getBookName(), book.getBookAuthor(), book.getBookCost());
    }

    public int getDelayFine() {
        return delayFine;
    }

    public void setDelayFine(int delayFine) {
        this.delayFine = delayFine;
    }

    public boolean getIsIssued() {
        return isIssued;
    }

    public void setIssued(boolean issued) {
        isIssued = issued;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getMaxDelayDaysAllowed() {
        return maxDelayDaysAllowed;
    }

    public void setMaxDelayDaysAllowed(int maxDelayDaysAllowed) {
        this.maxDelayDaysAllowed = maxDelayDaysAllowed;
    }

    boolean isDelayed(){
        Date today=new Date();
        long days=getDifferenceDays(dueDate, today);
        if(days>maxDelayDaysAllowed){
            return true;
        }
        else{
            return false;
        }
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString() {
        return "com.library.LibraryBook{" +
                "isIssued=" + isIssued +
                ", userId=" + userId +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", maxDelayDaysAllowed=" + maxDelayDaysAllowed +
                '}';
    }
}
