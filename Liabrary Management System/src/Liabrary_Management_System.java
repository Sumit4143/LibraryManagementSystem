// LibraryManagementSystem.java
// Complete Library Management System in a single file

import java.util.ArrayList;
import java.util.Scanner;

// Book class - Represents a book in the library
class Book {
    private String title;
    private String author;
    private boolean isAvailable;

    // Constructor
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Set availability
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    // Display book details
    public void displayInfo() {
        System.out.printf("Title: %s | Author: %s | %s\n", title, author, (isAvailable ? "Available" : "Issued"));
    }
}

// User class - Represents a user in the library system
class User {
    private String username;
    private ArrayList<Book> issuedBooks;
    private static final int MAX_ISSUED_BOOKS = 4;

    // Constructor
    public User(String username) {
        this.username = username;
        this.issuedBooks = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Book> getIssuedBooks() {
        return issuedBooks;
    }

    // Issue a book to the user
    public boolean issueBook(Book book) {
        if (issuedBooks.size() < MAX_ISSUED_BOOKS) {
            issuedBooks.add(book);
            return true;
        }
        return false;
    }

    // Return a book from the user
    public boolean returnBook(Book book) {
        return issuedBooks.remove(book);
    }

    // Check if user has issued a specific book
    public boolean hasBook(Book book) {
        return issuedBooks.contains(book);
    }

    // Display issued books
    public void displayIssuedBooks() {
        if (issuedBooks.isEmpty()) {
            System.out.println("No books issued.");
        } else {
            System.out.println("Issued Books:");
            for (Book book : issuedBooks) {
                book.displayInfo();
            }
        }
    }
}

// Library class - Manages the book catalog and user list
class Library {
    private ArrayList<Book> books;
    private ArrayList<User> users;

    public Library() {
        books = new ArrayList<>();
        users = new ArrayList<>();
    }

    // Add a book to the catalog
    public void addBook(Book book) {
        books.add(book);
    }

    // Add a user to the system
    public void addUser(User user) {
        users.add(user);
    }

    // Find a user by username
    public User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    // Find a book by title
    public Book findBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    // Display all books
    public void displayAllBooks() {
        System.out.println("\nBook Catalog:");
        for (Book book : books) {
            book.displayInfo();
        }
    }

    // Display only available books
    public void displayAvailableBooks() {
        System.out.println("\nAvailable Books:");
        boolean found = false;
        for (Book book : books) {
            if (book.isAvailable()) {
                book.displayInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books are currently available.");
        }
    }
}

// Main class - LibraryManagementSystem
public class LibraryManagementSystem {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSampleData();
        System.out.println("Welcome to the Library Management System!");
        while (true) {
            System.out.print("\nEnter your username (or type 'exit' to quit): ");
            String username = scanner.nextLine().trim();
            if (username.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }
            User user = library.findUser(username);
            if (user == null) {
                user = new User(username);
                library.addUser(user);
                System.out.println("New user created: " + username);
            } else {
                System.out.println("Welcome back, " + username + "!");
            }
            userMenu(user);
        }
    }

    // Menu for each user session
    private static void userMenu(User user) {
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. View all books");
            System.out.println("2. View available books");
            System.out.println("3. Issue a book");
            System.out.println("4. Return a book");
            System.out.println("5. View my issued books");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    library.displayAllBooks();
                    break;
                case "2":
                    library.displayAvailableBooks();
                    break;
                case "3":
                    issueBook(user);
                    break;
                case "4":
                    returnBook(user);
                    break;
                case "5":
                    user.displayIssuedBooks();
                    break;
                case "6":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Issue a book to the user
    private static void issueBook(User user) {
        if (user.getIssuedBooks().size() >= 4) {
            System.out.println("You have already issued the maximum number of books (4). Return a book to issue a new one.");
            return;
        }
        library.displayAvailableBooks();
        System.out.print("Enter the title of the book to issue: ");
        String title = scanner.nextLine().trim();
        Book book = library.findBook(title);
        if (book == null) {
            System.out.println("Book not found.");
        } else if (!book.isAvailable()) {
            System.out.println("Book is currently issued by another user.");
        } else {
            if (user.issueBook(book)) {
                book.setAvailable(false);
                System.out.println("Book issued successfully!");
            } else {
                System.out.println("Failed to issue book. You may have reached your limit.");
            }
        }
    }

    // Return a book from the user
    private static void returnBook(User user) {
        if (user.getIssuedBooks().isEmpty()) {
            System.out.println("You have no books to return.");
            return;
        }
        user.displayIssuedBooks();
        System.out.print("Enter the title of the book to return: ");
        String title = scanner.nextLine().trim();
        Book book = library.findBook(title);
        if (book == null || !user.hasBook(book)) {
            System.out.println("You have not issued this book.");
        } else {
            if (user.returnBook(book)) {
                book.setAvailable(true);
                System.out.println("Book returned successfully!");
            } else {
                System.out.println("Failed to return book.");
            }
        }
    }

    // Initialize the library with some sample books
    private static void initializeSampleData() {
        library.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
        library.addBook(new Book("To Kill a Mockingbird", "Harper Lee"));
        library.addBook(new Book("1984", "George Orwell"));
        library.addBook(new Book("Pride and Prejudice", "Jane Austen"));
        library.addBook(new Book("Moby Dick", "Herman Melville"));
    }
}
