import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int LOAN_DAYS = 14;
    public static final double FINE_PER_DAY = 2.0;

    private int id;
    private int bookId;
    private int memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;

    public Transaction(int id, int bookId, int memberId, LocalDate issueDate) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = issueDate.plusDays(LOAN_DAYS);
    }

    public int getId() { return id; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getFine() { return fine; }
    public boolean isReturned() { return returnDate != null; }

    /** Closes the transaction and returns the fine (0 if on time). */
    public double closeTransaction(LocalDate date) {
        this.returnDate = date;
        long lateDays = ChronoUnit.DAYS.between(dueDate, date);
        this.fine = lateDays > 0 ? lateDays * FINE_PER_DAY : 0;
        return fine;
    }

    @Override
    public String toString() {
        return String.format("Txn#%-4d Book:%-4d Member:%-4d Issued:%s Due:%s %s",
                id, bookId, memberId, issueDate, dueDate,
                isReturned() ? "Returned:" + returnDate + " Fine:Rs." + fine : "[ISSUED]");
    }
}
