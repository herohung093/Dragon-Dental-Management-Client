package sample.Model.Interface;

public class BestSeller {
    String productCode;
    int totalSold;

    public BestSeller(String productCode, int totalSold) {
        this.productCode = productCode;
        this.totalSold = totalSold;
    }

    public BestSeller() {
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }

    @Override
    public String toString() {
        return "BestSeller{" +
                "productCode='" + productCode + '\'' +
                ", totalSold=" + totalSold +
                '}';
    }
}
