import java.io.Serializable;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String phone;
    private double totalFinePaid;

    public Member(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public double getTotalFinePaid() { return totalFinePaid; }
    public void addFine(double amount) { totalFinePaid += amount; }

    @Override
    public String toString() {
        return String.format("%-5d %-25s %-12s Fine paid: Rs.%.2f", id, name, phone, totalFinePaid);
    }
}
