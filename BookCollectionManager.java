
import java.io.*;
import java.util.*;

class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    int id;
    String title;
    String author;
    String genre;
    int totalCopies;
    int availableCopies;
    String readingStatus;

    Book(int id, String title, String author, String genre, int copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.totalCopies = copies;
        this.availableCopies = copies;
        this.readingStatus = "To Read";
    }

    public void display() {
        System.out.println("--------------------------------");
        System.out.println("Book ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Genre: " + genre);
        System.out.println("Total Copies: " + totalCopies);
        System.out.println("Available Copies: " + availableCopies);
        System.out.println("Reading Status: " + readingStatus);
        System.out.println("--------------------------------");
    }
}

public class BookCollectionManager {

    static ArrayList<Book> books = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    static final String FILE_NAME = "books.dat";

    // Load books from file
    static void loadBooks() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            books = (ArrayList<Book>) in.readObject();
            System.out.println("Book data loaded successfully!");

        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading book data: " + e.getMessage());
        }
    }

    // Save books to file
    static void saveBooks() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(books);
            System.out.println("Book data saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Find book using ID
    static Book findBook(int id) {
        for (Book b : books) {
            if (b.id == id) {
                return b;
            }
        }
        return null;
    }

    // Add a new book
    static void addBook() {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (findBook(id) != null) {
            System.out.println("Book ID already exists!");
            return;
        }

        System.out.print("Enter Book Title: ");
        String title = sc.nextLine();

        System.out.print("Enter Author Name: ");
        String author = sc.nextLine();

        System.out.print("Enter Genre: ");
        String genre = sc.nextLine();

        System.out.print("Enter Number of Copies: ");
        int copies = sc.nextInt();
        sc.nextLine();

        if (copies <= 0) {
            System.out.println("Copies must be greater than zero!");
            return;
        }

        books.add(new Book(id, title, author, genre, copies));

        saveBooks();
        System.out.println("Book added successfully!");
    }

    // View all books
    static void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available!");
            return;
        }

        System.out.println("\n===== BOOK COLLECTION =====");

        for (Book b : books) {
            b.display();
        }
    }

    // Search books
    static void searchBook() {
        System.out.println("\n1. Search by ID");
        System.out.println("2. Search by Title");
        System.out.println("3. Search by Author");
        System.out.print("Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        boolean found = false;

        switch (choice) {
            case 1:
                System.out.print("Enter Book ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                Book b = findBook(id);

                if (b != null) {
                    b.display();
                    found = true;
                }
                break;

            case 2:
                System.out.print("Enter Book Title: ");
                String title = sc.nextLine();

                for (Book book : books) {
                    if (book.title.toLowerCase()
                            .contains(title.toLowerCase())) {
                        book.display();
                        found = true;
                    }
                }
                break;

            case 3:
                System.out.print("Enter Author Name: ");
                String author = sc.nextLine();

                for (Book book : books) {
                    if (book.author.toLowerCase()
                            .contains(author.toLowerCase())) {
                        book.display();
                        found = true;
                    }
                }
                break;

            default:
                System.out.println("Invalid choice!");
                return;
        }

        if (!found) {
            System.out.println("Book not found!");
        }
    }

    // Update book details
    static void updateBook() {
        System.out.print("Enter Book ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        Book b = findBook(id);

        if (b == null) {
            System.out.println("Book not found!");
            return;
        }

        System.out.println("1. Update Title");
        System.out.println("2. Update Author");
        System.out.println("3. Update Genre");
        System.out.println("4. Update Total Copies");

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter New Title: ");
                b.title = sc.nextLine();
                break;

            case 2:
                System.out.print("Enter New Author: ");
                b.author = sc.nextLine();
                break;

            case 3:
                System.out.print("Enter New Genre: ");
                b.genre = sc.nextLine();
                break;

            case 4:
                System.out.print("Enter New Total Copies: ");
                int newCopies = sc.nextInt();
                sc.nextLine();

                int issuedCopies = b.totalCopies - b.availableCopies;

                if (newCopies < issuedCopies || newCopies <= 0) {
                    System.out.println("Invalid number of copies!");
                    return;
                }

                b.totalCopies = newCopies;
                b.availableCopies = newCopies - issuedCopies;
                break;

            default:
                System.out.println("Invalid choice!");
                return;
        }

        saveBooks();
        System.out.println("Book updated successfully!");
    }

    // Delete a book
    static void deleteBook() {
        System.out.print("Enter Book ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        Book b = findBook(id);

        if (b == null) {
            System.out.println("Book not found!");
            return;
        }

        if (b.availableCopies != b.totalCopies) {
            System.out.println("Cannot delete! Some copies are issued.");
            return;
        }

        books.remove(b);
        saveBooks();

        System.out.println("Book deleted successfully!");
    }

    // Sort books
    static void sortBooks() {
        System.out.println("\n1. Sort by ID");
        System.out.println("2. Sort by Title");
        System.out.println("3. Sort by Author");

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                books.sort(Comparator.comparingInt(b -> b.id));
                break;

            case 2:
                books.sort(Comparator.comparing(
                        b -> b.title.toLowerCase()));
                break;

            case 3:
                books.sort(Comparator.comparing(
                        b -> b.author.toLowerCase()));
                break;

            default:
                System.out.println("Invalid choice!");
                return;
        }

        System.out.println("Books sorted successfully!");
        viewBooks();
    }

    // Issue a book
    static void issueBook() {
        System.out.print("Enter Book ID to issue: ");
        int id = sc.nextInt();
        sc.nextLine();

        Book b = findBook(id);

        if (b == null) {
            System.out.println("Book not found!");
            return;
        }

        if (b.availableCopies > 0) {
            b.availableCopies--;
            saveBooks();
            System.out.println("Book issued successfully!");
        } else {
            System.out.println("No copies available!");
        }
    }

    // Return a book
    static void returnBook() {
        System.out.print("Enter Book ID to return: ");
        int id = sc.nextInt();
        sc.nextLine();

        Book b = findBook(id);

        if (b == null) {
            System.out.println("Book not found!");
            return;
        }

        if (b.availableCopies < b.totalCopies) {
            b.availableCopies++;
            saveBooks();
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("No issued copies to return!");
        }
    }

    // Update reading status
    static void updateReadingStatus() {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Book b = findBook(id);

        if (b == null) {
            System.out.println("Book not found!");
            return;
        }

        System.out.println("1. To Read");
        System.out.println("2. Reading");
        System.out.println("3. Completed");

        System.out.print("Select Reading Status: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                b.readingStatus = "To Read";
                break;

            case 2:
                b.readingStatus = "Reading";
                break;

            case 3:
                b.readingStatus = "Completed";
                break;

            default:
                System.out.println("Invalid choice!");
                return;
        }

        saveBooks();
        System.out.println("Reading status updated!");
    }

    // Main menu
    public static void main(String[] args) {

        loadBooks();

        int choice;

        do {
            System.out.println("\n================================");
            System.out.println("     BOOK COLLECTION MANAGER");
            System.out.println("================================");

            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Book");
            System.out.println("4. Update Book Details");
            System.out.println("5. Delete Book");
            System.out.println("6. Sort Books");
            System.out.println("7. Issue Book");
            System.out.println("8. Return Book");
            System.out.println("9. Update Reading Status");
            System.out.println("10. Save Data");
            System.out.println("0. Exit");

            System.out.println("================================");
            System.out.print("Enter Your Choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addBook();
                    break;

                case 2:
                    viewBooks();
                    break;

                case 3:
                    searchBook();
                    break;

                case 4:
                    updateBook();
                    break;

                case 5:
                    deleteBook();
                    break;

                case 6:
                    sortBooks();
                    break;

                case 7:
                    issueBook();
                    break;

                case 8:
                    returnBook();
                    break;

                case 9:
                    updateReadingStatus();
                    break;

                case 10:
                    saveBooks();
                    break;

                case 0:
                    saveBooks();
                    System.out.println("Thank you for using Book Manager!");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }

        } while (choice != 0);

        sc.close();
    }
}
