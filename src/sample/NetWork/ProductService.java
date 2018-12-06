package sample.NetWork;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import sample.Model.Inventory;
import sample.Model.Product;
import sample.Model.ProductInput;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

public class ProductService {
    private String BASE_URL = "https://stormy-ridge-84291.herokuapp.com/products/";
    // A Gson object that we will use for conversion JSON strings
    // to objects and vice versa
    //
    private Gson gson = new Gson();

    public ProductService() {

    }

    public List<Product> getAll() throws Exception
    {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL, "GET", null);
        HttpConfig.processResponseCode(connection);
        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<Product>>(){}.getType();
        List<Product> products = gson.fromJson(receivedData, listType);
        return products;
    }

    public String addProduct(ProductInput productInput) throws Exception {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL, "POST", gson.toJson(productInput));
        HttpConfig.processResponseCode(connection);
        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        return receivedData;
    }
}
