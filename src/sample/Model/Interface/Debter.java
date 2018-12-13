package sample.Model.Interface;

public class Debter {
    String customer;
    float total;
    float paid;
    long orderId;
    String date;

    public Debter() {
    }

    public Debter(String debtCustomer, float total, float paid, long id, String createDate) {
        this.customer = debtCustomer;
        this.total = total;
        this.paid = paid;
        this.orderId = id;
        this.date = createDate;
    }

    public String getDebtCustomer() {
        return customer;
    }

    public void setDebtCustomer(String debtCustomer) {
        this.customer = debtCustomer;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public long getId() {
        return orderId;
    }

    public void setId(long id) {
        this.orderId = id;
    }

    public String getCreateDate() {
        return date;
    }

    public void setCreateDate(String createDate) {
        this.date = createDate;
    }

    @Override
    public String toString() {
        return "Debter{" +
                "customer='" + customer + '\'' +
                ", total=" + total +
                ", paid=" + paid +
                ", orderId=" + orderId +
                ", date='" + date + '\'' +
                '}';
    }
}
