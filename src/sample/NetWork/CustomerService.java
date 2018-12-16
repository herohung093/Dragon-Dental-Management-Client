package sample.NetWork;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sample.Model.Customer;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

public class CustomerService {
    private String BASE_URL = UrlConfig.APP_BASE_URL+"customer/";
    // A Gson object that we will use for conversion JSON strings
    // to objects and vice versa
    //
    private Gson gson = new Gson();

    public CustomerService(){

    }
    public List<Customer> getAll() throws Exception
    {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL, "GET", null);

        HttpConfig.processResponseCode(connection);

        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        // Convert JSON string into a list of Products, and display them
        Type listType = new TypeToken<List<Customer>>(){}.getType();
        List<Customer> customers = gson.fromJson(receivedData, listType);
        return customers;
    }

    public String createCustomer(Customer customer) throws Exception {
        HttpURLConnection connection = HttpConfig.makeRESTRequest(BASE_URL, "POST", gson.toJson(customer));
        HttpConfig.processResponseCode(connection);

        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        return receivedData;
    }

    public String updateCustomer(Customer customer) throws Exception {
        HttpURLConnection connection = HttpConfig.makeRESTRequest("http://localhost:8080/customer/", "PUT", gson.toJson(customer));
        HttpConfig.processResponseCode(connection);

        //	get received JSON string
        String receivedData = HttpConfig.getReceivedData(connection);
        System.out.println(receivedData);
        return receivedData;
    }
}
