package sample.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    long id;
    Customer customer;
    Staff staff;
    LocalDate createAt;
    LocalDate updateAt;
    String note;
    float paid = 0;
    boolean isInstalment;
    List<OrderLine> orderLines = new ArrayList<>();
    public Order(Customer customer, Staff staff) {
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDate updateAt) {
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
                ", customer=" + customer.getContactPerson() +
                ", staff=" + staff.getName() +
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
