package sample.Model;

import java.time.LocalDate;

public class ProductInput {
    private long id;
    private Product product;
    private String createAt;
    private String updateAt;
    private String description;
    private int quantity;
    private Staff operator;
    public ProductInput() {
    }

    public ProductInput(Product product, String description, int quantity, Staff operator) {
        this.product = product;
        this.description = description;
        this.quantity = quantity;
        this.operator = operator;
    }

    public long getId() {
        return id;
    }

    public String getProduct() {
        return product.getCode();
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getOperator() {
        return operator.getName();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOperator(Staff operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "ProductInput{" +
                "id=" + id +
                ", product=" + product +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", operator=" + operator +
                '}';
    }
}
