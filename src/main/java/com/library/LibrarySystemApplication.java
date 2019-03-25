package com.library;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class LibrarySystemApplication extends Application<LibrarySystemConfigration> {
    public static void main(String[] args) throws Exception {
        new LibrarySystemApplication().run(args);
    }
    public void run(LibrarySystemConfigration confiration, Environment environment) throws Exception {
        LibrarySystemResource resource=new LibrarySystemResource(confiration.getLibraryName(), new Helper());
        resource.addBook(new Book(1,"A","B",100));
        resource.addBook(new Book(2,"A","B",100));
        resource.addBook(new Book(3,"A","B",100));
        resource.addBook(new Book(4,"A","B",100));

        resource.addUser(new User(1,"A",'M',9999999999L));
        resource.addUser(new User(2,"B",'F',2111111111L));
        resource.addUser(new User(3,"C",'F',1111111111L));
        resource.addUser(new User(4,"D",'M',111111111L));

        environment.jersey().register(resource);
    }
}
