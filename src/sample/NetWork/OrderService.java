package sample.NetWork;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sample.Model.Inventory;
import sample.Model.Order;
import sample.Model.OrderLine;
import sample.Model.ProductInput;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

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
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL, "POST", gson.toJson(order));
        HttpConfig.processResponseCode(connection);
        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        return receivedData;
    }
    public List<Order> getAll() throws Exception
    {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL, "GET", null);

        HttpConfig.processResponseCode(connection);

        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<Order>>(){}.getType();
        List<Order> orders = gson.fromJson(receivedData, listType);

        return orders;
    }

    public List<OrderLine> getOrderItems(long id) throws Exception {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL+id+"/items", "GET", null);

        HttpConfig.processResponseCode(connection);

        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<OrderLine>>(){}.getType();
        List<OrderLine> orderItems = gson.fromJson(receivedData, listType);

        return orderItems;
    }

    public List<Order> getOrderByCustomer(long id) throws Exception {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL+"customer/"+id, "GET", null);

        HttpConfig.processResponseCode(connection);

        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<Order>>(){}.getType();
        List<Order> orders = gson.fromJson(receivedData, listType);

        return orders;
    }
    public List<Order> getUnPaidOrder() throws Exception
    {
        HttpURLConnection connection = HttpConfig.makeRESTRequest("https://stormy-ridge-84291.herokuapp.com/analysis/unpaid", "GET", null);

        HttpConfig.processResponseCode(connection);

        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<Order>>(){}.getType();
        List<Order> orders = gson.fromJson(receivedData, listType);

        return orders;
    }
    public List<Order> getBetweenDate(String startDate, String endDate) throws Exception
    {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL+"date/between?startDate="+startDate+"&endDate="+endDate, "GET", null);

        HttpConfig.processResponseCode(connection);

        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<Order>>(){}.getType();
        List<Order> orders = gson.fromJson(receivedData, listType);

        return orders;
    }
}
