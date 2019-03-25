package com.library;

import java.util.ArrayList;
import java.util.List;

public class LibraryUser extends User {
    private List<Integer>bookIds=new ArrayList<Integer>();

    public LibraryUser(int userId, String usedName, char userGender, long mobileNo) {
        super(userId, usedName, userGender, mobileNo);
    }

    public List<Integer> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Integer> bookIds) {
        this.bookIds = bookIds;
    }
}
