package sample.NetWork;

import sample.Model.Customer;
import sample.Model.Inventory;
import sample.Model.Product;
import sample.Model.Staff;

import java.util.List;

public class DataController {
    List<Inventory> inventories;
    List<Staff> staff;
    List<Product> products;
    List<Customer> customers;
    InventoryService inventoryService = new InventoryService();
    StaffService staffService = new StaffService();
    ProductService productService = new ProductService();
    CustomerService customerService = new CustomerService();
    private  static final DataController instance = new DataController();
    private DataController() {
        try {
           /* inventories = inventoryService.getAll();
            staff = staffService.getAll();
            products = productService.getAll();
            customers = customerService.getAll();*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static DataController getDataInstance(){
        return instance;
    }
    public List<Inventory> getInventories() {
        return inventories;
    }

    public List<Staff> getStaff() {
        return staff;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}
