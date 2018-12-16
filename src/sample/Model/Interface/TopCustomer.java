package sample.Model.Interface;

public class TopCustomer {
    String customerName;
    float totalPaid;

    public TopCustomer(String customerName, float getTotalPaid) {
        this.customerName = customerName;
        this.totalPaid = getTotalPaid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public float getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(float getTotalPaid) {
        this.totalPaid = getTotalPaid;
    }

    @Override
    public String toString() {
        return "TopCustomer{" +
                "customerName='" + customerName + '\'' +
                ", getTotalPaid=" + totalPaid +
                '}';
    }
}
