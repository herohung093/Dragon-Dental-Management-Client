package sample.NetWork;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sample.Model.Product;
import sample.Model.ProductInput;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

public class ProductInputService {
    private String BASE_URL = UrlConfig.APP_BASE_URL+"productinput/";
    // A Gson object that we will use for conversion JSON strings
    // to objects and vice versa
    //
    private Gson gson = new Gson();

    public ProductInputService() {
    }
    public List<ProductInput> getAll() throws Exception {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL, "GET", null);
        HttpConfig.processResponseCode(connection);
        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        System.out.println(receivedData);
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<ProductInput>>(){}.getType();
        List<ProductInput> products = gson.fromJson(receivedData, listType);
        System.out.println(products);
        return products;
    }
    public List<ProductInput> getBetweenDate(String startDate, String endDate) throws Exception {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL+"date/between?startDate="+startDate+"&endDate="+endDate, "GET", null);
        HttpConfig.processResponseCode(connection);
        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        System.out.println(receivedData);
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<ProductInput>>(){}.getType();
        List<ProductInput> products = gson.fromJson(receivedData, listType);
        System.out.println(products);
        return products;
    }
}
