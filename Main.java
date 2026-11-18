import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    public static void main(String[] args) {
        Library lib = Library.load();
        System.out.println("=====================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM (Java)  ");
        System.out.println("=====================================");

        if (!login()) {
            System.out.println("Too many failed attempts. Exiting.");
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addBook(lib);
                case 2 -> removeBook(lib);
                case 3 -> viewBooks(lib);
                case 4 -> searchBooks(lib);
                case 5 -> addMember(lib);
                case 6 -> viewMembers(lib);
                case 7 -> issueBook(lib);
                case 8 -> returnBook(lib);
                case 9 -> viewList("Currently Issued Books", lib.getIssuedBooks());
                case 10 -> viewList("Overdue Books", lib.getOverdueBooks());
                case 11 -> viewList("All Transactions", lib.getAllTransactions());
                case 0 -> {
                    lib.save();
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
            if (running) lib.save(); // auto-save after every action
        }
    }

    private static boolean login() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.print("Username: ");
            String u = sc.nextLine().trim();
            System.out.print("Password: ");
            String p = sc.nextLine().trim();
            if (u.equals(ADMIN_USER) && p.equals(ADMIN_PASS)) {
                System.out.println("Login successful.\n");
                return true;
            }
            System.out.println("Wrong credentials. Attempts left: " + (3 - attempt));
        }
        return false;
    }

    private static void printMenu() {
        System.out.println("\n----------- MAIN MENU -----------");
        System.out.println(" 1. Add Book");
        System.out.println(" 2. Remove Book");
        System.out.println(" 3. View All Books");
        System.out.println(" 4. Search Books");
        System.out.println(" 5. Register Member");
        System.out.println(" 6. View All Members");
        System.out.println(" 7. Issue Book");
        System.out.println(" 8. Return Book");
        System.out.println(" 9. View Issued Books");
        System.out.println("10. View Overdue Books");
        System.out.println("11. View Transaction History");
        System.out.println(" 0. Save & Exit");
        System.out.println("---------------------------------");
    }

    private static void addBook(Library lib) {
        String title = readString("Title: ");
        String author = readString("Author: ");
        String category = readString("Category: ");
        int copies = readInt("Number of copies: ");
        if (copies <= 0) { System.out.println("Copies must be positive."); return; }
        Book b = lib.addBook(title, author, category, copies);
        System.out.println("Book saved with ID " + b.getId());
    }

    private static void removeBook(Library lib) {
        int id = readInt("Book ID to remove: ");
        System.out.println(lib.removeBook(id)
                ? "Book removed."
                : "Cannot remove: book not found or copies are still issued.");
    }

    private static void viewBooks(Library lib) {
        if (lib.getAllBooks().isEmpty()) { System.out.println("No books in library."); return; }
        System.out.printf("%-5s %-30s %-20s %-12s %s%n", "ID", "Title", "Author", "Category", "Avail/Total");
        lib.getAllBooks().forEach(System.out::println);
    }

    private static void searchBooks(Library lib) {
        String k = readString("Search (title/author/category): ");
        List<Book> result = lib.searchBooks(k);
        if (result.isEmpty()) System.out.println("No matching books.");
        else result.forEach(System.out::println);
    }

    private static void addMember(Library lib) {
        String name = readString("Member name: ");
        String phone = readString("Phone: ");
        Member m = lib.addMember(name, phone);
        System.out.println("Member registered with ID " + m.getId());
    }

    private static void viewMembers(Library lib) {
        if (lib.getAllMembers().isEmpty()) { System.out.println("No members."); return; }
        lib.getAllMembers().forEach(System.out::println);
    }

    private static void issueBook(Library lib) {
        int bid = readInt("Book ID: ");
        int mid = readInt("Member ID: ");
        System.out.println(lib.issueBook(bid, mid));
    }

    private static void returnBook(Library lib) {
        int bid = readInt("Book ID: ");
        int mid = readInt("Member ID: ");
        System.out.println(lib.returnBook(bid, mid));
    }

    private static void viewList(String title, List<Transaction> list) {
        System.out.println("--- " + title + " ---");
        if (list.isEmpty()) System.out.println("(none)");
        else list.forEach(System.out::println);
    }

    // ---------- input helpers ----------
    private static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
