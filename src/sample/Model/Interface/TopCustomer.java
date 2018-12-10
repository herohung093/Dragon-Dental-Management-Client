package sample.Model.Interface;

public class TopCustomer {
    String customerName;
    float getTotalPaid;

    public TopCustomer(String customerName, float getTotalPaid) {
        this.customerName = customerName;
        this.getTotalPaid = getTotalPaid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public float getGetTotalPaid() {
        return getTotalPaid;
    }

    public void setGetTotalPaid(float getTotalPaid) {
        this.getTotalPaid = getTotalPaid;
    }

    @Override
    public String toString() {
        return "TopCustomer{" +
                "customerName='" + customerName + '\'' +
                ", getTotalPaid=" + getTotalPaid +
                '}';
    }
}
