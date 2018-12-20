package sample.Model;

public class GroupOrderLine {
    String name;
    int quantity;

    public GroupOrderLine() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public GroupOrderLine(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "GroupOrderLine{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
