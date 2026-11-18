import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private String author;
    private String category;
    private int totalCopies;
    private int availableCopies;

    public Book(int id, String title, String author, String category, int copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalCopies = copies;
        this.availableCopies = copies;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    public boolean isAvailable() { return availableCopies > 0; }
    public void issueCopy() { availableCopies--; }
    public void returnCopy() { availableCopies++; }
    public void addCopies(int n) { totalCopies += n; availableCopies += n; }

    @Override
    public String toString() {
        return String.format("%-5d %-30s %-20s %-12s %d/%d",
                id, title, author, category, availableCopies, totalCopies);
    }
}
