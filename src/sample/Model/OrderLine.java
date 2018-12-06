package sample.Model;

import java.util.Objects;

public class OrderLine {
    Product product;
    private Order order;
    private int quantity;
    private float price;
    private float totalPrice;
    private int discount;

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    private void calculateTotalPrice(){
        totalPrice=((quantity * price)*(100-discount))/100;
    }
    public float getTotalPrice() {
        calculateTotalPrice();
        return totalPrice;
    }
    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
    public OrderLine(Product product, Order order, int quantity, float price, int discount) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        getTotalPrice();
    }
    public OrderLine(String product, Order order, int quantity, float price, int discount) {
        this.product = new Product(product);
        this.order = order;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        getTotalPrice();
    }
    public OrderLine(Product product, int quantity, float price, int discount) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        getTotalPrice();
    }
    public OrderLine(String productCode, int quantity, float price, int discount){
        this.product = new Product(productCode);
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        getTotalPrice();
    }
    public OrderLine() {
    }

    public String getProduct() {
        return product.getCode();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        getTotalPrice();
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "product=" + product.getCode() +
                ", order=" + order +
                ", quantity=" + quantity +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderLine)) return false;
        OrderLine orderLine = (OrderLine) o;
        return getQuantity() == orderLine.getQuantity() &&
                Float.compare(orderLine.getOrder().getId(), getOrder().getId()) == 0 &&
                (getProduct().equals(orderLine.getProduct()) ) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProduct(), getOrder(), getQuantity(), getPrice());
    }
}
