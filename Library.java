import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Library implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "library.dat";
    private static final int MAX_BOOKS_PER_MEMBER = 3;

    private Map<Integer, Book> books = new LinkedHashMap<>();
    private Map<Integer, Member> members = new LinkedHashMap<>();
    private List<Transaction> transactions = new ArrayList<>();
    private int nextBookId = 101;
    private int nextMemberId = 1001;
    private int nextTxnId = 1;

    // ---------- Book operations ----------
    public Book addBook(String title, String author, String category, int copies) {
        for (Book b : books.values()) {
            if (b.getTitle().equalsIgnoreCase(title) && b.getAuthor().equalsIgnoreCase(author)) {
                b.addCopies(copies);
                return b;
            }
        }
        Book b = new Book(nextBookId++, title, author, category, copies);
        books.put(b.getId(), b);
        return b;
    }

    public boolean removeBook(int id) {
        Book b = books.get(id);
        if (b == null) return false;
        if (b.getAvailableCopies() != b.getTotalCopies()) return false; // some copies still issued
        books.remove(id);
        return true;
    }

    public List<Book> searchBooks(String keyword) {
        String k = keyword.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(k)
                        || b.getAuthor().toLowerCase().contains(k)
                        || b.getCategory().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    public Collection<Book> getAllBooks() { return books.values(); }

    // ---------- Member operations ----------
    public Member addMember(String name, String phone) {
        Member m = new Member(nextMemberId++, name, phone);
        members.put(m.getId(), m);
        return m;
    }

    public Collection<Member> getAllMembers() { return members.values(); }

    // ---------- Issue / Return ----------
    public String issueBook(int bookId, int memberId) {
        Book b = books.get(bookId);
        Member m = members.get(memberId);
        if (b == null) return "Book not found.";
        if (m == null) return "Member not found.";
        if (!b.isAvailable()) return "No copies available right now.";
        if (activeIssuesOf(memberId).size() >= MAX_BOOKS_PER_MEMBER)
            return "Member has reached the limit of " + MAX_BOOKS_PER_MEMBER + " books.";
        for (Transaction t : activeIssuesOf(memberId))
            if (t.getBookId() == bookId) return "Member already has this book.";

        b.issueCopy();
        Transaction t = new Transaction(nextTxnId++, bookId, memberId, LocalDate.now());
        transactions.add(t);
        return "Issued '" + b.getTitle() + "' to " + m.getName() + ". Due date: " + t.getDueDate();
    }

    public String returnBook(int bookId, int memberId) {
        return returnBook(bookId, memberId, LocalDate.now());
    }

    public String returnBook(int bookId, int memberId, LocalDate returnDate) {
        for (Transaction t : transactions) {
            if (!t.isReturned() && t.getBookId() == bookId && t.getMemberId() == memberId) {
                double fine = t.closeTransaction(returnDate);
                books.get(bookId).returnCopy();
                members.get(memberId).addFine(fine);
                return fine > 0
                        ? "Book returned late. Fine: Rs." + fine
                        : "Book returned on time. No fine.";
            }
        }
        return "No active issue record found for this book and member.";
    }

    public List<Transaction> activeIssuesOf(int memberId) {
        return transactions.stream()
                .filter(t -> !t.isReturned() && t.getMemberId() == memberId)
                .collect(Collectors.toList());
    }

    public List<Transaction> getIssuedBooks() {
        return transactions.stream().filter(t -> !t.isReturned()).collect(Collectors.toList());
    }

    public List<Transaction> getOverdueBooks() {
        LocalDate today = LocalDate.now();
        return transactions.stream()
                .filter(t -> !t.isReturned() && t.getDueDate().isBefore(today))
                .collect(Collectors.toList());
    }

    public List<Transaction> getAllTransactions() { return transactions; }

    // ---------- Persistence ----------
    public void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static Library load() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return new Library();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (Library) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not load saved data, starting fresh.");
            return new Library();
        }
    }
}
