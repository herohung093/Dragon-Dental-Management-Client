package sample.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    long id;
    String customer;
    String staff;
    String createAt;
    String updateAt;
    String note;
    float paid = 0;
    boolean isInstalment;
    List<OrderLine> orderLines = new ArrayList<>();
    public Order(String customer, String staff) {
        this.customer = customer;
        this.staff = staff;
        this.isInstalment = false;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public boolean isInstalment() {
        return isInstalment;
    }

    public void setInstalment(boolean instalment) {
        isInstalment = instalment;
    }

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;

    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", staff=" + staff +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", note='" + note + '\'' +
                ", paid=" + paid +
                ", isInstalment=" + isInstalment +
                ", orderLines=" + orderLines +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getId() == order.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
