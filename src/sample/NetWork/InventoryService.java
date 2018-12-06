package sample.NetWork;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sample.Model.Inventory;
import sample.Model.Product;
import sample.Model.ProductInput;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class InventoryService {
    private String BASE_URL = "https://stormy-ridge-84291.herokuapp.com/inventory/";
    // A Gson object that we will use for conversion JSON strings
    // to objects and vice versa
    //
    private Gson gson = new Gson();

    public InventoryService(){

    }

    public List<Inventory> getAll() throws Exception
    {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL, "GET", null);

        HttpConfig.processResponseCode(connection);

        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);

        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<Inventory>>(){}.getType();
        List<Inventory> products = gson.fromJson(receivedData, listType);

        return products;
    }
    public String updateInventory(ProductInput productInput) throws Exception {

        HttpURLConnection connection = HttpConfig.makeRESTRequest("https://stormy-ridge-84291.herokuapp.com/productinput/", "PUT", gson.toJson(productInput));
        HttpConfig.processResponseCode(connection);
        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        return receivedData;
    }

    public String increaseStock(ProductInput productInput) throws Exception {
        HttpURLConnection connection = HttpConfig.makeRESTRequest("https://stormy-ridge-84291.herokuapp.com/productinput/increase", "PUT", gson.toJson(productInput));
        HttpConfig.processResponseCode(connection);
        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        return receivedData;
    }
}
