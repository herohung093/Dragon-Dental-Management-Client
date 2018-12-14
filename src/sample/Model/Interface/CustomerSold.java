package sample.Model.Interface;

public class CustomerSold {
    String product;
    int quantity;

    public CustomerSold() {
    }

    public CustomerSold(String product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CustomerSold{" +
                "product='" + product + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
