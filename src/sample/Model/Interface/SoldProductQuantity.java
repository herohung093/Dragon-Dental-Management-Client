package sample.Model.Interface;

public class SoldProductQuantity {
    String productCode;
    int quantity;
    String Customer;
    long orderId;

    public SoldProductQuantity(String productCode, int quantity, String customer, long orderId) {
        this.productCode = productCode;
        this.quantity = quantity;
        Customer = customer;
        this.orderId = orderId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "SoldProductQuantity{" +
                "productCode='" + productCode + '\'' +
                ", quantity=" + quantity +
                ", Customer='" + Customer + '\'' +
                ", orderId=" + orderId +
                '}';
    }
}
