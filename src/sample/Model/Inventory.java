package sample.Model;

public class Inventory {
    private String code;
    private int stock;

    public Inventory(String code, int stock) {
        this.code = code;
        this.stock = stock;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "code='" + code + '\'' +
                ", stock=" + stock +
                '}';
    }
}
