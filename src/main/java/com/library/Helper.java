package com.library;

import org.hibernate.validator.constraints.NotEmpty;

import javax.ws.rs.core.MultivaluedMap;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    private List<LibraryBook> booksInLibrary=new ArrayList<LibraryBook>();

    private List<User> libraryUser= new ArrayList<User>();

    public void addBookInLibrary(LibraryBook book){
        booksInLibrary.add(book);
    }

    public void addLibraryUser(User user){
        libraryUser.add(user);
    }

    public int getFine(LibraryBook libraryBook) {
        int fine=0;
        if(libraryBook.isDelayed()){
            fine=libraryBook.getDelayFine();
        }
        return fine;
    }

    public boolean userExists(int userId) {
        for(User u:libraryUser){
            if(u.getUserId()==userId){
                return true;
            }
        }
        return false;
    }

    //TODO: ABHAY, search operation can be simplified.
    // Maintain a global list of object(user/book) and simply search in that list with the inputted param
    // I have already declared global variables and doing a simple search!!

    //TODO: ABHAY, try implementing the same using java8 constructs
    //Updated it.

    //TODO: ABHAY, try giving meaningful variable names
    //  :/

    public boolean findFieldMatch(MultivaluedMap<String, String> queryParams, User user){
        List<Field> fields = Arrays.asList(new User().getClass().getDeclaredFields());
        int count = 0;
        for (Field f : fields) {
            List<String> values = queryParams.get(f.getName());
            if(values!=null && values.size()!=0){
                if(values.get(0).equalsIgnoreCase(user.findValue(f.getName()))){
                    count++;
                }
            }
        }
        return count == queryParams.size();
    }

    public boolean findFieldMatch(MultivaluedMap<String, String> queryParams, Book book){
        List<Field> fields = Arrays.asList(new Book().getClass().getDeclaredFields());
        int count = 0;
        for (Field f : fields) {
            List<String> values = queryParams.get(f.getName());
            if(values!=null && values.size()!=0){
                if(values.get(0).equalsIgnoreCase(book.findValue(f.getName()))){
                    count++;
                }
            }
        }
        return count == queryParams.size();
    }

    public List<User> searchUsers(MultivaluedMap<String, String> queryParams){
        return libraryUser.stream().
                filter(user-> findFieldMatch(queryParams,user)).
                collect(Collectors.toList());
    }

    public List<Book> searchBooks(MultivaluedMap<String, String> queryParams) throws IllegalAccessException {
        return booksInLibrary.stream().
                filter(book->findFieldMatch(queryParams,book)).
                collect(Collectors.toList());
    }
}
