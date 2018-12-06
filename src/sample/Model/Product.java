package sample.Model;

public class Product {
    private String code;
    private String name;
    private Float price;
    private String unit;

    public Product(String code, String name, float price, String unit) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.unit = unit;
    }

    public Product(String code, float price, String unit) {
        this.code = code;
        this.price = price;
        this.unit = unit;
    }

    public Product(String code) {
        this.code = code;
    }

    public Product(String code, String unit) {
        this.code = code;
        this.unit = unit;
    }

    public Product() {}

      public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Product{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                '}';
    }
}
