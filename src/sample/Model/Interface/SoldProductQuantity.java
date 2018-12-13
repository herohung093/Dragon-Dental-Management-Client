package sample.Model.Interface;

public class SoldProductQuantity {
    String productCode;
    int quantity;
    String customer;
    long orderId;

    public SoldProductQuantity(String productCode, int quantity, String customer, long orderId) {
        this.productCode = productCode;
        this.quantity = quantity;
        this.customer = customer;
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
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
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
                ", debtCustomer='" + customer + '\'' +
                ", id=" + orderId +
                '}';
    }
}
