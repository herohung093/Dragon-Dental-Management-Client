package sample.NetWork;

import com.google.gson.Gson;
import sample.Model.Order;
import sample.Model.ProductInput;

import java.net.HttpURLConnection;

public class OrderService {
    private String BASE_URL = "https://stormy-ridge-84291.herokuapp.com/order/";
    // A Gson object that we will use for conversion JSON strings
    // to objects and vice versa
    //
    private Gson gson = new Gson();

    public OrderService() {
    }
    public String createOrder(Order order) throws Exception {
        System.out.println(gson.toJson(order));
        HttpURLConnection connection = HttpConfig.makeRESTRequest("http://localhost:8080/order/", "POST", gson.toJson(order));
        HttpConfig.processResponseCode(connection);
        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        return receivedData;
    }
}
