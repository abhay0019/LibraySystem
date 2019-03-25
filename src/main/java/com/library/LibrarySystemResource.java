package com.library;

import org.hibernate.validator.constraints.NotEmpty;


import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/flibrary")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class LibrarySystemResource {

    @NotEmpty
    private String libraryName;

    private Helper helper;

    //TODO: ABHAY, these getters and setters are not required here!!
    //Deleted

    public LibrarySystemResource(String libraryName, Helper helper) {
        this.helper = helper;
        this.libraryName=libraryName;
    }

    /* TODO: ABHAY, 2 APIs should not be used for same purpose. 1 API for fetching all books,
        1 API for fetching a book detail from a particular param
        eg. /search/book?id={id}&name={name}
            /search/book?id={id}
            /search/book?name={name}
            all these three requests can be handled by same resource function
        REASON: Looking at the function creates confusion about how the API will be, what are the query params and so on.
    */
    //Don't have clarity exactly what to do here !!

    @GET
    @Path("/books-details")
    public List<Book> getBooks(@Context UriInfo uriInfo)
            throws IllegalAccessException {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        List<Book> books=helper.searchBooks(queryParams);
        return books;
    }

    /* TODO: ABHAY, same as above

     */

    @GET
    @Path("/users-details")
    public List<User> getUsers(@Context UriInfo uriInfo)
            throws IllegalAccessException {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        List<User> users = helper.searchUsers(queryParams);
        return users;
    }

    //TODO: ABHAY, add @Body    String addBook(@Body Book book)

    //I cannot use this annotation as this is not a generic and it is implemented by platform team only.
    //Also I would like to understand it why is it needed as without it also we are able to catch the @POST body here.

    @POST
    @Path("/add-book")
    public String addBook(Book book) throws IllegalAccessException {
        StringBuilder builder;
        MultivaluedMap<String, String> searchParams=new MultivaluedHashMap<String, String>();
        searchParams.add("bookId", String.valueOf(book.getBookId()));
        if(helper.searchBooks(searchParams).size()==0) {
            helper.addBookInLibrary(new LibraryBook(book));
            return "Added Successfully !!";
        }
        else{
            builder = new StringBuilder("BookId :").append(book.getBookId()).append(" already present!!");
            return builder.toString();
        }
    }

    //TODO: SAME
    @POST
    @Path("/add-user")
    public String addUser(User user){
        StringBuilder builder;
        if(!helper.userExists(user.getUserId())) {
            helper.addLibraryUser(user);
            return "com.library.User Added Successfully !!";
        }
        else{
            builder = new StringBuilder("UserId : ").append(user.getUserId()).append(" already present !!");
            return builder.toString();
        }
    }

    //TODO:[IMP] ABHAY, Post call should always have a body
    //Changed @POST to @GET as @POST was not needed here !!

    @GET
    @Path("/issue-book")
    public String issueBook(@DefaultValue("-1") @QueryParam("bookId") int bookId,
                            @DefaultValue("-1") @QueryParam("userId") int userId,
                            @DefaultValue("10") @QueryParam("days") int days) throws IllegalAccessException {
        if(userId==-1 || !helper.userExists(userId)){
            return "Please Specify Valid UserId !!";
        }
        if(bookId==-1){
            return "Please Specify Valid BookId !!";
        }

        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<String, String>();
        queryParams.add("bookId", String.valueOf(bookId));
        List<Book> books=helper.searchBooks(queryParams);
        StringBuilder builder;
        if(books.size()==0){
            return "Sorry, com.library.Book Not Available !!";
        }
        else{
            if(books.size()!=1){
                return "Please specify book by Unique bookId param !!";
            }
            else {
                if(((LibraryBook)books.get(0)).getIsIssued()){
                    return "Sorry, the book is aready issued !!";
                }
                else{
                    LibraryBook libraryBook = (LibraryBook) books.get(0);
                    libraryBook.issueBookToUser(userId, days);
                    builder=new StringBuilder("Issued com.library.Book to com.library.User").append(userId);
                    return builder.toString();
                    //TODO: ABHAY, concatinating strings with '+' is heavy. Read about other ways of doing so.
                    //Using StringBuilder now which is suggested to be the best way to concatenate string as of now.
                }
            }
        }
    }

    @POST
    @Path("/return-book")
    public String returnBook(@DefaultValue("-1") @QueryParam("bookId") int bookId) throws IllegalAccessException {
        if(bookId==-1){
            return "Please provide a valid bookId !!";
        }
        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<String, String>();
        StringBuilder builder;
        queryParams.add("bookId", String.valueOf(bookId));
        List<Book> books=helper.searchBooks(queryParams);

        if(books.size()==0){
            return "Sorry, the book is not of our library !!";
        }
        else{
            if(books.size()!=1){
                return "Please specify book by Unique bookId param !!";
            }
            else {
                LibraryBook libraryBook = (LibraryBook) books.get(0);
                if(libraryBook.getIsIssued()){
                    //TODO: ABHAY, there should be separate method for fine computation and separate method for resetting the book attributes.
                    //Separated the methods for fine computation and reseting values!!

                    //TODO: it is a bad practice to pass an object to a method and modify the same object.
                    //Doing the updates but still want to know why so?
                    // Implementing a reset function in libraryBook class itself !!

                    int fine = helper.getFine(libraryBook);
                    libraryBook.resetIssueParamters();
                    builder=new StringBuilder("com.library.Book ").append(libraryBook.getBookId())
                            .append(" returned succesfully with a fine of ").append(fine);
                    return builder.toString();
                }
                else{
                    return "com.library.Book not issued to this user. A fraud !!";
                }
            }
        }
    }
}
