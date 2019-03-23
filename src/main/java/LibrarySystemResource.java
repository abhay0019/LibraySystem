import org.hibernate.validator.constraints.NotEmpty;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.lang.reflect.Field;
import java.util.*;

@Path("/flibrary")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class LibrarySystemResource {

    private List<LibraryBook> booksInLibrary=new ArrayList<LibraryBook>();

    private List<User> libraryUser= new ArrayList<User>();

    @NotEmpty
    private String libraryName;

    public List<LibraryBook> getBooksInLibrary() {
        return booksInLibrary;
    }

    public void setBooksInLibrary(List<LibraryBook> booksInLibrary) {
        this.booksInLibrary = booksInLibrary;
    }

    public List<User> getLibraryUser() {
        return libraryUser;
    }

    public void setLibraryUser(List<User> libraryUser) {
        this.libraryUser = libraryUser;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public LibrarySystemResource(String libraryName) {
        this.libraryName=libraryName;
    }

    @GET
    @Path("/books-details")
    public List<Book> getBooks(@Context UriInfo uriInfo)
            throws IllegalAccessException {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        List<Book> books=searchBooks(queryParams);
        return books;
    }

    @GET
    @Path("/users-details")
    public List<User> getUsers(@Context UriInfo uriInfo)
            throws IllegalAccessException {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        List<User> users = searchUsers(queryParams);
        return users;
    }

    @POST
    @Path("/add-book")
    public String addBook(Book book) throws IllegalAccessException {
        MultivaluedMap<String, String> searchParams=new MultivaluedHashMap<String, String>();
        searchParams.add("bookId", String.valueOf(book.getBookId()));
        if(searchBooks(searchParams).size()==0) {
            booksInLibrary.add(new LibraryBook(book));
            return "Added Successfully !!";
        }
        else{
            return "BookId : "+book.getBookId()+" already present !!";
        }
    }

    @POST
    @Path("/add-user")
    public String addUser(User user) throws IllegalAccessException {
        if(!userExists(user.getUserId())) {
            libraryUser.add(user);
            return "User Added Successfully !!";
        }
        else{
            return "UserId : "+user.getUserId()+" already present !!";
        }
    }

    @POST
    @Path("/issue-book")
    public String issueBook(@DefaultValue("-1") @QueryParam("bookId") int bookId,
                            @DefaultValue("-1") @QueryParam("userId") int userId,
                            @DefaultValue("10") @QueryParam("days") int days) throws IllegalAccessException {
        if(userId==-1 || !userExists(userId)){
            return "Please Specify Valid UserId !!";
        }
        if(bookId==-1){
            return "Please Specify Valid BookId !!";
        }

        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<String, String>();
        queryParams.add("bookId", String.valueOf(bookId));
        List<Book> books=searchBooks(queryParams);
        if(books.size()==0){
            return "Sorry, Book Not Available !!";
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
                    issueBookToUser(libraryBook, userId, days);
                    return "Issued Book to User "+String.valueOf(userId);
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
        queryParams.add("bookId", String.valueOf(bookId));
        List<Book> books=searchBooks(queryParams);
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
                    int fine = returnBookFromUserWithFine(libraryBook);
                    return "Book "+libraryBook.getBookId()+" returned succesfully with a fine of "+fine;
                }
                else{
                    return "Book not issued to this user. A fraud !!";
                }
            }
        }
    }

    private int returnBookFromUserWithFine(LibraryBook libraryBook) {

        int fine=0;
        libraryBook.setIssued(false);
        libraryBook.setUserId(-1);
        Date due=libraryBook.getDueDate();
        if(libraryBook.isDelayed()){
            fine=libraryBook.getDelayFine();
        }
        libraryBook.setIssueDate(null);
        libraryBook.setDueDate(null);
        return fine;
    }


    private boolean userExists(int userId) {
        for(User u:libraryUser){
            if(u.getUserId()==userId){
                return true;
            }
        }
        return false;
    }


    private void issueBookToUser(LibraryBook libraryBook, int userId, int days) {
        Calendar cal = new GregorianCalendar();
        Date issue=cal.getTime();
        cal.add(Calendar.DATE, days);
        Date due=cal.getTime();

        libraryBook.setIssued(true);
        libraryBook.setUserId(userId);
        libraryBook.setIssueDate(issue);
        libraryBook.setDueDate(due);

    }

    private List<User> searchUsers(MultivaluedMap<String, String> queryParams) throws IllegalAccessException {
        List<User> list = new ArrayList<User>();
        Field[] fields=new User().getClass().getDeclaredFields();
        for(User u:libraryUser) {
            int count = 0;
            for (Field f : fields) {
                List<String> values = queryParams.get(f.getName());
                if(values!=null && values.size()!=0){
                    if(values.get(0).equalsIgnoreCase(u.findValue(f.getName()))){
                        count++;
                    }
                }
            }
            if (count == queryParams.size()) {
                list.add(u);
            }
        }
        return list;
    }

    private List<Book> searchBooks(MultivaluedMap<String, String> queryParams) throws IllegalAccessException {
        List<Book> list = new ArrayList<Book>();
        Field[] fields=new Book().getClass().getDeclaredFields();
        for(Book b:booksInLibrary) {g
            int count = 0;
            for (Field f : fields) {
                List<String> values = queryParams.get(f.getName());
                if(values!=null && values.size()!=0){
                    if(values.get(0).equalsIgnoreCase(b.findValue(f.getName()))){
                        count++;
                    }
                }
            }
            if (count == queryParams.size()) {
                list.add(b);
            }
        }
        return list;
    }
}
